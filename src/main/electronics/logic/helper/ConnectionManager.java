/**
 * 
 */
package electronics.logic.helper;

import helper.Consts;
import helper.ProcGenException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	// TODO: See if there is an efficient data structure for this
	// Stores the details of to which entity 
	private HashMap<String, HashMap<String, List<Connection>>> connectionDirectory = new HashMap<String, HashMap<String, List<Connection>>>();
	
	
	/*
	 *  The connection manager is updated in case there is any change Triggered in entityManager/Entity itself
	 */
	public void updateConnectionManager(EntityChangeEvent entityChangeDetails) throws ProcGenException{
		if(entityChangeDetails.getChangeType() == EntityChangeType.AddEntityEvent){
			updateNewEntityAddition(entityChangeDetails.getEntityAfterChange());
		}
		
		else if(entityChangeDetails.getChangeType() == EntityChangeType.DeleteEntityEvent){
			updateEntityDeletion(entityChangeDetails.getEntityAfterChange());
		}
		
		else if(entityChangeDetails.getChangeType() == EntityChangeType.ModifyEntityEvent){
			updateEntityModification(entityChangeDetails.getEntityBeforeChange(),entityChangeDetails.getEntityAfterChange());
		}
		
	}
	
	private void updateNewEntityAddition(Entity entityAfterChange) throws ProcGenException {
		if(connectionDirectory.get(entityAfterChange.getId())!=null){
			throw new ProcGenException(Consts.ErrorCodes.ENTITY_ALREADY_PRESENT,Consts.ExceptionMessages.CONNECTION_CREATION_ERROR);
		}
		
		HashMap<String,List<Connection>> signalConnectionMapping = new HashMap<String,List<Connection>> ();
		List<SignalBus> allPortsList = entityAfterChange.getOutputPortList();
		
		for(SignalBus port:allPortsList){
			// Note currently no connnection is being added to the directory.
			signalConnectionMapping.put(port.getName(),null);
		}
		
		connectionDirectory.put(entityAfterChange.getId(),signalConnectionMapping);
	}


	private void updateEntityDeletion(Entity entityToDelete) {
		connectionDirectory.remove(entityToDelete);
		
		// TODO: check if any of the other entity signals are connected to signal in this entity
		
		
	}
	
	
	private void updateEntityModification(Entity entityBeforeChange,
			Entity entityAfterChange) {
		// TODO Auto-generated method stub
		
	}


	public void createConnection(Entity sourceEntity, Entity destinationEntity,
			SignalBus inputSignal, SignalBus outputSignal, ConnectionType connectType)
			throws ProcGenException {

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
		
		Connection newConnectionToAdd = new Connection(sourceEntityId,destinationEntityId,inputSignal,outputSignal,connectType); 
		
		assert(connectionDirectory.containsKey(sourceEntityId));
		assert(connectionDirectory.containsKey(destinationEntityId));
		assert(connectionDirectory.get(sourceEntityId).containsKey(inputSignal.getName()));
	
		connectionDirectory.get(sourceEntityId).get(inputSignal.getName()).add(newConnectionToAdd);
	}
}
