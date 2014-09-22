/**
 * 
 */
package electronics.logic.entities;

import helper.InvalidSignalException;
import helper.ProcGenException;

import java.util.ArrayList;
import java.util.List;

import electronics.logic.helper.Entity;
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
	
	int numberOfInputs;
	public AndGate(String id, String name,int noOfInputs) throws ProcGenException {
		super(id, name);
		numberOfInputs = noOfInputs;
		
		int signalBusWidth = 1; 
		
		for(int i =0;i<noOfInputs;i++){
			this.addInput("input" + i, signalBusWidth);
		}
		
		// only one output for AND gate
		this.addOutput("output", signalBusWidth);
	}
	
	public AndGate(String id, String name,int noOfInputs, int signalBusWidth) throws ProcGenException {
		super(id, name);
		numberOfInputs = noOfInputs;
		
		for(int i =0;i<noOfInputs;i++){
			this.addInput("input" + i, signalBusWidth);
		}
		
		// only one output for AND gate
		this.addOutput("output", signalBusWidth);
	}
	
	@Override
	public void defaultBehaviour() throws InvalidSignalException {
		List<SignalBus> inputList = this.getInputPortList();
		
		assert(this.getOutputPortList().size() == 1);
		SignalBus andGateOutput = new SignalBus("tempOutput",this.getOutputPortList().get(0).getBusWidth(),Signal.HIGH);
		
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
		
		this.getOutputPortList().get(0).setValue(andGateOutput.getValue());

	}
	

}
