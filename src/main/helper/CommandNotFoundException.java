/**
 * 
 */
package helper;

/**
 * @author DINESH THANGAVEL
 *
 */
@SuppressWarnings("serial")
public class CommandNotFoundException extends ProcGenException {
	public CommandNotFoundException(String message) {
		super(message);
	}

	public CommandNotFoundException(String specificErrorcode, String message) {
		super(specificErrorcode, message);
	}
}
