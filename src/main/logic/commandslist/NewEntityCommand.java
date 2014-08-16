/**
 * Creates an electronic Entity
 * 
 */
package logic.commandslist;

import helper.Consts;
import helper.ProcGenException;

import java.io.IOException;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.EntityManager;
import userinterface.EntityDetailsRetriever;
import userinterface.EntityDetailsRetriever.EntityDetailsFromUser;
import logic.UndoableCommand;

/**
 * @author DINESH THANGAVEL
 *	
 */
public class NewEntityCommand implements UndoableCommand{

	@Override
	public String execute(String args) {
		try {
			ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
			if(activeAppDetails.getActivePrjectInstance()==null){
				return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
			}
			
			EntityDetailsRetriever.EntityDetailsFromUser newEntityDetails = null;
			if(args.length() > 4){
				try{
					newEntityDetails = parseEntityArguments(args);
				}
				catch(NumberFormatException e){
					throw new ProcGenException(Consts.ErrorCodes.INVALID_COMMAND_ARGUMENT,Consts.ExceptionMessages.ERROR_CREATING_ENTITY);
				}
			}
			
			else{
				newEntityDetails = EntityDetailsRetriever.retrieveEntityDetails();
			}
			EntityManager activeEntityManager = activeAppDetails.getActivePrjectInstance().getEntityManager();
			String newEntityId = activeEntityManager.addEntity(newEntityDetails);
			return Consts.CommandResults.SUCCESS_NEW_ENTITY_CREATION + newEntityId;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcGenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private EntityDetailsFromUser parseEntityArguments(String args) throws ProcGenException {
		EntityDetailsFromUser newEntityDetails = new EntityDetailsFromUser();
		String[] splitArguments = args.split("\\s+");
		try{
		newEntityDetails.nameOfEntity = splitArguments[0];
		newEntityDetails.noOfInputSignals = Integer.parseInt(splitArguments[1]);
		newEntityDetails.noOfOutputSignals = Integer.parseInt(splitArguments[2]);
		newEntityDetails.parentOfEntityId = splitArguments[3];
		
		if(splitArguments.length !=( 4 + (newEntityDetails.noOfInputSignals + newEntityDetails.noOfOutputSignals)*2))
			throw new ProcGenException(Consts.ErrorCodes.INVALID_COMMAND_ARGUMENT,Consts.ExceptionMessages.ERROR_CREATING_ENTITY);
		
		int argumentCount = 4;
		for(int i = 0; i< newEntityDetails.noOfInputSignals;i++){
			newEntityDetails.inputSignalNames.put(splitArguments[argumentCount], Integer.parseInt(splitArguments[argumentCount+1]));
			argumentCount = argumentCount + 2;
		}
		
		for(int i = 0; i< newEntityDetails.noOfOutputSignals;i++){
			newEntityDetails.outputSignalNames.put(splitArguments[argumentCount], Integer.parseInt(splitArguments[argumentCount+1]));
			argumentCount = argumentCount + 2; ;
		}
		
		}
		catch(NumberFormatException e){
			throw e;
		}
		return newEntityDetails;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
	