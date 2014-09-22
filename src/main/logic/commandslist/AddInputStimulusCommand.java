/**
 * 
 */
package logic.commandslist;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.simulation.CircuitStimulus;
import electronics.logic.simulation.ProjectSimulator;
import helper.Consts;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
public class AddInputStimulusCommand implements Command{

	@Override
	public String execute(String arguments) throws ProcGenException {
		
		String[] commandSplit = arguments.split("'");
		String[] splitArguments = commandSplit[0].split("\\s+");
		
		if(splitArguments.length > 1 ){
			try{
				int noOfStimuli = Integer.parseInt(splitArguments[0]);
				if((splitArguments.length + commandSplit.length -1) != (2*noOfStimuli +1))
					return Consts.ExceptionMessages.INPUT_NOT_RECOGNISED;
				
				
				ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
				Project activeProjectInstance = activeAppDetails.getActivePrjectInstance();
				ProjectSimulator prjSim = activeProjectInstance.getProjectSimulator();
				
				
				
				int termCount = 1;
				float timeOfInput;
				String commandToExecute;
				CircuitStimulus newCircuitStimulus;

				// add the first one separately
				timeOfInput = Float.parseFloat(splitArguments[1]);
				commandToExecute = commandSplit[1];
			
				newCircuitStimulus = new CircuitStimulus(timeOfInput,commandToExecute);
				prjSim.addInputStimulus(newCircuitStimulus);
				
				
				for(int stimuliCount = 1;stimuliCount<noOfStimuli;stimuliCount++){
					timeOfInput = Float.parseFloat(commandSplit[++termCount].trim());
					commandToExecute = commandSplit[++termCount];
				
					newCircuitStimulus = new CircuitStimulus(timeOfInput,commandToExecute);
					prjSim.addInputStimulus(newCircuitStimulus);
				}
			}
			catch(NumberFormatException e){
				throw e;
			}
		}
		return Consts.CommandResults.SUCCESS_ADDED_STIMULI_TO_SIMULATOR;
	}

}
