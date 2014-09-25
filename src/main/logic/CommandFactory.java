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
		cmdMapper.put(CommandInputText.NEW_OR_GATE, new NewOrGateCommand());
		cmdMapper.put(CommandInputText.NEW_INPUT_SIMULATOR, new NewInputSimulatorCommand());
		cmdMapper.put(CommandInputText.SIMULATE, new StartSimulation());
		cmdMapper.put(CommandInputText.CHANGE_INPUT_FOR_SIMULATION, new ChangeInputSimulatorValueCommand());
		cmdMapper.put(CommandInputText.ADD_INPUT_STIMULUS, new AddInputStimulusCommand());
		cmdMapper.put(CommandInputText.NEW_REGISTER_COMMAND, new NewRegisterCommand());
		cmdMapper.put(CommandInputText.GET_SIGNALBUS_VALUE, new GetSignalBusValueCommand());
		cmdMapper.put(CommandInputText.CONNECT_TO_CLOCK, new ConnectToClockCommand());
		cmdMapper.put(CommandInputText.EXPORT_TO_VHDL, new ExportToVhdl());
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
