/**
 * 
 */
package electronics.logic.helper;

import helper.Consts;
import helper.InvalidSignalException;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class SignalBus {

	Signal value[];
	String name;

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

		if (value.length != valueToSet.length)
			throw new InvalidSignalException(
					Consts.ErrorCodes.UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT,
					Consts.ExceptionMessages.UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT);

		for (int i = 0; i < value.length; i++) {
			value[i] = valueToSet[i];
		}
		return true;
	}

	/**
	 * This method assigns the value to set to the entire bus
	 * 
	 * @param valueToSet
	 * @return
	 */
	public boolean setValue(Signal valueToSet) {
		for (int i = 0; i < value.length; i++) {
			value[i] = valueToSet;
		}

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

		value[indexToSet] = valueToSet;
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

}
