/**
 * This class is the trigger that has to be connected to the input of entity. 
 */
package electronics.logic.simulation;

import electronics.logic.helper.SignalBus;
import helper.InvalidSignalException;

/**
 * @author DINESH THANGAVEL
 *
 */
public class InputSimulator extends SignalBus{

	public InputSimulator(String name, int busWidth) {
		super(name, busWidth);
	}
	
}
