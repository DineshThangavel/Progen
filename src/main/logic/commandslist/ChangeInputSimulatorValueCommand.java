/**
 * 
 */
package logic.commandslist;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBus;
import electronics.logic.simulation.InputSimulator;
import electronics.logic.simulation.ProjectSimulator;
import helper.Consts;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ChangeInputSimulatorValueCommand implements Command{

	@Override
	public String execute(String arguments) throws ProcGenException {
		
		String[] splitArgs = arguments.split("\\s+");
		String nameOfInputSimulator = splitArgs[0];
		String valueToSetAsString = splitArgs[1];
		
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		Project activeProjectInstance = activeAppDetails.getActivePrjectInstance();
		ProjectSimulator prjSim = activeProjectInstance.getProjectSimulator();
		
		SignalBus signalToModify = null;
		if(nameOfInputSimulator.length() > 0){
			signalToModify = prjSim.getSignalBusConnectedToInputSimulator(nameOfInputSimulator);
			if(signalToModify == null){
				return Consts.CommandResults.INPUT_SIM_NOT_CONNECTED;
			}
		}
		
		SignalBus newTemporarySignalBus = new SignalBus("temp",valueToSetAsString);
		InputSimulator inputSimToChange = prjSim.getInputSimulator(nameOfInputSimulator);
		assert(inputSimToChange != null);

		signalToModify.setValue(newTemporarySignalBus.getValue());
		inputSimToChange.setValue(newTemporarySignalBus.getValue());
		
		return null;
	}

}
