package commands;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;

public class NewConnectionTest {

	Project testProject = null;
	LogicFacade logicInterface = new LogicFacade();

	@Test
	public void testConnection() {
		String feedback;
		try {
			System.out.println(logicInterface.processInput("new_entity entity1 2 1 0 clk 1 input1 1 instruction 1"));
			
			System.out.println(logicInterface.processInput("new_entity entity2 1 1 0 input1 1 output1 1"));
			System.out.println(logicInterface.processInput("new_entity entity21 1 1 2 input11 1 output11 1"));
			System.out.println(logicInterface.processInput("new_entity entity22 1 1 2 input12 1 output12 1"));
			System.out.println(logicInterface.processInput("new_entity entity23 1 1 2 input13 1 output13 1"));
			
			
			System.out.println(logicInterface.processInput("connect 1 2 instruction input1 default"));
			//System.out.println(logicInterface.processInput("connect 2 2-1 input1 input11 default"));
			
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		// Create a new project
		try {
			logicInterface.processInput("new_project testProject");
			testProject = ElectronicsLogicFacade.getInstance()
					.getActivePrjectInstance();
		} catch (ProcGenException e) {

			Assert.fail();
			e.printStackTrace();
		}
	}

}
