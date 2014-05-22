/**
 * 
 */
package electronics.logic.entities;

import java.util.List;

import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;

/**
 * @author DINESH THANGAVEL
 *
 */
public class AndGate extends Entity{

	/*
	 *	@param id- ID of or gate which is unique throughout
	 *	@param name - user given name for the gate
	 */
	public AndGate(String id, String name) {
		super(id, name);
	}
	
	@Override
	public SignalBus defaultBehaviour(List<SignalBus> inputList) {
		SignalBus andGateOutput = new SignalBus("andGateOutput",inputList.get(0).getBusWidth());
		andGateOutput.setValue(Signal.HIGH);
		
		if (inputList.size() > 0) {
			for (int i = 0; i < inputList.get(0).getBusWidth(); i++) {
				for (SignalBus input : inputList) {
					assert (input.getBusWidth() == inputList.get(0)
							.getBusWidth());

					if (input.getValue()[i] == Signal.LOW) {
						andGateOutput.setValue(Signal.LOW, i);
					}
				}
			}
		}

		return andGateOutput;
	}
	

}
