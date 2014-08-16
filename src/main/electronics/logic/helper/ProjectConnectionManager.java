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

import electronics.logic.helper.EntityChangeEvent.EntityChangeType;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ProjectConnectionManager extends ConnectionManager{
	
	private Project hostProject;
	
	public ProjectConnectionManager(Project hostProject){
		this.hostProject = hostProject;
	}

	public void updateAboutEvent(EntityChangeEvent entityChangeEvent) throws ProcGenException{
		Entity affectedEntity = entityChangeEvent.entityAfterChange;
		if(affectedEntity.getParent() == null){
			super.updateAboutEvent(entityChangeEvent);
		}
		
		else{
		String parentId = affectedEntity.getParent().getId();
		Entity parentEntity = hostProject.getEntityManager().getEntityById(parentId);
		EntityConnectionManager cm = parentEntity.getEntityConnectionManager();
		cm.updateAboutEvent(entityChangeEvent);
		}
		
		affectedEntity.notifyEntityChangeEvent(entityChangeEvent);
	}
}
