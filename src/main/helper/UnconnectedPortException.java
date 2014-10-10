/**
 * 
 */
package helper;

import java.util.List;

/**
 * @author DINESH THANGAVEL
 *
 */
@SuppressWarnings("serial")
public class UnconnectedPortException  extends ProcGenException{
	
	List<String> unconnectedPortNames;
	public UnconnectedPortException(String message, List<String> unconnectedPortNames){
		super(message);
		this.unconnectedPortNames = unconnectedPortNames;
	}
	
	public List<String> getUnconnectedPortNames(){
		return this.unconnectedPortNames;
	}
}
