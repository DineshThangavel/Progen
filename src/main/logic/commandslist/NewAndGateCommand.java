/**
 * 
 */
package logic.commandslist;

import electronics.logic.entities.AndGate;
import electronics.logic.helper.ElectronicsLogicFacade;
import helper.Consts;
import helper.ProcGenException;
import logic.UndoableCommand;

/**
 * @author DINESH THANGAVEL
 *
 */
public class NewAndGateCommand implements UndoableCommand{

	@Override
	public String execute(String arguments) throws ProcGenException {
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		String[] splitArguments = arguments.split("\\s+");
		String nameOfGate = "";
		if(splitArguments.length > 0){
			nameOfGate = splitArguments[0];
		}
		
		AndGate newAndGate = new AndGate("", nameOfGate);
		String entityId = activeAppDetails.getActivePrjectInstance().getEntityManager().addEntity(newAndGate);
		return Consts.CommandResults.SUCCESS_NEW_AND_CREATION + entityId;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
