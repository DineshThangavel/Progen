/**
 * 
 */
package helper;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author DINESH THANGAVEL
 *
 */

@SuppressWarnings("serial")
public class ProcGenException extends Exception{
	public String errorCode;
	
	public ProcGenException() {
		super();
	}

	public ProcGenException(String message) {
		super(message);
	}

	public ProcGenException(String errorcode,	String message) {
		super(message);
		errorCode = errorcode;
	}

	public static String toStringWithStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return "\n" + sw.toString();
	}	
}
