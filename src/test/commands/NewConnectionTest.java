package commands;

import java.util.List;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.log4testng.Logger;

import sun.rmi.runtime.Log;
import electronics.logic.helper.Connection;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityConnectionManager;
import electronics.logic.helper.Project;
import electronics.logic.helper.ProjectConnectionManager;

public class NewConnectionTest {

	Project testProject1 = null;
	LogicFacade logicInterface = new LogicFacade();

	@Test
	public void testConnection() {
		String feedback;
		try {
			
			System.out.println(logicInterface.processInput("new_entity entity1 2 1 0 clk 1 input1 1 instruction 1"));
			
			System.out.println(logicInterface.processInput("new_entity entity2 1 1 0 input1 1 output1 1"));
			System.out.println(logicInterface.processInput("new_entity entity21 1 1 2 input11 1 output11 1"));
			System.out.println(logicInterface.processInput("new_entity entity22 1 1 2 input12 1 output12 1"));
			
			
			System.out.println(logicInterface.processInput("connect 1 2 instruction input1 default"));
			ProjectConnectionManager pcm = testProject1.getConnectionManager();
			List<Connection> connectionList = pcm.getConnectionForEntity("1").get("instruction");
			Assert.assertEquals(connectionList.size(), 1);
			Assert.assertEquals(connectionList.get(0).getDestinationEntityId(), "2");
			
			System.out.println(logicInterface.processInput("connect 2 2-1 input1 input11 default"));
			Entity entity2 = testProject1.getEntityManager().getEntityById("2");
			EntityConnectionManager connectionManagerForEntity2 = entity2.getEntityConnectionManager();
			connectionList = connectionManagerForEntity2.getConnectionsForSignal("2", "input1");
			Assert.assertEquals(connectionList.size(), 1);
			Assert.assertEquals(connectionList.get(0).getDestinationEntityId(), "2-1");
			
			System.out.println(logicInterface.processInput("connect 2-1 2-2 output11 input12 default"));
			connectionList = connectionManagerForEntity2.getConnectionsForSignal("2-1", "output11");
			Assert.assertEquals(connectionList.size(), 1);
			Assert.assertEquals(connectionList.get(0).getDestinationEntityId(), "2-2");
			Assert.assertEquals(connectionList.get(0).getOutputSignal().getName(), "input12");
			
			System.out.println(logicInterface.processInput("connect 2-2 2 output12 output1 default"));
			connectionList = connectionManagerForEntity2.getConnectionsForSignal("2-2", "output12");
			Assert.assertEquals(connectionList.size(), 1);
			Assert.assertEquals(connectionList.get(0).getDestinationEntityId(), "2");
			Assert.assertEquals(connectionList.get(0).getOutputSignal().getName(), "output1");
			
		} catch (ProcGenException e) {
			
			System.out.println(e.getMessage());
			e.printStackTrace();
			Assert.fail();
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		// Create a new project
		try {
			logicInterface.processInput("new_project testProject1");
			testProject1 = ElectronicsLogicFacade.getInstance()
					.getActivePrjectInstance();
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

}
