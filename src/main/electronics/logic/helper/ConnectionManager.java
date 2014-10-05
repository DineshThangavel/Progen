/**
 * 
 */
package electronics.logic.helper;

import helper.Consts;
import helper.ProcGenException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import electronics.logic.helper.EntityChangeEvent.EntityChangeType;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class ConnectionManager {

	/*
	 * The list of connections is stored as per entity and the signal port. This
	 * will help in easily determining what are the entities that are connected
	 * in the next level. Key for outer hashmap is the entity id, inner hashmap
	 * key is signal name.
	 */

	// Stores the details of to which entity
	protected HashMap<String, HashMap<String, List<Connection>>> connectionDirectory = new HashMap<String, HashMap<String, List<Connection>>>();

	/*
	 * The connection manager is updated in case there is any change Triggered
	 * in entityManager/Entity itself
	 */
	protected void updateAboutEvent(EntityChangeEvent entityChangeDetails)
			throws ProcGenException {
		if (entityChangeDetails.getChangeType() == EntityChangeType.AddEntityEvent) {
			updateNewEntityAddition(entityChangeDetails.getEntityAfterChange());
		}

		else if (entityChangeDetails.getChangeType() == EntityChangeType.DeleteEntityEvent) {
			updateEntityDeletion(entityChangeDetails.getEntityAfterChange());
		}

		else if (entityChangeDetails.getChangeType() == EntityChangeType.ModifyEntityEvent) {
			updateEntityModification(
					entityChangeDetails.getEntityBeforeChange(),
					entityChangeDetails.getEntityAfterChange());
		}

	}

	// the details of the child entity added is stored in the parent's connection manager
	private void updateNewEntityAddition(Entity entityAfterChange)
			throws ProcGenException {
		
		HashMap<String, HashMap<String, List<Connection>>> connectionDirectoryToModify = null;		
			
		if (connectionDirectory.get(entityAfterChange.getId()) != null) {
			throw new ProcGenException(
					Consts.ErrorCodes.ENTITY_ALREADY_PRESENT,
					Consts.ExceptionMessages.CONNECTION_CREATION_ERROR);
		}

		HashMap<String, List<Connection>> signalConnectionMapping = new HashMap<String, List<Connection>>();
		List<String> allPortsNameList = entityAfterChange.getAllPortsName();

		for (String port : allPortsNameList) {
			// Note currently no connnection is being added to the directory.
			signalConnectionMapping.put(port, new ArrayList<Connection>());
		}

		connectionDirectory.put(entityAfterChange.getId(),
				signalConnectionMapping);
	}

	private void updateEntityDeletion(Entity entityToDelete) {
		// check if any of the other entity signals are connected to
		// signal in this entity
		
		// Iterate through all entities
		Iterator entitiesIterator = this.connectionDirectory.keySet().iterator();
		while(entitiesIterator.hasNext()){
			String entityName = (String) entitiesIterator.next();
			
			HashMap<String, List<Connection>> signalConnectionMappingHashMap = this.connectionDirectory.get(entityName); 
			Iterator signalIterator = this.connectionDirectory.get(entityName).keySet().iterator();
			
			while(signalIterator.hasNext()){
				String signalName = (String) signalIterator.next();
				List<Connection> connectionListForPorts = signalConnectionMappingHashMap.get(signalName);
				for(Connection connection:connectionListForPorts)
				{
					if(connection.destinationEntityId == entityToDelete.getId()){
						connectionListForPorts.remove(connection);
					}
				}
			}
		}
		
		connectionDirectory.remove(entityToDelete);
	}

	// only one modification will be contained in an entity after change
	private void updateEntityModification(Entity entityBeforeChange,
			Entity entityAfterChange) {
		
		HashMap<String, List<Connection>> signalConnectionMapping = this.connectionDirectory
				.get(entityBeforeChange.getId());

		// assumption validation
		if (signalConnectionMapping != null) {
			assert (signalConnectionMapping.size() == entityBeforeChange
					.getOutputPortList().size());
			for (SignalBus outputSignal : entityBeforeChange
					.getOutputPortList()) {
				assert (signalConnectionMapping.containsKey(outputSignal
						.getName()));
			}
		}
		// Check for what has changed

		// TODO: check if enitity Id change case is needed

		// Signal bus deleted
		if(signalConnectionMapping!=null){
		if (entityAfterChange.getNumberOfOutputs() == (entityBeforeChange
				.getNumberOfOutputs() - 1)) {
			// port has been deleted. find out which one
			String deletedSignalName = null;

			for (int i = 0; i < entityBeforeChange.getNumberOfOutputs(); i++) {
				if (!entityAfterChange.getOutputPortList().contains(
						entityBeforeChange.getOutputPortList().get(i))) {
					deletedSignalName = entityBeforeChange.getOutputPortList()
							.get(i).getName();
					break;
				}
			}

			if (deletedSignalName != null) {
				signalConnectionMapping.remove(deletedSignalName);
				return;
			}
		}
		}
		
		// either a new addition or a rename
		for (SignalBus outputSignal : entityAfterChange.getOutputPortList()) {
			if (!signalConnectionMapping.containsKey(outputSignal.getName())) {
				// hence check for entityBeforeChange's size

				if (entityAfterChange.getNumberOfOutputs() != entityBeforeChange
						.getNumberOfOutputs()) {
					// there has been an addition
					signalConnectionMapping.put(outputSignal.getName(), new ArrayList<Connection>());
					break;
				}

				else {
					// it is a renaming, so just just change mapping
					String signalNameBeforeRenaming = null;
					String signalNameAfterRenaming = outputSignal.getName();
					for (int i = 0; i < entityBeforeChange.getNumberOfOutputs(); i++) {
						if (!entityAfterChange.getOutputPortList().contains(
								entityBeforeChange.getOutputPortList().get(i))) {
							signalNameBeforeRenaming = entityBeforeChange
									.getOutputPortList().get(i).getName();
							break;
						}
					}

					List<Connection> connectionListCopy = new ArrayList<Connection>();

					for (Connection connection : signalConnectionMapping
							.get(signalNameBeforeRenaming)) {
						Connection newConnectionCopy = connection.deepCopy();
						connectionListCopy.add(newConnectionCopy);
					}

					signalConnectionMapping.remove(signalNameBeforeRenaming);
					signalConnectionMapping.put(signalNameAfterRenaming,
							connectionListCopy);

				}
			}
		}

	}

	public void createConnectionBetweenBaseEntities(Entity sourceEntity, Entity destinationEntity,
			SignalBus inputSignal, SignalBus outputSignal,
			ConnectionType connectType) throws ProcGenException {

		String sourceEntityId = sourceEntity.getId();
		String destinationEntityId = destinationEntity.getId();

		Connection newConnectionToAdd = new Connection(sourceEntityId,
				destinationEntityId, inputSignal, outputSignal, connectType);

		assert (connectionDirectory.containsKey(sourceEntityId));
		assert (connectionDirectory.containsKey(destinationEntityId));
		assert (connectionDirectory.get(sourceEntityId).containsKey(inputSignal
				.getName()));

		connectionDirectory.get(sourceEntityId).get(inputSignal.getName())
				.add(newConnectionToAdd);
	}

/*
 * @return: returns a reference to connections. Hence do not edit the values obtained
 */
	public List<Connection> getConnectionsForSignal(String EntityId,
			String SignalName) {
		// TODO: check for entityId and validate
		
		List<Connection> connectionList = connectionDirectory.get(EntityId)
				.get(SignalName);
	
		return connectionList;
	}
	
	/*
	 * 	@return: returns a reference to connections. Hence do not edit the values obtained
	 */
	public HashMap<String, List<Connection>> getConnectionForEntity(String EntityId){
		
		// TODO: validate entity Id and check if entity is present with that Id
		
		HashMap<String, List<Connection>> signalConnectionMapping = this.connectionDirectory
				.get(EntityId);
		
		return signalConnectionMapping;
	}
	
	public List<Connection> getAllConnectionsInEntityAsList(String entityId){
		
		HashMap<String,List<Connection>> connectionListForSignals = this.connectionDirectory.get(entityId);
		List<Connection> allConnectionsList = new ArrayList<Connection>();
		
		for (String signalName:connectionListForSignals.keySet()){
			List<Connection> connectionList = this.connectionDirectory.get(entityId).get(signalName);
			if(connectionList != null){
				allConnectionsList.addAll(connectionList);
			}
		}
		
		return allConnectionsList;
	}
}
