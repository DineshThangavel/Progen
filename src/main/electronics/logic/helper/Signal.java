package electronics.logic.helper;
/**
 *  This is the enum class for modelling signals
 *  between entities
 */

/**
 * @author DINESH THANGAVEL
 * 
 */
public enum Signal {
	HIGH("1"), LOW("0"), UNDEFINED("Z");

	private String vhdlValue;

	Signal(String vhdlValue) {
		this.vhdlValue = vhdlValue;
	}

	public  static Signal getSignalFromString(String vhdlValue){
		if(vhdlValue.equals("1"))
			return Signal.HIGH;
		
		else if(vhdlValue.equals("0"))
			return Signal.LOW;
		
		else if(vhdlValue.equals("Z"))
			return Signal.UNDEFINED;
		
		// TODO Invalid Signal Exception
		return null;		
	}
	
	public String getVhdlValue() {
		return this.vhdlValue;

	}
}
