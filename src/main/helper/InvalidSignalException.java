/**
 * 
 */
package helper;

/**
 * @author DINESH THANGAVEL
 *
 */
@SuppressWarnings("serial")
public class InvalidSignalException extends ProcGenException{
	public InvalidSignalException(String message){
		super(message);
	}

	public InvalidSignalException(String specificErrorcode, String message){
		super(specificErrorcode, message);
	}
}
