/**
 * 
 */
package electronics.logic.helper;

/**
 * @author DINESH THANGAVEL
 *
 */
public  class EntityChangeEvent {
	public enum EntityChangeType{
		AddEntityEvent,
		DeleteEntityEvent,
		ModifyEntityEvent;
	}
	
	EntityChangeType changeType;
	Entity entityBeforeChange ;
	Entity entityAfterChange;
	
	
public EntityChangeEvent(EntityChangeType changeType, Entity oldEntity, Entity changedEntity){
		this.changeType = changeType;
		this.entityBeforeChange = oldEntity;
		this.entityAfterChange = changedEntity;
}

public EntityChangeType getChangeType(){
	return this.changeType;
}

public Entity getEntityBeforeChange(){
	return this.entityBeforeChange;
}

public Entity getEntityAfterChange(){
	return this.entityAfterChange;
}

}

