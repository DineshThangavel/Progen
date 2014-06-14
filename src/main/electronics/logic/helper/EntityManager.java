/**
 * 
 */
package electronics.logic.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author DINESH THANGAVEL
 *
 */
public final class EntityManager {
	HashMap<String,Entity> baseEntityMap = new HashMap<String,Entity>();
	
	public List<Entity> getBaseEntities(){
		return (List<Entity>) baseEntityMap;	
	}
	
	public void addBaseEntity(Entity entityToAdd){
		String newId = generateBaseEntityId();
		entityToAdd.changeEntityId(newId);
		baseEntityMap.put(newId,entityToAdd);
	}
	
	/*
	 * Ids of baseEntities have Ids with just one digit
	 * eg "1"
	 * Every subentity will have 2 digits separated by '-
	 * eg "1-1'
	 */
	private String generateBaseEntityId(){
		int id = baseEntityMap.size() + 1;
		return String.valueOf(id);	
	}
	
	public void addChildEntity(String parentEntityId, Entity newChildEntity){
		
	}
	
	public Entity getEntityById(String entityId){
		
	}
}
