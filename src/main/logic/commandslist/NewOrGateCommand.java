/**
 * 
 */
package logic.commandslist;

import helper.Consts;
import helper.ProcGenException;
import logic.UndoableCommand;
import electronics.logic.entities.OrGate;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;

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
		
		// TODO : Clean the arguments validation and assignment
		
		String[] splitArguments = arguments.split("\\s+");
		String nameOfGate = "Or";
		String parentId = ""; 
		int noOfInputs = 2;
		if(splitArguments[0].length() > 0){
			nameOfGate = splitArguments[0];
		}	
		
		if(splitArguments.length > 1){
			noOfInputs = Integer.parseInt(splitArguments[1]);
		}

		if(splitArguments.length > 2){
			parentId = splitArguments[2];
		}
		
		
		OrGate newOrGate = new OrGate("", nameOfGate,noOfInputs);
		if(parentId.length()>0){
			EntityManager entityManager = activeAppDetails.getActivePrjectInstance().getEntityManager();
			Entity parentEntity = entityManager.getEntityById(parentId);
			if(parentEntity != null)
				newOrGate.setParent(parentEntity);
			else{
				throw new ProcGenException(Consts.ExceptionMessages.ENTITY_NOT_FOUND);
			}
		}
		String entityId = activeAppDetails.getActivePrjectInstance().getEntityManager().addEntity(newOrGate);
		return Consts.CommandResults.SUCCESS_NEW_OR_CREATION + entityId;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
