/**
 * 
 */
package electronics.logic.helper;

import helper.ProcGenException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import userinterface.EntityDetailsRetriever.EntityDetailsFromUser;
import electronics.logic.helper.EntityChangeEvent.EntityChangeType;

/**
 * @author DINESH THANGAVEL
 *
 */
public final class EntityManager {
	ArrayList<Entity> baseEntityList = new ArrayList<Entity>();
	Project parentProject;
	
	public EntityManager(Project project) {
		parentProject = project;
	}

	public List<Entity> getBaseEntities(){
		return (List<Entity>) baseEntityList;	
	}
	
	public void addBaseEntity(Entity entityToAdd) throws ProcGenException{
		String newId = generateBaseEntityId();
		entityToAdd.changeEntityId(newId);
		baseEntityList.add(entityToAdd);
		
		// create EntityChange event
		EntityChangeEvent entityChangeEvent = new EntityChangeEvent(EntityChangeType.AddEntityEvent,null,entityToAdd);
		parentProject.publishEntityChangeEvent(entityChangeEvent);
	}
	
	/*
	 * Ids of baseEntities have Ids with just one digit
	 * eg "1"
	 * Every subentity will have 2 digits separated by '-
	 * eg "1-1'
	 */
	private String generateBaseEntityId(){
		int id = baseEntityList.size() + 1;
		return String.valueOf(id);	
	}
	
	public void addChildEntity(String parentEntityId, Entity newChildEntity) throws ProcGenException{
		Entity parentEntity = getEntityById(parentEntityId);
		String newChildId = parentEntityId +"-" + String.valueOf((parentEntity.getChildEntityList().size() + 1));
		parentEntity.addChildEntity(newChildId, newChildEntity);
		
		// create EntityChange event
		EntityChangeEvent entityChangeEvent = new EntityChangeEvent(EntityChangeType.AddEntityEvent,null,newChildEntity);
		parentProject.publishEntityChangeEvent(entityChangeEvent);
	}
	
	public void addInputSignal(String entityId,String signalName, int signalBusWidth) throws ProcGenException{
		Entity entityToEdit = getEntityById(entityId);
		// Deep copy constructor for Entity
		Entity entityBeforeEndit = entityToEdit.deepCopy();
		entityToEdit.addInput(signalName, signalBusWidth);
		
		EntityChangeEvent entityModificationEvent  =  new EntityChangeEvent(EntityChangeType.ModifyEntityEvent,entityBeforeEndit,entityToEdit);
		this.parentProject.publishEntityChangeEvent(entityModificationEvent);
	}

	public void addOutputSignal(String entityId,String signalName, int signalBusWidth) throws ProcGenException{
		Entity entityToEdit = getEntityById(entityId);
		
		// Deep copy constructor for Entity
		Entity entityBeforeEndit = entityToEdit.deepCopy();
		entityToEdit.addOutput(signalName, signalBusWidth);

		EntityChangeEvent entityModificationEvent  =  new EntityChangeEvent(EntityChangeType.ModifyEntityEvent,entityBeforeEndit,entityToEdit);
		this.parentProject.publishEntityChangeEvent(entityModificationEvent);
	}
	
	public Entity getEntityById(String entityId){
		
		String[] entityLocations = entityId.split("-");
		String baseEntityId = entityLocations[0];
		
		// TODO check for non existing entityId and return null
		try{
			int baseEntityIdAsInt = Integer.valueOf(baseEntityId);
			if(baseEntityIdAsInt <= 0 || baseEntityIdAsInt > baseEntityList.size())
				return null;
			
			// Ids start with 1. So need to subtract 1 to make it an index of array.
			Entity baseEntity = baseEntityList.get(Integer.valueOf(baseEntityId)-1);
			
			assert(baseEntity != null);
			Entity requiredEntity = baseEntity.getChildEntityById(entityId);
			return requiredEntity;
			
		}catch(NumberFormatException e){
			return null;
		}
	}

	public String addEntity(EntityDetailsFromUser newEntityDetails) throws ProcGenException {
		
		Entity newEntityToAdd = new Entity(newEntityDetails.nameOfEntity);
		Entity parentEntity = this.getEntityById(newEntityDetails.parentOfEntityId);
		newEntityToAdd.setParent(parentEntity);
		
		Iterator<String> keyItrForInput = newEntityDetails.inputSignalNames.keySet().iterator();
		
		while(keyItrForInput.hasNext()){
			String signalName = keyItrForInput.next();
			newEntityToAdd.addInput(signalName,newEntityDetails.inputSignalNames.get(signalName));
		}
		
		Iterator<String> keyItrForOutput = newEntityDetails.outputSignalNames.keySet().iterator();
		
		while(keyItrForOutput.hasNext()){
			String signalName = keyItrForOutput.next();
			newEntityToAdd.addOutput(signalName,newEntityDetails.outputSignalNames.get(signalName));
		}

		addEntity(newEntityToAdd);
		
		return newEntityToAdd.getId();		
	}
	
	public String addEntity(Entity newEntityToAdd) throws ProcGenException{
		
		if(newEntityToAdd.getParent()!=null){
			this.addChildEntity(newEntityToAdd.getParent().getId(), newEntityToAdd);		
		}
		
		else{
			this.addBaseEntity(newEntityToAdd);
		}
		
		publishEventForAllChildEntitiesInNewEntity(newEntityToAdd);
		
		return newEntityToAdd.getId();
	}

	private void publishEventForAllChildEntitiesInNewEntity(
			Entity newEntityToAdd) throws ProcGenException {
		
		// create Event for all child entities in newEntityToAdd also
		for(Entity childEntity:newEntityToAdd.getChildEntityList()){
			// create EntityChange event
			EntityChangeEvent entityChangeEvent = new EntityChangeEvent(EntityChangeType.AddEntityEvent,null,childEntity);
			parentProject.publishEntityChangeEvent(entityChangeEvent);
			publishEventForAllChildEntitiesInNewEntity(childEntity);
		}
	}

}
