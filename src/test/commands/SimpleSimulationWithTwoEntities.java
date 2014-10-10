package commands;

import hdl.translator.logic.ElectronicsToVhdlConverter;
import helper.ProcGenException;

import java.io.IOException;
import java.util.List;

import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import electronics.logic.helper.Connection;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.helper.SignalValueRecord;

public class SimpleSimulationWithTwoEntities {
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
		  public void testMultipleGateSimulation() {
				try {
					System.out.println(logicInterface.processInput("new_or_gate"));
					System.out.println(logicInterface.processInput("new_and_gate"));
					
					SignalBusObserver orInput0Observer = new SignalBusObserver(testProject.getEntityManager().getEntityById("1").getInputByName("input0"),testProject.getProjectSimulator(),"or-input0Observer");
					SignalBusObserver orInput1Observer = new SignalBusObserver(testProject.getEntityManager().getEntityById("1").getInputByName("input1"),testProject.getProjectSimulator(),"or-input1Observer");
					SignalBusObserver orOutputObserver = new SignalBusObserver(testProject.getEntityManager().getEntityById("1").getOutputByName("output"),testProject.getProjectSimulator(),"or-outputObserver");
					
					SignalBusObserver andInput0Observer = new SignalBusObserver(testProject.getEntityManager().getEntityById("2").getInputByName("input0"),testProject.getProjectSimulator(),"and-input0Observer");
					SignalBusObserver andInput1Observer = new SignalBusObserver(testProject.getEntityManager().getEntityById("2").getInputByName("input1"),testProject.getProjectSimulator(),"and-input1Observer");
					SignalBusObserver andOutputObserver = new SignalBusObserver(testProject.getEntityManager().getEntityById("2").getOutputByName("output"),testProject.getProjectSimulator(),"and-outputObserver");

					
					System.out.println(logicInterface.processInput("new_input_simulator simulator1 1 1 input0"));
					System.out.println(logicInterface.processInput("new_input_simulator simulator2 1 1 input1"));
					
					
					System.out.println(logicInterface.processInput("new_input_simulator simulator3 1 2 input1"));
					System.out.println(logicInterface.processInput("connect 1 2 output input0 default"));
					
					List<Connection> connectionList = testProject.getConnectionManager().getConnectionsForSignal("1", "output");
					Assert.assertEquals(connectionList.size(), 1);
										
					System.out.println(testProject.getEntityManager().getEntityById("1").getOutputByName("output").getValue());
					
					System.out.println(logicInterface.processInput("add_input_stimulus 3 200 'change_sim_input simulator1 0' 200 'change_sim_input simulator2 1' 200 'change_sim_input simulator3 1'"));
					
					System.out.println(logicInterface.processInput("add_input_stimulus 1 400 'change_sim_input simulator3 0'"));
					
					System.out.println(logicInterface.processInput("simulate 0 1"));
					
					System.out.println("OR gate's input 0");
					orInput0Observer.printSignalLogToConsole();
					
					System.out.println("OR gate's input 1");
					orInput1Observer.printSignalLogToConsole();
					
					System.out.println("OR gate's output");
					orOutputObserver.printSignalLogToConsole();
					
					System.out.println("AND gate's input0");
					andInput0Observer.printSignalLogToConsole();
					
					System.out.println("AND gate's input1");
					andInput1Observer.printSignalLogToConsole();
					
					System.out.println("AND gate's output");
					andOutputObserver.printSignalLogToConsole();
					
					List<SignalValueRecord> orOutputSignalVariations = orOutputObserver.getSignalValueRecords();
					Assert.assertEquals(orOutputSignalVariations.size(), 2);
					Assert.assertEquals(orOutputSignalVariations.get(0).getDisplayStringForCurrentValue(), "L");
					Assert.assertEquals(orOutputSignalVariations.get(1).getDisplayStringForCurrentValue(), "H");
					
					List<SignalValueRecord> andOutputSignalVariations = andOutputObserver.getSignalValueRecords();
					Assert.assertEquals(andOutputSignalVariations.size(), 3);
					Assert.assertEquals(andOutputSignalVariations.get(0).getDisplayStringForCurrentValue(), "L");
					Assert.assertEquals(andOutputSignalVariations.get(1).getDisplayStringForCurrentValue(), "H");
					Assert.assertEquals(andOutputSignalVariations.get(1).getDisplayStringForCurrentValue(), "H");
										
					System.out.println(testProject.getEntityManager().getEntityById("1").getOutputByName("output").getValue());
					
				} catch (ProcGenException e) {
					System.out.println(e.getMessage());
					Assert.fail();
					e.printStackTrace();
				}
		  }
		  
		
}
