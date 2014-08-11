/**
 * 
 */
package logic.commandslist;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.InputSimulator;
import electronics.logic.helper.ProjectSimulator;
import helper.Consts;
import helper.ProcGenException;
import logic.UndoableCommand;

/**
 * @author DINESH THANGAVEL
 *
 */
public class NewInputSimulatorCommand implements UndoableCommand{
	
	@Override
	public String execute(String arguments) throws ProcGenException {
// parentid should be 0 or ""
		String[] splitArgs = arguments.split("\\s+");
		String nameOfInputSimulator = splitArgs[0];
		int widthOfSimulator = 0 ;
		try{
			widthOfSimulator = Integer.parseInt(splitArgs[1]);
		}
				
		catch(NumberFormatException e){
			throw new ProcGenException(Consts.ErrorCodes.INVALID_COMMAND_ARGUMENT,Consts.ExceptionMessages.ERROR_CREATING_INPUT_SIM);
		}

		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		// TODO validate arguments and finish the command
		String destinationEntityId = splitArgs[2];
		String signalName = splitArgs[3];
		InputSimulator newInputSimulator = new InputSimulator(nameOfInputSimulator, widthOfSimulator);
		ProjectSimulator proSim =activeAppDetails.getActivePrjectInstance().getProjectSimulator(); 
		proSim.addInputSimulator(newInputSimulator,destinationEntityId,signalName);
		return null;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
