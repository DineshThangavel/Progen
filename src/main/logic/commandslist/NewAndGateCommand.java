/**
 * 
 */
package logic.commandslist;

import electronics.logic.entities.AndGate;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;
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
		
		// TODO: clean the arguments validation
		String[] splitArguments = arguments.split("\\s+");
		String nameOfGate = "And";
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
		
		AndGate newAndGate = new AndGate("", nameOfGate,noOfInputs);
		if(parentId.length()>0){
			EntityManager entityManager = activeAppDetails.getActivePrjectInstance().getEntityManager();
			Entity parentEntity = entityManager.getEntityById(parentId);
			if(parentEntity != null)
				newAndGate.setParent(parentEntity);
			else{
				throw new ProcGenException(Consts.ExceptionMessages.ENTITY_NOT_FOUND);
			}
		}
		String entityId = activeAppDetails.getActivePrjectInstance().getEntityManager().addEntity(newAndGate);

		return Consts.CommandResults.SUCCESS_NEW_AND_CREATION + entityId;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
