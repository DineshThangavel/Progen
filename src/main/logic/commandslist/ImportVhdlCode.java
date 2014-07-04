/**
 * 
 */
package logic.commandslist;

import java.io.IOException;

import de.upb.hni.vmagic.parser.VhdlParserException;
import electronics.logic.helper.ElectronicsLogicFacade;
import hdl.translator.logic.VhdlToElectronicsConverter;
import helper.Consts;
import helper.ProcGenException;
import logic.UndoableCommand;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class ImportVhdlCode implements UndoableCommand {

	@Override
	public String execute(String arguments) throws ProcGenException {
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade
				.getInstance();
		if (activeAppDetails.getActivePrjectInstance() == null) {
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}

		VhdlToElectronicsConverter hdlConverter = new VhdlToElectronicsConverter();
		String[] argumentsArray = splitArguments(arguments);
		if (argumentsArray.length > 0) {
			hdlConverter.convertToProgen(argumentsArray[1]);
		}

		return Consts.ConsoleUIConstants.SPECIFY_INPUT_FILE;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

	private String[] splitArguments(String argument) {
		return argument.split(" ");
	}

}
