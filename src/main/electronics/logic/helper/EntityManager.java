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
	List<Entity> baseEntityList = new ArrayList<Entity>();
	
	public List<Entity> getBaseEntities(){
		return baseEntityList;	
	}
	
	public void addBaseEntity(Entity entityToAdd){
		String newId = generateBaseEntityId();
		entityToAdd.changeEntityId(newId);
		baseEntityList.add(entityToAdd);
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
}
