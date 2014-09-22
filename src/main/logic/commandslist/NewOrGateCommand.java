/**
 * 
 */
package logic.commandslist;

import helper.Consts;
import helper.ProcGenException;
import logic.UndoableCommand;
import electronics.logic.entities.OrGate;
import electronics.logic.helper.ElectronicsLogicFacade;

/**
 * @author DINESH THANGAVEL
 *
 */
public class NewOrGateCommand implements UndoableCommand {

	@Override
	public String execute(String arguments) throws ProcGenException {
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		String[] splitArguments = arguments.split("\\s+");
		String nameOfGate = "";
		int noOfInputs = 2;
		if(splitArguments.length > 0){
			nameOfGate = splitArguments[0];
		}	
		
		if(splitArguments.length > 1){
			noOfInputs = Integer.parseInt(splitArguments[1]);
		}
		
		OrGate newOrGate = new OrGate("", nameOfGate,noOfInputs);
		String entityId = activeAppDetails.getActivePrjectInstance().getEntityManager().addEntity(newOrGate);
		return Consts.CommandResults.SUCCESS_NEW_OR_CREATION + entityId;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
