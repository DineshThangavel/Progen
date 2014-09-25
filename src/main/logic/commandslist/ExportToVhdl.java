/**
 * 
 */
package logic.commandslist;

import java.io.IOException;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import hdl.translator.logic.ElectronicsToVhdlConverter;
import helper.Consts;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ExportToVhdl implements Command{

	@Override
	public String execute(String arguments) throws ProcGenException {

		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		String[] splitArguments = arguments.split("\\s+");
		
		assert(splitArguments.length == 1);
		
		String outputDirectory = splitArguments[0];
		
		Project activeProject = activeAppDetails.getActivePrjectInstance();
		
		ElectronicsToVhdlConverter e = new ElectronicsToVhdlConverter();
		try {
			e.convertProjectToVhdl(activeProject,outputDirectory);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ProcGenException(Consts.ExceptionMessages.UNABLE_TO_WRITE_TO_FILE);
		}
		return Consts.CommandResults.SUCCESS_EXPORT_TO_VHDL;
	}

}
