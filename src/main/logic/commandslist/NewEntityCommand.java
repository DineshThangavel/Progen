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
			
			EntityDetailsRetriever.EntityDetailsFromUser newEntityDetails = EntityDetailsRetriever.retrieveEntityDetails();
			EntityManager activeEntityManager = activeAppDetails.getActivePrjectInstance().getEntityManager();
			String newEntityId = activeEntityManager.addEntity(newEntityDetails);
			return newEntityId;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcGenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
