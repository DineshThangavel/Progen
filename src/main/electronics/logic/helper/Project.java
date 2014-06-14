/**
 * 
 */
package electronics.logic.helper;

import helper.Consts.ExceptionMessages;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Project {
	String name;
	Simulator flowController = null;
	EntityManager entityManager =  new EntityManager();
	ConnectionManager connectionManager = new ConnectionManager();
	
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
