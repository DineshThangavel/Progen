/**
 * 
 */
package helper;

/**
 * @author DINESH THANGAVEL
 *
 */
public class Consts {

	public static class ConsoleUIConstants {
		public static final String PROMPT_ENTER_COMMAND = "Please Enter the Command:";
		public static final String ERROR = "ERROR:";
		public static final String SUCCESS = "SUCCESS:";
		public static final String NEW_ENTITY_HEADER = "New Entity Details:";
		public static final String PROMPT_NEW_ENTITY_NAME = " Please Enter the name of the entity";
		public static final String PROMPT_NEW_ENTITY_PARENT_ID = "Please Enter the parent ID of the entity"; // has to be removed for GUI
		public static final String PROMPT_NEW_ENTITY_NO_OF_INPUT_SIGNALS = "Please enter the number of inputs";
		public static final String PROMPT_NEW_ENTITY_NO_OF_OUTPUT_SIGNALS = "Please enter the number of outputs";
		public static final String PROMPT_NEW_ENTITY_INPUT_SIGNAL_NAME = "Please enter input port name";
		public static final String PROMPT_NEW_ENTITY_OUTPUT_SIGNAL_NAME = "Please enter output port name";
		public static final String PRINT_NOT_A_NUMBER = "It is not a number";
		public static final String PROMPT_NEW_INPUT_SIGNAL_BUS_WIDTH = "Enter the bus width size for input signal";
		public static final String PROMPT_NEW_OUTPUT_SIGNAL_BUS_WIDTH = "Enter the bus width size for output signal";
		public static final String SEPARATOR = "--------------------------------------------------------------------";
		public static final String ENTITY_NAME = "Entity Name:";
		public static final String ENTITY_ID = "Entity Id:";
		public static final String PARENT_ID = "Parent Id:";
		public static final String INPUT_PORTS = "Input Ports:";
		public static final String OUTPUT_PORTS = "Output Ports:";
		public static final String NO_ENTITY_IN_PROJECT = "No Entity present in the project";
		public static final String SPECIFY_INPUT_FILE = "Please specify file name";
	}
	
	public static class CommandInputText{
		public static final String UNDO = "undo";
		public static final String NEW_ENTITY = "new_entity";
		public static final String NEW_PROJECT = "new_project";
		public static final String DISPLAY_ENTITIES = "display_entities";
		public static final String IMPORT_VHDL = "import_vhdl";
	}
	
	public static class CommandResults{
		public static final String UNDO_UNAVAILABLE = "UNDO Unavailable";
		public static final String SUCCESS_NEW_PROJECT_CREATION = "Successfully created new project";
		public static final String CREATE_PROJECT_BEFORE_ENTITY = "Please create project before creating entity";
		public static final String SUCCESS_NEW_ENTITY_CREATION = "Successfully created new entity with Id ";
		public static final String COMMAND_EXECUTED_SUCCESSFULLY = "Command Executed Sucessfully";
	}
	
	public static class ExceptionMessages{
		public static final String STARTING_ENTITY_NOT_NULL = "Triggering Entity of simulation cannot be null";
		public static final String COMMAND_NOT_FOUND = "The command could not be found";
		public static final String UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT = "The signal lengths do not match for assignment";
		public static final String INPUT_ALREADY_PRESENT = "Input with same name already present";
		public static final String OUTPUT_ALREADY_PRESENT = "Output with same name already present";
		public static final String SIGNAL_NOT_RECOGNISED = "Unable to recognise type of signal";
		public static final String CANNOT_CONNECT_EXCEPTION = "Unable to make a connection";
		public static final String CONNECTION_CREATION_ERROR= "New Entity is already present";
	}
	
	public static class ErrorCodes{
		public static final String SIGNAL_NOT_RECOGNISED = "100";
		public static final String UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT = "101";
		public static final String SIGNAL_INDEX_NOT_FOUND = "102";
		public static final String ENTITY_ALREADY_PRESENT = "103";
		
	}
}
