/**
 *This class start the flow of the program 
 */
package electronics.logic.helper;

import java.util.List;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ProjectSimulator implements Simulator{
	Entity triggeringEntity;
	boolean isTimeTriggered;
	
	ProjectSimulator(Entity newTriggeringEntity,boolean isTimeTriggered){
		this.triggeringEntity = newTriggeringEntity;
		this.isTimeTriggered = isTimeTriggered;
	}
	
	public void runSimulation(){
		// 		
	}
}
