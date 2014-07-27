package commands;

import java.util.HashMap;
import java.util.List;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import electronics.logic.helper.Connection;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityConnectionManager;
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
	
	@AfterMethod
	public void afterMethod(){
		try {
			System.out.println(logicInterface.processInput("close_project"));
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
			
			EntityConnectionManager em1 = newlyAddedEntity.getEntityConnectionManager();
			HashMap<String, List<Connection>> connectionDetails = em1.getConnectionForEntity("1");
			Assert.assertEquals(connectionDetails.containsKey("clk"),true);
			Assert.assertEquals(connectionDetails.containsKey("out1"),true);
			
		
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
			
			Entity childEntity = testProject.getEntityManager().getEntityById("1-1");
			Assert.assertEquals(childEntity.getName(),"testChildEntity1");
			Assert.assertEquals(childEntity.getNumberOfInputs(),1);
			Assert.assertEquals(childEntity.getNumberOfOutputs(),1);
			Assert.assertEquals(childEntity.getChildEntityList().size(),0);
			Assert.assertEquals(childEntity.getInputPortList().get(0).getName(),"clk");
			Assert.assertEquals(childEntity.getOutputPortList().get(0).getName(),"out1");
			
			EntityConnectionManager childEntityConnectionManager = childEntity.getEntityConnectionManager();
			HashMap<String, List<Connection>> connectionDetails = childEntityConnectionManager.getConnectionForEntity("1-1");
			Assert.assertEquals(connectionDetails.containsKey("clk"),true);
			Assert.assertEquals(connectionDetails.containsKey("out1"),true);
			
			Entity parentEntity = testProject.getEntityManager().getEntityById("1");
			EntityConnectionManager parentEntityConnectionManager = parentEntity.getEntityConnectionManager();
			HashMap<String, List<Connection>> parentConnectionDetails = parentEntityConnectionManager.getConnectionForEntity("1-1");
			Assert.assertEquals(parentConnectionDetails.containsKey("clk"),true);
			Assert.assertEquals(parentConnectionDetails.containsKey("out1"),true);
	
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}

	}
	
	

}
