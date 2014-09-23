package commands;

import helper.ProcGenException;

import java.util.ArrayList;
import java.util.List;

import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.helper.SignalValueRecord;
import electronics.logic.simulation.ProjectSimulator;

public class ClockSimulationTest {
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
  public void testCircuitClock() {
	  try {
		ProjectSimulator newPS = testProject.getProjectSimulator();
		SignalBusObserver prjClkObserver = new SignalBusObserver(newPS.getProjectClock(),newPS,"clockObserver"); 
		
		float timeToSimulate = 1000;
		float clockPeriod = 200;
		newPS.runSimulation(timeToSimulate, clockPeriod,false,false);
		
		List<SignalValueRecord> actualClkValues = prjClkObserver.getSignalValueRecords();
		
		// computing expectedClkValue
		Signal[] initialOldSignal = new Signal[1];
		initialOldSignal[0] = Signal.UNDEFINED;
		
		Signal[] initialNewSignal = new Signal[1];
		initialNewSignal[0] = Signal.HIGH;
		
		SignalValueRecord recordAfterChange = new SignalValueRecord(0,initialOldSignal,initialNewSignal);
		
		Assert.assertEquals(actualClkValues.get(0),recordAfterChange);
		float t = 0;
		Signal signalValue = Signal.HIGH;
		
		for(int i =1; i< actualClkValues.size();i++){
			SignalValueRecord recordAfterFirstChange = actualClkValues.get(i);
			Assert.assertEquals(recordAfterFirstChange.getTimeOfRecord(), t);
			t = t + clockPeriod/2;

			if(signalValue == Signal.HIGH){
				Assert.assertEquals(recordAfterFirstChange.getDisplayStringForCurrentValue(), "L");
				signalValue = Signal.LOW;
			}
			
			else{
				Assert.assertEquals(recordAfterFirstChange.getDisplayStringForCurrentValue(), "H");
				signalValue = Signal.HIGH;
			}
		}
		
		prjClkObserver.printSignalLogToConsole();
		
	} catch (ProcGenException e) {
		Assert.fail();
		e.printStackTrace();
	}
  }

}
