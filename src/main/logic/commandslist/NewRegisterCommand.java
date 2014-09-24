/**
 * 
 */
package logic.commandslist;

import electronics.logic.entities.AndGate;
import electronics.logic.entities.Register;
import electronics.logic.helper.ElectronicsLogicFacade;
import helper.Consts;
import helper.ProcGenException;
import logic.UndoableCommand;

/**
 * @author DINESH THANGAVEL
 *
 */
public class NewRegisterCommand implements UndoableCommand {

	@Override
	public String execute(String arguments) throws ProcGenException {
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		String[] splitArguments = arguments.split("\\s+");
		String nameOfGate = "FlipFlop";
		int widthOfInput = 1;
		
		assert(splitArguments.length == 2);

		if(splitArguments.length > 0){
			widthOfInput = Integer.parseInt(splitArguments[0]);
			// TODO: catch number format exception
		}
		
		String triggerType = splitArguments[1];
		
		Register newRegister = new Register("",widthOfInput,triggerType);
		String entityId = activeAppDetails.getActivePrjectInstance().getEntityManager().addEntity(newRegister);
		return Consts.CommandResults.SUCCESS_NEW_REGISTER_CREATION + entityId;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
