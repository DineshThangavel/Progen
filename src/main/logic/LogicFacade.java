/**
 * 
 */
package logic;

import helper.ProcGenException;

/**
 * @author DINESH THANGAVEL
 *
 */
public class LogicFacade {
	CommandFactory cmdGenerator = new CommandFactory();
	CommandManager cmdManager = new CommandManager();
	
	public String processInput(String userInput) throws ProcGenException{
		
		String userInputParts[] = userInput.trim().split("\\s+");
		String commandAndArguments[] = userInput.split(userInputParts[0]);
		String arguments = "";
		if(commandAndArguments.length > 1){
			arguments = commandAndArguments[1];
		}
		
		Command cmdToExecute = cmdGenerator.makeCommand(userInputParts[0].toLowerCase());
		String feedbackToUI = cmdManager.executeCommand(cmdToExecute,arguments);
		return feedbackToUI;
	}
	
}
