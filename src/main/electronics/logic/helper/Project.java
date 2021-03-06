/**
 * 
 */
package electronics.logic.helper;

import helper.Consts.ExceptionMessages;
import helper.ProcGenException;

import java.security.InvalidParameterException;

import electronics.logic.simulation.ProjectSimulator;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Project {
	String name;
	ProjectSimulator projectSim = new ProjectSimulator(this);
	EntityManager entityManager =  new EntityManager(this);
	ProjectConnectionManager connectionManager = new ProjectConnectionManager(this);	
	
	public Project(String projectName){
		this.name = projectName;
	}
	
	public void publishEntityChangeEvent(EntityChangeEvent entityChangeEvent) throws ProcGenException {
		this.connectionManager.updateAboutEvent(entityChangeEvent);
	}
	
	public ProjectConnectionManager getConnectionManager(){
		return this.connectionManager;
	}
	
	public EntityManager getEntityManager(){
		return this.entityManager;
	}
	
	public ProjectSimulator getProjectSimulator(){
		return projectSim;	
	}
	
	public String getName(){
		return this.name;
	}
}
