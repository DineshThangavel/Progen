/**
 * 
 */
package electronics.logic.helper;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ElectronicsLogicFacade {
	Project baseProject = null;
	
	public void createNewProject(String name){
		if(baseProject == null){
			baseProject = new Project(name);
		
		//TODO Implement Exception 
		};
	}
	
	/*
	 *  This returns a project instance for other commands 
	 *  to act and modify
	 */
	public Project getPrjectInstance(){
		return this.baseProject;
	}	
		
	
}
