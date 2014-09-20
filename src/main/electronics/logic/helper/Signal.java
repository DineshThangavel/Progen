package electronics.logic.helper;

import helper.Consts;
/**
 *  This is the enum class for modelling signals
 *  between entities
 */
import helper.InvalidSignalException;

/**
 * @author DINESH THANGAVEL
 * 
 */
public enum Signal {
	HIGH("1"), LOW("0"), UNDEFINED("Z");

	private String vhdlRepresentation;

	Signal(String vhdlValue) {
		this.vhdlRepresentation = vhdlValue;
	}

	public static Signal getSignalFromString(String vhdlValue)
			throws InvalidSignalException {
		if (vhdlValue.equals("1"))
			return Signal.HIGH;

		else if (vhdlValue.equals("0"))
			return Signal.LOW;

		else if (vhdlValue.equals("Z"))
			return Signal.UNDEFINED;

		throw new InvalidSignalException(
				Consts.ExceptionMessages.SIGNAL_NOT_RECOGNISED);
	}

}
