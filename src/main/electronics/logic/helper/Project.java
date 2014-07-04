/**
 * 
 */
package electronics.logic.helper;

import helper.Consts.ExceptionMessages;
import helper.ProcGenException;

import java.security.InvalidParameterException;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Project {
	String name;
	Simulator flowController = null;
	EntityManager entityManager =  new EntityManager(this);
	ConnectionManager connectionManager = new ConnectionManager();
	
	public Project(String projectName){
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

	public void publishEntityChangeEvent(EntityChangeEvent entityChangeEvent) throws ProcGenException {
		connectionManager.updateConnectionManager(entityChangeEvent);		
	}
	
	public ConnectionManager getConnectionManager(){
		return this.connectionManager;
	}
	
	public EntityManager getEntityManager(){
		return this.entityManager;
	}
}
