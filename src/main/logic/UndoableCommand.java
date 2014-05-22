/**
 * 
 */
package logic;

/**
 * @author DINESH THANGAVEL
 *
 */
public interface UndoableCommand extends Command{
	public String undo();
}
