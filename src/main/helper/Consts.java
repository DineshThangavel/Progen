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
		public static int NEW_ENTITY_ARGS_COUNT = 6;
	}
	
	public static class CommandInputText{
		public static final String UNDO = "undo";
		public static final String NEW_ENTITY = "new_entity";
		public static final String NEW_PROJECT = "new_project";
		public static final String DISPLAY_ENTITIES = "display_entities";
		public static final String IMPORT_VHDL = "import_vhdl";
		public static final String CONNECT = "connect";
		public static final String CLOSE_PROJECT = "close_project";
		public static final String NEW_AND_GATE = "new_and_gate";
		public static final String SIMULATE = "simulate";
		public static final String NEW_INPUT_SIMULATOR = "new_input_simulator";
		public static final String CHANGE_INPUT_FOR_SIMULATION = "change_sim_input";
		public static final String ADD_INPUT_STIMULUS = "add_input_stimulus";
		public static final String NEW_OR_GATE = "new_or_gate";
		public static final String NEW_REGISTER_COMMAND = "new_register";
		public static final String GET_SIGNALBUS_VALUE = "get_signalbus_value";
		public static final String CONNECT_TO_CLOCK = "connect_to_clock";
		public static final String EXPORT_TO_VHDL = "export_to_vhdl";
		public static final String NEW_MUX = "new_mux";
	}
	
	public static class CommandResults{
		public static final String UNDO_UNAVAILABLE = "UNDO Unavailable";
		public static final String SUCCESS_NEW_PROJECT_CREATION = "Successfully created new project";
		public static final String CREATE_PROJECT_BEFORE_ENTITY = "Please create project before creating entity";
		public static final String SUCCESS_NEW_ENTITY_CREATION = "Successfully created new entity with Id ";
		public static final String COMMAND_EXECUTED_SUCCESSFULLY = "Command Executed Sucessfully";
		public static final String SUCCESS_NEW_CONNECTION_CREATION = "Successfully created new Connection between ";
		public static final String SUCCESS_CLOSED_PROJECT = "Successfully closed the project ";
		public static final String SUCCESS_NEW_AND_CREATION = "Successfully created new AND gate with Id ";
		public static final String SUCCESS_SIMULATION_COMPLETED = "Succesfully simulated project";
		public static final String SUCCESS_NEW_INPUT_SIMULATOR = "Successfully created new input Simulator";
		public static final String INPUT_SIM_NOT_CONNECTED = "Please check output connection of input simulator ";
		public static final String ERROR_SIM_NOT_SUCCESSFUL = "Error occured while simulating";
		public static final String SUCCESS_ADDED_STIMULI_TO_SIMULATOR = "Successfully added stimuli to simulator";
		public static final String SUCCESS_NEW_OR_CREATION = "Successfully created new OR gate with Id ";
		public static final String SUCCESS_CHANGED_SIMULATOR_VALUE = "Successfully changed simulator value";
		public static final String CLOCK = "Clock-";
		public static final String SUCCESS_NEW_REGISTER_CREATION = "Successfully created new Register with Id ";
		public static final String SUCCESS_EXPORT_TO_VHDL = "Successfully exportd to VHDL";
		public static final String SUCCESS_NEW_MUX_CREATION = "Successfully created a new multiplexer";
	}
	
	public static class ExceptionMessages{
		public static final String COMMAND_NOT_FOUND = "The command could not be found";
		public static final String UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT = "The signal lengths do not match for assignment";
		public static final String INPUT_ALREADY_PRESENT = "Input with same name already present";
		public static final String OUTPUT_ALREADY_PRESENT = "Output with same name already present";
		public static final String SIGNAL_NOT_RECOGNISED = "Unable to recognise type of signal";
		public static final String CANNOT_CONNECT_EXCEPTION = "Unable to make a connection";
		public static final String CONNECTION_CREATION_ERROR= "New Entity is already present";
		public static final String ERROR_CREATING_ENTITY = "Unable to create entity";
		public static final String ERROR_CREATING_CONNECTION = "Unable to create connection";
		public static final String ERROR_CREATING_INPUT_SIM = "Unable to create input simulator";
		public static final String ENTITY_NOT_FOUND ="Entity with given Id could not be found";
		public static final String INPUT_NOT_RECOGNISED = "The Input could not be recognised";
		public static final String INCORRECT_TRIGGER_TYPE = "The trigger type could not be recognised";
		public static final String UNABLE_TO_WRITE_TO_FILE = "Unable to write to file ";
	}
	
	public static class ErrorCodes{
		public static final String SIGNAL_NOT_RECOGNISED = "100";
		public static final String UNEQUAL_LENGTH_SIGNAL_ASSIGNMENT = "101";
		public static final String SIGNAL_INDEX_NOT_FOUND = "102";
		public static final String ENTITY_ALREADY_PRESENT = "103";
		public static final String INVALID_COMMAND_ARGUMENT = "104";
		
	}
}
