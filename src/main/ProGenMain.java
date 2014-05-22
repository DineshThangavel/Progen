import helper.CommandNotFoundException;
import helper.Consts;
import helper.ProcGenException;

import java.io.IOException;

import logic.LogicFacade;
import userinterface.ConsoleUI;

public class ProGenMain {

	public static void main(String[] args) {

		ConsoleUI ui = new ConsoleUI();
		LogicFacade logicInterface = new LogicFacade();
		while (true) {
			ui.printEnterCommand();
			String input = null;
			
			try {
				input = ui.readConsoleInput();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if(input.length() > 0){
					String feedbackMsg = logicInterface.processInput(input);
					ui.printMessage(feedbackMsg);
				}
			} catch (CommandNotFoundException e) {
				ui.printMessage(Consts.ConsoleUIConstants.ERROR + e.getMessage());
			} catch (ProcGenException e) {
				ui.printMessage(e.getMessage());
			}
		}
	}

}
