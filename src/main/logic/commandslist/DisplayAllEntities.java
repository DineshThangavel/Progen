/**
 * 
 */
package logic.commandslist;

import java.util.List;

import userinterface.ConsoleUI;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBus;
import helper.Consts;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
public class DisplayAllEntities  implements  Command{

	@Override
	public String execute(String arguments) throws ProcGenException {
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		Project activeProject = activeAppDetails.getActivePrjectInstance();
		List<Entity> baseEntityList = activeProject.getEntityManager().getBaseEntities();
		
		if(baseEntityList.size()== 0){
			ConsoleUI.printMessage(Consts.ConsoleUIConstants.NO_ENTITY_IN_PROJECT);
		}
		
		else{
			for(Entity e:baseEntityList){
				this.displayCompleteEntityDetail(e);
				for(Entity childEntity:e.getChildEntityList()){
					this.displayCompleteEntityDetail(childEntity);
				}
			}
		}

		return Consts.CommandResults.COMMAND_EXECUTED_SUCCESSFULLY;
	}
	
	private void displayCompleteEntityDetail(Entity entity){
		ConsoleUI.printMessage(Consts.ConsoleUIConstants.ENTITY_NAME + entity.getName());
		ConsoleUI.printMessage(Consts.ConsoleUIConstants.ENTITY_ID + entity.getId());
		ConsoleUI.printMessage(Consts.ConsoleUIConstants.PARENT_ID + entity.getParentId());
		ConsoleUI.printMessage(Consts.ConsoleUIConstants.INPUT_PORTS);
		for(SignalBus s: entity.getInputPortList()){
			ConsoleUI.printMessage(s.getName()+ " " + s.getBusWidth());
		}

		ConsoleUI.printMessage(Consts.ConsoleUIConstants.OUTPUT_PORTS);
		for(SignalBus s: entity.getOutputPortList()){
			ConsoleUI.printMessage(s.getName() + " " +  s.getBusWidth());
		}
		
		ConsoleUI.printMessage(Consts.ConsoleUIConstants.SEPARATOR);
	}

}
