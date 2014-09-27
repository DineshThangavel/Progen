/**
 * 
 */
package electronics.logic.helper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import helper.Consts;
import helper.InvalidSignalException;
import helper.ProcGenException;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class SignalBus {

	private Signal value[];
	private String name;
		
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	public String getName() {
		return this.name;
	}

	public int getBusWidth() {
		return value.length;
	}

	public SignalBus(String name, int busWidth) {
		this.value = new Signal[busWidth];
		setValue(Signal.UNDEFINED);
		this.name = name;
	}

	public SignalBus(String name, int busWidth, Signal valueToSet) {
		this.value = new Signal[busWidth];
		setValue(valueToSet);
		this.name = name;
	}

	/*
	 * @param valueToSet- this is a binary representation as a string
	 */
	public SignalBus(String name, String valueToSet) throws InvalidSignalException {
		this.value = new Signal[valueToSet.length()];

		for (int i = 0; i < valueToSet.length(); i++) {
			String signalAtIndexi = String.valueOf(valueToSet.charAt(i));
			try {
				setValue(Signal.getSignalFromString(signalAtIndexi), i);
			} catch (InvalidSignalException e) {
				throw e;
			}
		}

		this.name = name;
	}
	
	public boolean setValue(Signal valueToSet[])
			throws InvalidSignalException {
		
		assert(valueToSet != null);
		
		// copy the current value before changing it
		Signal[] previousSignalValue = new Signal[this.value.length];
		for (int i = 0; i < value.length; i++) {
			previousSignalValue[i] = value[i];
		}
		
		if (value.length != valueToSet.length)
			throw new InvalidSignalException(
					Consts.ErrorCodes.UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT,
					Consts.ExceptionMessages.UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT);

		// assign new value for the signal
		for (int i = 0; i < value.length; i++) {
			value[i] = valueToSet[i];
		}
		
		notifyListeners(this,this.name,previousSignalValue,value);
		
		return true;
	}

	/**
	 * This method assigns the value to set to the entire bus
	 * 
	 * @param valueToSet
	 * @return
	 */
	public boolean setValue(Signal valueToSet) {
		
		// copy the current value before changing it
		Signal[] previousSignalValue = new Signal[this.value.length];
		for (int i = 0; i < value.length; i++) {
			previousSignalValue[i] = value[i];
		}
		
		// assign new value for the signal
		for (int i = 0; i < value.length; i++) {
			value[i] = valueToSet;
		}

		notifyListeners(this,this.name,previousSignalValue,value);
		return true;
	}

	/**
	 * This method assigns the value to set at the specified index in the bus
	 * 
	 * @param valueToSet
	 * @return
	 * @throws InvalidSignalException
	 */
	public boolean setValue(Signal valueToSet, int indexToSet)
			throws InvalidSignalException {

		if (indexToSet > this.getBusWidth())
			throw new InvalidSignalException(
					Consts.ErrorCodes.SIGNAL_INDEX_NOT_FOUND,
					Consts.ExceptionMessages.UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT);
		
		// copy the current value before changing it
		Signal[] previousSignalValue = new Signal[this.value.length];
		for (int i = 0; i < value.length; i++) {
			previousSignalValue[i] = value[i];
		}
		
		// set the new value
		value[indexToSet] = valueToSet;
		
		notifyListeners(this,this.name,previousSignalValue,value);
		return true;
	}

	public Signal[] getValue() {
		Signal[] valueCopy = new Signal[value.length];

		for (int i = 0; i < value.length; i++) {
			valueCopy[i] = value[i];
		}
		return valueCopy;
	}
	
	public SignalBus deepCopy(){
		SignalBus newSignalBusCopy = new SignalBus(this.name,this.getBusWidth());
		for(int i=0;i<this.getBusWidth();i++){
			newSignalBusCopy.value[i] = this.value[i];
		}
		
		return newSignalBusCopy;
	}
	
	  private void notifyListeners(Object object, String value, Signal[] value3, Signal[] valueToSet) {
		    for (PropertyChangeListener name : listener) {
		      name.propertyChange(new PropertyChangeEvent(this, value, value3, valueToSet));
		    }
		  }

	  public void addChangeListener(PropertyChangeListener newListener) {
		    listener.add(newListener);
		  }

	public String getDisplayValue() {
		StringBuilder displayString = new StringBuilder();
		Signal[] busValue = this.getValue();
		
		for(int i=0;i<busValue.length;i++){
			if(busValue[i].equals(Signal.HIGH)){
				displayString.append("H");
			}
			else if(busValue[i].equals(Signal.LOW)){
				displayString.append("L");
			}
			
			else{
				displayString.append("Z");
			}
		}
		
		return displayString.toString();
	}
	
	static public Signal[] getSignalArrayFromString(String signalValue) throws ProcGenException{
		Signal[] signalValueArray = new Signal[signalValue.length()];
		
		for(int i=0;i<signalValue.length();i++){
			String c = signalValue.substring(i, i+1);
			
			if(c.equals("1")){
				signalValueArray[i] = Signal.HIGH;
			}
			
			else if(c.equals("0")){
				signalValueArray[i] = Signal.LOW;
			}
			
			else
				throw new ProcGenException(Consts.ExceptionMessages.INPUT_NOT_RECOGNISED);
		}
		return signalValueArray;
	}

}
