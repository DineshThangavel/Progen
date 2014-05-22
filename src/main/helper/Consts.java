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
	}
	
	public static class CommandInputText{
		public static final String UNDO = "undo";
		public static final String NEW_ENTITY = "new_entity";
		public static final String NEW_PROJECT = "new_project";
		
	}
	
	public static class CommandResults{
		public static final String UNDO_UNAVAILABLE = "UNDO Unavailable";
		public static final String SUCCESS_NEW_PROJECT_CREATION = "Successfully created new project";
	}
	
	public static class ExceptionMessages{
		public static final String STARTING_ENTITY_NOT_NULL = "Triggering Entity of simulation cannot be null";
		public static final String COMMAND_NOT_FOUND = "The command could not be found";
	}
}
