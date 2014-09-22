/**
 * 
 */
package electronics.logic.simulation;

/**
 * @author DINESH THANGAVEL
 *
 */
/*
 * This class keeps track of the stimulus/ command to be executed at the time mentioned
 */
public class CircuitStimulus implements Comparable{
	float time;
	String commandToExecuteAtTime;
	
	public CircuitStimulus(float time,String commandToExecuteAtTime){
		this.time = time;
		this.commandToExecuteAtTime = commandToExecuteAtTime;
	}

	@Override
	public int compareTo(Object o) {
		
		return (int) ((this.time - ((CircuitStimulus)o).time)*100);
	}
	
	
}
