/**
 * 
 */
package electronics.logic.helper;

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
	public SignalBus(String name, String valueToSet) {
		this.value = new Signal[valueToSet.length()];

		for (int i = 0; i < valueToSet.length(); i++) {
			String signalAtIndexi = String.valueOf(valueToSet.charAt(i));
			setValue(Signal.getSignalFromString(signalAtIndexi), i);
		}

		this.name = name;
	}

	public boolean setValue(Signal valueToSet[]) {
		// TODO make this an exception
		if (value.length != valueToSet.length)
			return false;

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
	 */
	public boolean setValue(Signal valueToSet, int indexToSet) {

		// TODO : Throw exception here
		if (indexToSet > this.getBusWidth())
			return false;

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

}
