/**
 * 
 */
package logic;

import helper.Consts.CommandResults;
import helper.ProcGenException;

import java.util.Stack;

import logic.commandslist.UndoCommand;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class CommandManager {

	private Stack<Command> commandStack = new Stack<Command>();

	public String executeCommand(Command cmd, String arguments) throws ProcGenException  {
		String userFeedback = null;

		// check if it is undo command
		if (cmd instanceof UndoCommand) {
			userFeedback = this.undo();
			return userFeedback;
		}

		userFeedback = cmd.execute(arguments);

		if (cmd instanceof UndoableCommand) {
			commandStack.push(cmd);
		}

		return userFeedback;
	}

	public String undo() {
		if (commandStack.empty() == false) {
			UndoableCommand cmd = (UndoableCommand) commandStack.pop();
			return cmd.undo();
		}

		return CommandResults.UNDO_UNAVAILABLE;
	}
}
