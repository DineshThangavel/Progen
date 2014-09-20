package commands;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.simulation.ProjectSimulator;

public class SimpleSimulationTest {
	Project testProject = null;
	LogicFacade logicInterface = new LogicFacade();

	@BeforeMethod
	public void beforeMethod() {
	
		// Create a new project
		try {
			logicInterface.processInput("new_project testProject");
			testProject = ElectronicsLogicFacade.getInstance().getActivePrjectInstance();
		} catch (ProcGenException e) {
			
			Assert.fail();
			e.printStackTrace();
		}
	}
	
	@AfterMethod
	public void afterMethod(){
		try {
			System.out.println(logicInterface.processInput("close_project"));
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

  @Test
  public void testSimpleCircuit() {
	  try {
//		System.out.println(logicInterface.processInput("new_and_gate"));
		ProjectSimulator newPS = testProject.getProjectSimulator(); 
		newPS.runSimulation(1000, 200);
		  
	} catch (ProcGenException e) {
		Assert.fail();
		e.printStackTrace();
	}
  }

}
