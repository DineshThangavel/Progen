/**
 * 
 */
package electronics.logic.entities;

import helper.InvalidSignalException;

import java.util.List;

import electronics.logic.helper.Entity;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;

/**
 * @author DINESH THANGAVEL
 *
 */
public class FlipFlop extends Entity{

	SignalBus output;
	
	public FlipFlop(String name,int width) {
		super(name);
		output = new SignalBus("outputSignalStore", width,Signal.UNDEFINED);
	}
	
	public SignalBus defaultBehaviour(List<SignalBus> inputList) throws InvalidSignalException{
		assert(inputList.size()==2); // inputs should be the signal and the clock
		
		SignalBus clk = inputList.get(1);
		assert(clk.getBusWidth() == 1);
		
		if(clk.equals(Signal.HIGH)){
			output.setValue(inputList.get(0).getValue());
		}
		return output;
	}
}
