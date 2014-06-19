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
	 * in the next level Key for outer hashmap is the entity id, inner hashmap
	 * key is signal name.
	 */

	// Stores the details of to which entity
	private HashMap<String, HashMap<String, List<Connection>>> connectionDirectory = new HashMap<String, HashMap<String, List<Connection>>>();

	/*
	 * The connection manager is updated in case there is any change Triggered
	 * in entityManager/Entity itself
	 */
	protected void updateConnectionManager(EntityChangeEvent entityChangeDetails)
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

	private void updateNewEntityAddition(Entity entityAfterChange)
			throws ProcGenException {
		if (connectionDirectory.get(entityAfterChange.getId()) != null) {
			throw new ProcGenException(
					Consts.ErrorCodes.ENTITY_ALREADY_PRESENT,
					Consts.ExceptionMessages.CONNECTION_CREATION_ERROR);
		}

		HashMap<String, List<Connection>> signalConnectionMapping = new HashMap<String, List<Connection>>();
		List<SignalBus> allPortsList = entityAfterChange.getOutputPortList();

		for (SignalBus port : allPortsList) {
			// Note currently no connnection is being added to the directory.
			signalConnectionMapping.put(port.getName(), new ArrayList<Connection>());
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

	public void createConnection(Entity sourceEntity, Entity destinationEntity,
			SignalBus inputSignal, SignalBus outputSignal,
			ConnectionType connectType) throws ProcGenException {

		String sourceEntityId = sourceEntity.getId();
		String destinationEntityId = destinationEntity.getId();

		// check if signalbus provided are present in the entity
		boolean areInputOutputSignalsPresent = sourceEntity
				.isSignalPresentInOutputByName(inputSignal.getName())
				&& destinationEntity.isSignalPresentInInputByName(outputSignal
						.getName());

		if (!areInputOutputSignalsPresent)
			throw new ProcGenException(Consts.ErrorCodes.SIGNAL_NOT_RECOGNISED,
					Consts.ExceptionMessages.CANNOT_CONNECT_EXCEPTION);

		Connection newConnectionToAdd = new Connection(sourceEntityId,
				destinationEntityId, inputSignal, outputSignal, connectType);

		assert (connectionDirectory.containsKey(sourceEntityId));
		assert (connectionDirectory.containsKey(destinationEntityId));
		assert (connectionDirectory.get(sourceEntityId).containsKey(inputSignal
				.getName()));

		connectionDirectory.get(sourceEntityId).get(inputSignal.getName())
				.add(newConnectionToAdd);
	}

	public ConnectionManager deepCopy() {
		ConnectionManager newConnectionManagerCopy = new ConnectionManager();
		HashMap<String, HashMap<String, List<Connection>>> connectionDirectoryCopy = new HashMap<String, HashMap<String, List<Connection>>>();

		Iterator iterator = this.connectionDirectory.keySet().iterator();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			HashMap<String, List<Connection>> signalConnectionMapping = this.connectionDirectory
					.get(key);
			HashMap<String, List<Connection>> signalConnectionMappingCopy = new HashMap<String, List<Connection>>();

			Iterator signalConnIterator = signalConnectionMapping.keySet()
					.iterator();
			while (signalConnIterator.hasNext()) {
				String signalName = (String) signalConnIterator.next();
				List<Connection> connectionList = signalConnectionMapping
						.get(signalName);
				List<Connection> connectionListCopy = new ArrayList<Connection>();

				for (Connection connection : connectionList) {
					Connection newConnectionCopy = connection.deepCopy();
					connectionListCopy.add(newConnectionCopy);
				}
				signalConnectionMappingCopy.put(signalName, connectionListCopy);
			}
			connectionDirectoryCopy.put(key, signalConnectionMappingCopy);
		}

		newConnectionManagerCopy.connectionDirectory = connectionDirectoryCopy;
		return newConnectionManagerCopy;
	}

	public List<Connection> getConnectionsForEntity(String EntityId,
			String SignalName) {
		List<Connection> connectionList = connectionDirectory.get(EntityId)
				.get(SignalName);
		List<Connection> newConnectionList = new ArrayList<Connection>();
		for (Connection connection : connectionList) {
			newConnectionList.add(connection.deepCopy());
		}
		return newConnectionList;
	}
}
