/**
 * 
 */
package electronics.logic.entities;

import java.util.HashMap;

import helper.InvalidSignalException;
import helper.ProcGenException;
import electronics.logic.helper.Entity;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Multiplexer extends Entity{

	// key is the digital representation of the the selectionSignal
	HashMap<String,String> inputOutputMapping = new HashMap<String,String>();
	public Multiplexer(String name,int widthOfSelectionInput,int widthOfInputSignal) throws ProcGenException {
		super(name);
		
		// 2^widthOfSelectionInput
		int noOfInputs = 1<<widthOfSelectionInput;
		
		this.addInput("selectionInput", widthOfSelectionInput);
		
		for(int i =0;i<noOfInputs;i++){
			this.addInput("input" + i, widthOfInputSignal);
		}
		
		// only one output for MUX
		this.addOutput("output", widthOfInputSignal);
	}
	
	public void programMux(String selectionSignalBusValue, String inputSignalName){
		if(this.getInputPortNames().contains(inputSignalName)){
			inputOutputMapping.put(selectionSignalBusValue, inputSignalName);
		}
	}
	
	@Override
	public void defaultBehaviour() throws InvalidSignalException{
		SignalBus selectionSignal = this.getInputByName("selectionInput");
		String selectionSignalValue = selectionSignal.getDisplayValue();
		
		if (this.inputOutputMapping.containsKey(selectionSignalValue)) {
			String inputSignalToAssign = this.inputOutputMapping
					.get(selectionSignalValue);

			SignalBus inputSignal = this.getInputByName(inputSignalToAssign);

			assert (this.getOutputPortList().size() == 1);

			this.getOutputPortList().get(0).setValue(inputSignal.getValue());
		}
	}
	

}
