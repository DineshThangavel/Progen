/**
 * 
 */
package userinterface;

import helper.Consts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author DINESH THANGAVEL
 * 
 */

/*
 * The purpose of this class is to retrieve the entity details from the user and
 * send it to the project.
 */
public class EntityDetailsRetriever {

	// This inner class will be used as a data transfer object and hence all the
	// attributes are public
	public static class EntityDetailsFromUser {
		// TODO: validate these parameters
		public String nameOfEntity;
		public String parentOfEntity;
		public int noOfInputSignals;
		public int noOfOutputSignals;
		public HashMap<String, Integer> inputSignalNames = new HashMap<String, Integer>();
		public HashMap<String, Integer> outputSignalNames = new HashMap<String, Integer>();

	}

	static public EntityDetailsFromUser retrieveEntityDetails()
			throws IOException {
		EntityDetailsFromUser newEntityDetails = new EntityDetailsRetriever.EntityDetailsFromUser();
		ConsoleUI.printMessage(Consts.ConsoleUIConstants.NEW_ENTITY_HEADER);

		// TODO Current Assumption only word is entered and inputs are valid. So
		// validate them

		getEntityNameFromUser(newEntityDetails);

		getEntityParentFromUser(newEntityDetails);

		getNoOfInputSignals(newEntityDetails);

		getNoOfOutputSignals(newEntityDetails);

		getInputOutputSignalDetails(newEntityDetails);

		return newEntityDetails;
	}

	private static void getInputOutputSignalDetails(
			EntityDetailsFromUser newEntityDetails) throws IOException {
		for (int i = 0; i < newEntityDetails.noOfInputSignals; i++) {
			ConsoleUI
					.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_ENTITY_INPUT_SIGNAL_NAME);
			String inputSignalName = null;
			while (true) {
				inputSignalName = ConsoleUI.readConsoleInput();
				if (inputSignalName.length() > 0) {
					break;
					// TODO Check if name is already present
				}
			}
			int inputBusWidth = getInputSignalBusWidth(newEntityDetails);

			newEntityDetails.inputSignalNames.put(inputSignalName,inputBusWidth);
		}

		for (int i = 0; i < newEntityDetails.noOfOutputSignals; i++) {
			ConsoleUI
					.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_ENTITY_OUTPUT_SIGNAL_NAME);
			
			String outputSignalName;
			
			while (true) {
				outputSignalName = ConsoleUI.readConsoleInput();
				if (outputSignalName.length() > 0)
					break;
				//TODO: check if name is already present
			}
			int outputBusWidth = getOutputSignalBusWidth(newEntityDetails);

			newEntityDetails.outputSignalNames.put(outputSignalName,
					outputBusWidth);
		}
	}

	private static int getInputSignalBusWidth(
			EntityDetailsFromUser newEntityDetails) throws IOException {

		ConsoleUI
				.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_INPUT_SIGNAL_BUS_WIDTH);

		String busWidthAsString = ConsoleUI.readConsoleInput();
		try {
			int busWidth = Integer.valueOf(busWidthAsString);
			return busWidth;

		} catch (NumberFormatException e) {
			ConsoleUI
					.printMessage(Consts.ConsoleUIConstants.PRINT_NOT_A_NUMBER);
			return getInputSignalBusWidth(newEntityDetails);
		}
	}

	private static int getOutputSignalBusWidth(
			EntityDetailsFromUser newEntityDetails) throws IOException {

		ConsoleUI
				.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_OUTPUT_SIGNAL_BUS_WIDTH);

		String busWidthAsString = ConsoleUI.readConsoleInput();
		try {
			int busWidth = Integer.valueOf(busWidthAsString);
			return busWidth;

		} catch (NumberFormatException e) {
			ConsoleUI
					.printMessage(Consts.ConsoleUIConstants.PRINT_NOT_A_NUMBER);
			return getOutputSignalBusWidth(newEntityDetails);
		}
	}

	private static void getNoOfOutputSignals(
			EntityDetailsFromUser newEntityDetails) throws IOException {
		ConsoleUI
				.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_ENTITY_NO_OF_OUTPUT_SIGNALS);

		try {
			newEntityDetails.noOfOutputSignals = Integer.valueOf(ConsoleUI
					.readConsoleInput());
		} catch (NumberFormatException e) {

			ConsoleUI
					.printMessage(Consts.ConsoleUIConstants.PRINT_NOT_A_NUMBER);

			getNoOfOutputSignals(newEntityDetails);
		}
	}

	private static void getNoOfInputSignals(
			EntityDetailsFromUser newEntityDetails) throws IOException {
		ConsoleUI
				.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_ENTITY_NO_OF_INPUT_SIGNALS);

		try {
			newEntityDetails.noOfInputSignals = Integer.valueOf(ConsoleUI
					.readConsoleInput());

		} catch (NumberFormatException e) {

			ConsoleUI
					.printMessage(Consts.ConsoleUIConstants.PRINT_NOT_A_NUMBER);
			getNoOfInputSignals(newEntityDetails);
		}
	}

	private static void getEntityParentFromUser(
			EntityDetailsFromUser newEntityDetails) throws IOException {
		ConsoleUI
				.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_ENTITY_PARENT_ID);

		newEntityDetails.parentOfEntity = ConsoleUI.readConsoleInput();
	}

	private static void getEntityNameFromUser(
			EntityDetailsFromUser newEntityDetails) throws IOException {
		ConsoleUI
				.printMessage(Consts.ConsoleUIConstants.PROMPT_NEW_ENTITY_NAME);

		newEntityDetails.nameOfEntity = ConsoleUI.readConsoleInput();
	}
}
