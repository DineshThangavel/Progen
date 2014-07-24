package commands;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.Project;

public class NewEntityTest {

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

	public void firstEntityAdditionTest() {
		try {
			int sizeBeforeAddition = testProject.getEntityManager().getBaseEntities().size();
			String feedback = logicInterface.processInput("new_entity testEntity1 1 1 0 clk 1 out1 1");
			System.out.println(feedback);
			int sizeAfterAddition = testProject.getEntityManager().getBaseEntities().size();
			
			Assert.assertEquals(sizeAfterAddition, sizeBeforeAddition + 1);
			Entity newlyAddedEntity = testProject.getEntityManager().getBaseEntities().get(0);
			
			Assert.assertEquals(newlyAddedEntity.getId(), "1");
			Assert.assertEquals(newlyAddedEntity.getName(),"testEntity1");
			Assert.assertEquals(newlyAddedEntity.getNumberOfInputs(),1);
			Assert.assertEquals(newlyAddedEntity.getNumberOfOutputs(),1);
			Assert.assertEquals(newlyAddedEntity.getChildEntityList().size(),0);

			Assert.assertEquals(testProject.getEntityManager().getEntityById("1").getInputPortList().get(0).getName(),"clk");
			Assert.assertEquals(testProject.getEntityManager().getEntityById("1").getOutputPortList().get(0).getName(),"out1");
			
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void childEntityAdditionTest() {
		
		firstEntityAdditionTest();
		
		try {
			int sizeBeforeAddition = testProject.getEntityManager().getBaseEntities().get(0).getChildEntityList().size();
			String feedback;
			feedback = logicInterface.processInput("new_entity testChildEntity1 1 1 1 clk 1 out1 1");
			System.out.println(feedback);
			int sizeAfterAddition = testProject.getEntityManager().getBaseEntities().get(0).getChildEntityList().size();
			
			Assert.assertEquals(sizeAfterAddition, sizeBeforeAddition + 1);
			Assert.assertEquals(testProject.getEntityManager().getEntityById("1-1").getName(),"testChildEntity1");
			Assert.assertEquals(testProject.getEntityManager().getEntityById("1-1").getNumberOfInputs(),1);
			Assert.assertEquals(testProject.getEntityManager().getEntityById("1-1").getNumberOfOutputs(),1);
			Assert.assertEquals(testProject.getEntityManager().getEntityById("1-1").getChildEntityList().size(),0);
			Assert.assertEquals(testProject.getEntityManager().getEntityById("1-1").getInputPortList().get(0).getName(),"clk");
			Assert.assertEquals(testProject.getEntityManager().getEntityById("1-1").getOutputPortList().get(0).getName(),"out1");
			
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}

	}
	
	

}
