/**
 *This class start the flow of the program 
 */
package electronics.logic.helper;

import java.util.HashMap;
import java.util.List;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ProjectSimulator implements Simulator{
	
	Project hostProject = null;
	HashMap<String,List<SignalBus>> inputSimulatorDirectory = new HashMap<String,List<SignalBus>>();
	
	ProjectSimulator(Project hostProject){
		this.hostProject = hostProject;
	}
	
	public void runSimulation(){
		// 		
	}

	/*
	 * @input
	 */
	public void addInputSimulator(InputSimulator newInputSimulator, String entityId, String signalName) {
		
	}
}
