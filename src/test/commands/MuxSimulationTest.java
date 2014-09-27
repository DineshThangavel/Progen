package commands;

import java.util.List;

import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.helper.SignalValueRecord;

public class MuxSimulationTest {
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
		  public void testMuxSimulation() {
			  try {
				 // name,selectSignalWidth, inputSignalWidth
				System.out.println(logicInterface.processInput("new_mux mux1 1 1 0 0 input0 1 input1"));

				SignalBusObserver outputObserver = new SignalBusObserver(testProject.getEntityManager().getEntityById("1").getOutputByName("output"),testProject.getProjectSimulator(),"1-output"); 
				
				System.out.println(logicInterface.processInput("new_input_simulator simulator1 1 1 input0"));
				System.out.println(logicInterface.processInput("new_input_simulator simulator2 1 1 input1"));
				System.out.println(logicInterface.processInput("new_input_simulator simulator3 1 1 selectionInput"));
				
				System.out.println(logicInterface.processInput("add_input_stimulus 1 400 'change_sim_input simulator2 1'"));
				System.out.println(logicInterface.processInput("add_input_stimulus 1 600 'change_sim_input simulator3 1'"));
				System.out.println(logicInterface.processInput("add_input_stimulus 1 800 'change_sim_input simulator3 0'"));

				System.out.println(logicInterface.processInput("simulate 0 1"));
				System.out.println("Printing mux output values:");
				outputObserver.printSignalLogToConsole();
				
				List<SignalValueRecord> outputSignalVariations = outputObserver.getSignalValueRecords();
				Assert.assertEquals(outputSignalVariations.size(), 3);
				Assert.assertEquals(outputSignalVariations.get(0).getDisplayStringForCurrentValue(), "L");
				Assert.assertEquals(outputSignalVariations.get(1).getDisplayStringForCurrentValue(), "H");
				Assert.assertEquals(outputSignalVariations.get(2).getDisplayStringForCurrentValue(), "L");
				
			  } catch (ProcGenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Assert.fail();
			}
		}
}
