/**
 * 
 */
package electronics.logic.simulation;

import java.beans.PropertyChangeEvent;

import electronics.logic.helper.Entity;
import helper.ProcGenException;

/**
 * @author DINESH THANGAVEL
 *
 */
public class EntitySimulator extends Simulator{

	Entity hostEntity = null;
	
	public EntitySimulator(Entity hostEntity){
		this.hostEntity = hostEntity;
	}
	
	public void processInputChange(PropertyChangeEvent evt) throws ProcGenException{
		this.runSimulation();
	}
	
	public void runSimulation() throws ProcGenException {
		// If no sub-components execute the behaviour of the entity
		if(hostEntity.getChildEntityList().size()==0){
			hostEntity.defaultBehaviour();
		}
		
	}

	
}
