/**
 * 
 */
package electronics.logic.helper;

import helper.Consts.ExceptionMessages;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import electronics.logic.entities.Entity;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Project {
	String name;
	List<Entity> baseEntityList = new ArrayList<Entity>();
	Simulator flowController = null;
	
	Project(String projectName){
		this.name = projectName;
	}
	
	public void createSimulator(Entity simulationTriggeringEntity, boolean isTimeUsed){
		
		if(simulationTriggeringEntity == null){
			throw new InvalidParameterException(ExceptionMessages.STARTING_ENTITY_NOT_NULL);
		}
		
		if(flowController == null){
			this.flowController = new Simulator(simulationTriggeringEntity, isTimeUsed);
		}
	}
}
