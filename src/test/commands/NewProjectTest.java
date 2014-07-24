package commands;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import electronics.logic.helper.ElectronicsLogicFacade;

public class NewProjectTest {
  @Test
  public void createNewProject() {
	  LogicFacade logicInterface = new LogicFacade();
	  try {
		  Assert.assertEquals(ElectronicsLogicFacade.getInstance().getActivePrjectInstance(),null);
		  
		  String feedbackMsg = logicInterface.processInput("new_project testProject");
		  Assert.assertNotNull(ElectronicsLogicFacade.getInstance().getActivePrjectInstance());

	} catch (ProcGenException e) {
		Assert.fail();
		e.printStackTrace();
	}
  }

}
