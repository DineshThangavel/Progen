/**
 *This class start the flow of the program 
 */
package electronics.logic.helper;

import electronics.logic.entities.Entity;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Simulator {
	Entity triggeringEntity;
	boolean isTimeTriggered;
	
	Simulator(Entity newTriggeringEntity,boolean isTimeTriggered){
		this.triggeringEntity = newTriggeringEntity;
		this.isTimeTriggered = isTimeTriggered;
	}
	
	public void runSimulation(){
				
	}
}
