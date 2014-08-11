/**
 * 
 */
package logic;

import helper.CommandNotFoundException;
import helper.Consts.CommandInputText;
import helper.Consts.ExceptionMessages;

import java.util.HashMap;

import logic.commandslist.*;


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
		cmdMapper.put(CommandInputText.DISPLAY_ENTITIES, new DisplayAllEntities());
		cmdMapper.put(CommandInputText.IMPORT_VHDL, new ImportVhdlCode());
		cmdMapper.put(CommandInputText.CONNECT, new ConnectCommand());
		cmdMapper.put(CommandInputText.CLOSE_PROJECT, new CloseProjectCommand());
		cmdMapper.put(CommandInputText.NEW_AND_GATE, new NewAndGateCommand());
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
