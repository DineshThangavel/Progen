/**
 * 
 */
package logic;

import java.util.HashMap;

import logic.commandslist.*;
import helper.CommandNotFoundException;
import helper.Consts;
import helper.Consts.*;
import helper.Consts.CommandInputText;
import helper.Consts.ExceptionMessages;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class CommandFactory {

	private HashMap<String, Command> cmdMapper = new HashMap<String, Command>();

	CommandFactory() {
		// Register the commands available

		cmdMapper.put(CommandInputText.UNDO, new UndoCommand());
		cmdMapper.put(CommandInputText.NEW_PROJECT, new NewProjectCommand());
		cmdMapper.put(CommandInputText.NEW_ENTITY, new NewEntityCommand());
	}

	public Command makeCommand(String userInputText)
			throws CommandNotFoundException {

		assert (userInputText.length() > 0);

		if (cmdMapper.containsKey(userInputText)) {
			return cmdMapper.get(userInputText);
		}

		// if not command found throw exception
		throw new CommandNotFoundException(ExceptionMessages.COMMAND_NOT_FOUND);
	}
}
