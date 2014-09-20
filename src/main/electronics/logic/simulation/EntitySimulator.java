/**
 * 
 */
package electronics.logic.simulation;

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
	
	public void runSimulation() throws ProcGenException {
		// If no sub-components execute the behaviour of the entity
		if(hostEntity.getChildEntityList().size()==0){
			hostEntity.defaultBehaviour(hostEntity.getInputPortList());
		}
		
	}

	
}
