/**
 * 
 */
package logic.commandslist;

import helper.Consts;
import helper.ProcGenException;
import electronics.logic.helper.ElectronicsLogicFacade;
import logic.Command;
import logic.LogicFacade;

/**
 * @author DINESH THANGAVEL
 *
 */
public class NewProjectCommand implements Command {

	@Override
	public String execute(String args) throws ProcGenException {
		ElectronicsLogicFacade eleLogic = ElectronicsLogicFacade.getInstance();
		if(args.length() ==0)
			throw new ProcGenException("Argument cannot be empty for new_project");
		assert(args.length() > 0);
		eleLogic.createNewProject(args);
		return Consts.CommandResults.SUCCESS_NEW_PROJECT_CREATION;
	}
	
}
