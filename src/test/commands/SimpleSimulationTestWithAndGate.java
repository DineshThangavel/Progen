package commands;

import java.util.List;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.helper.SignalValueRecord;

public class SimpleSimulationTestWithAndGate {

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
	  public void testNewInputSimulatorWithAndGate() {
			try {
				System.out.println(logicInterface.processInput("new_and_gate"));
				System.out.println(logicInterface.processInput("new_input_simulator simulator1 1 1 input0"));
				System.out.println(logicInterface.processInput("new_input_simulator simulator2 1 1 input1"));
				
				SignalBusObserver outputObserver = new SignalBusObserver(testProject.getEntityManager().getEntityById("1").getOutputByName("output"),testProject.getProjectSimulator()); 
				
				System.out.println(testProject.getEntityManager().getEntityById("1").getOutputByName("output").getValue());
				
				System.out.println(logicInterface.processInput("add_input_stimulus 2 200 'change_sim_input simulator1 1' 200 'change_sim_input simulator2 1'"));
				
				System.out.println(logicInterface.processInput("add_input_stimulus 1 400 'change_sim_input simulator1 0'"));
				
				System.out.println(logicInterface.processInput("add_input_stimulus 1 600 'change_sim_input simulator1 1'"));
				
				System.out.println(logicInterface.processInput("simulate 0 1"));
				
				outputObserver.printSignalLogToConsole();
				
				List<SignalValueRecord> outputSignalVariations = outputObserver.getSignalValueRecords();
				Assert.assertEquals(outputSignalVariations.size(), 3);
				Assert.assertEquals(outputSignalVariations.get(0).getDisplayStringForCurrentValue(), "H");
				Assert.assertEquals(outputSignalVariations.get(1).getDisplayStringForCurrentValue(), "L");
				Assert.assertEquals(outputSignalVariations.get(2).getDisplayStringForCurrentValue(), "H");
				
				System.out.println(testProject.getEntityManager().getEntityById("1").getOutputByName("output").getValue());
				
			} catch (ProcGenException e) {
				Assert.fail();
				e.printStackTrace();
			}
	  }
}
