/**
 * 
 */
package logic.commandslist;

import electronics.logic.helper.ElectronicsLogicFacade;
import helper.Consts.CommandResults;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
public class CloseProjectCommand implements Command {

	@Override
	public String execute(String arguments) throws ProcGenException {
		// TODO ask for save etc
		ElectronicsLogicFacade eleLogic = ElectronicsLogicFacade.getInstance();
		String projectName = eleLogic.closeActiveProject();
		return CommandResults.SUCCESS_CLOSED_PROJECT + projectName;
	}

}
