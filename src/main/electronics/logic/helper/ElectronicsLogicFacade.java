/**
 * 
 */
package electronics.logic.helper;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class ElectronicsLogicFacade {

	private static ElectronicsLogicFacade instance = null;
	Project baseProject = null;

	private ElectronicsLogicFacade() {

	}

	public static ElectronicsLogicFacade getInstance() {
		if (instance == null) {
			instance = new ElectronicsLogicFacade();
		}
		return instance;

	}

	public void createNewProject(String name) {
		if (baseProject == null) {
			baseProject = new Project(name);

			// TODO If one project is active support opening another project in
			// the future.
		}
		;
	}

	public String closeActiveProject() {
		String projectName = baseProject.name;
		baseProject = null;
		return projectName;
	}

	/*
	 * This returns a project instance for other commands to act and modify
	 */
	public Project getActivePrjectInstance() {
		return this.baseProject;
	}

}
