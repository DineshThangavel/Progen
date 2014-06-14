/**
 * 
 */
package logic;

import helper.Consts.CommandResults;
import helper.ProcGenException;

import java.util.Stack;

import electronics.logic.helper.Project;
import logic.commandslist.UndoCommand;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class CommandManager {

	private Stack<Command> commandStack = new Stack<Command>();
	private Stack<Project> projectSnapshotStack = new Stack<Project>();

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
			// TODO : Keep track of project Snapshot. Ensure that project is merely copied 
			//TODO : Implement Copy constructor
		}

		return userFeedback;
	}

	public String undo() {
		if (commandStack.empty() == false) {
			UndoableCommand cmd = (UndoableCommand) commandStack.pop();
			// TODO : Add poping out project state
			return cmd.undo();
		}

		return CommandResults.UNDO_UNAVAILABLE;
	}
}
