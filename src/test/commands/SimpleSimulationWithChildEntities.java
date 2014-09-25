package commands;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBusObserver;

public class SimpleSimulationWithChildEntities {

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
  public void testSimulationWithChildEntities() {
		
		try {
			System.out.println(logicInterface.processInput("new_entity entity1 3 1 0 input0 1 input1 1 input2 1 output 1"));
			
			System.out.println(logicInterface.processInput("new_or_gate orGateTest 2 1"));
			System.out.println(logicInterface.processInput("new_and_gate andGateTest 2 1"));
			
			SignalBusObserver outputObserverForbaseEntity = new SignalBusObserver(testProject.getEntityManager().getEntityById("1").getOutputByName("output"),testProject.getProjectSimulator(),"1-output");
			SignalBusObserver outputObserverForOrGate = new SignalBusObserver(testProject.getEntityManager().getEntityById("1-1").getOutputByName("output"),testProject.getProjectSimulator(),"or-output");
			SignalBusObserver outputObserverForAndGate = new SignalBusObserver(testProject.getEntityManager().getEntityById("1-2").getOutputByName("output"),testProject.getProjectSimulator(),"and-output");
			
			System.out.println(logicInterface.processInput("new_input_simulator simulator1 1 1 input0"));
			System.out.println(logicInterface.processInput("new_input_simulator simulator2 1 1 input1"));
			System.out.println(logicInterface.processInput("new_input_simulator simulator3 1 1 input2"));
			
			System.out.println(logicInterface.processInput("connect 1 1-1 input0 input0 default"));
			System.out.println(logicInterface.processInput("connect 1 1-1 input1 input1 default"));
			
			System.out.println(logicInterface.processInput("change_sim_input simulator1 1"));
			
			System.out.println(logicInterface.processInput("connect 1-1 1-2 output input0 default"));
			System.out.println(logicInterface.processInput("connect 1 1-2 input2 input1 default"));
			System.out.println(logicInterface.processInput("connect 1-2 1 output output default"));
			
			System.out.println(logicInterface.processInput("change_sim_input simulator3 1"));

			
			System.out.println("Signal log for base-entity output");
			outputObserverForbaseEntity.printSignalLogToConsole();
		
			System.out.println("Signal log for or-gate output");
			outputObserverForOrGate.printSignalLogToConsole();
			
			System.out.println("Signal log for and-gate output");
			outputObserverForAndGate.printSignalLogToConsole();
			
		} catch (ProcGenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
}
