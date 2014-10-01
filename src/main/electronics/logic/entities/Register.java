/**
 * 
 */
package electronics.logic.entities;

import hdl.translator.logic.HdlConverter;
import hdl.translator.logic.HdlConsts.HdlConversionType;
import helper.Consts;
import helper.InvalidSignalException;
import helper.ProcGenException;

import java.util.List;

import electronics.logic.helper.Entity;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Register extends Entity{
	
	public Register(String name,int width,String triggerType) throws ProcGenException {
		super(name);
		
		this.addInput("input0",width);
		this.addInput("enable", 1);
		this.addInput("clock", 1);
		
		// only one output for AND gate
		this.addOutput("output", width);
		
		if(triggerType.compareToIgnoreCase("rising")==0){
			this.setEntityTriggerType(EntityTriggerType.RISING_EDGE_TRIGGERED);
		}
		
		else if(triggerType.compareToIgnoreCase("falling")==0){
			this.setEntityTriggerType(EntityTriggerType.FALLING_EDGE_TRIGGERED);
		}
		
		else{
			throw new ProcGenException(Consts.ExceptionMessages.INCORRECT_TRIGGER_TYPE);
		}
		
		this.setHdlConversionType(HdlConversionType.InlineConversion);
	}
	
	public void defaultBehaviour() throws InvalidSignalException{
		
		List<SignalBus> inputList = this.getInputPortList();
		assert(inputList.size()==3); // inputs should be the signal and the clock
		
		SignalBus clk = inputList.get(2);
		assert(clk.getBusWidth() == 1);
		
		SignalBus enable = inputList.get(1);
		assert(enable.getBusWidth() == 1);
		
		if(clk.getValue()[0].equals(Signal.HIGH) && enable.getValue()[0].equals(Signal.HIGH)){
			SignalBus inputForRegister = this.getInputByName("input0");
			this.getOutputByName("output").setValue(inputForRegister.getValue());		 
		}
	}
	
	@Override
	public Object convertToHdl(HdlConverter hdlConverter){
		return hdlConverter.convertRegister(this);
	}
}
