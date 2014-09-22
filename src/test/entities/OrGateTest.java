package entities;

import helper.InvalidSignalException;
import helper.ProcGenException;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import electronics.logic.entities.OrGate;
import electronics.logic.helper.Entity;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;

public class OrGateTest {

	Entity orGateForTest;
	private OrGate orGateForTest2;

	@BeforeMethod
	public void beforeMethod() {
		try {
			orGateForTest = new OrGate("OR_1", "testOrGate",2,1);
			orGateForTest2 = new OrGate("OR_2", "testOrGate",2,4);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void afterMethod() {
	}

	@DataProvider
	public Object[][] dp() {
		return new Object[][] {
				new Object[] { Signal.LOW, Signal.LOW, Signal.LOW },
				new Object[] { Signal.HIGH, Signal.LOW, Signal.HIGH },
				new Object[] { Signal.LOW, Signal.HIGH, Signal.HIGH },
				new Object[] { Signal.HIGH, Signal.HIGH, Signal.HIGH },
				new Object[] { Signal.HIGH, Signal.UNDEFINED, Signal.HIGH } };
	}

	@Test(dataProvider = "dp")
	public void defaultBehaviourTestWithTwoInputs(Signal input1, Signal input2,
			Signal expectedValue) {

		orGateForTest.getInputPortList().get(0).setValue(input1);
		orGateForTest.getInputPortList().get(1).setValue(input2);

		try {
			orGateForTest.defaultBehaviour();
			
			SignalBus orGateOutput = orGateForTest.getOutputPortList().get(0);
			Assert.assertEquals(orGateOutput.getValue().length, 1);
			Assert.assertEquals(orGateOutput.getValue()[0], expectedValue);
		} catch (ProcGenException e) {
			e.printStackTrace();
		}
	}
	
	  @DataProvider
	  public Object[][] dpMultiWidth() {
	    try {
			return new Object[][] {
			  new Object[] { new SignalBus("input1","1100"),new SignalBus("input2","0001"),new SignalBus("expected","1101")},
			  new Object[] { new SignalBus("input1","1111"),new SignalBus("input2","0001"),new SignalBus("expected","1111")},
			  new Object[] { new SignalBus("input1","1111"),new SignalBus("input2","1111"),new SignalBus("expected","1111")},
			  new Object[] { new SignalBus("input1","0000"),new SignalBus("input2","0011"),new SignalBus("expected","0011")},
			};
		} catch (InvalidSignalException e) {
			Assert.fail();
			e.printStackTrace();
		}
		return null;
	  }
	  
	  @Test(dataProvider = "dpMultiWidth")
	  public void defaultBehaviourTest(SignalBus input1,SignalBus input2, SignalBus expected) {
		  List<SignalBus> inputList = new ArrayList();
		  
		  try {
			orGateForTest2.getInputPortList().get(0).setValue(input1.getValue());
			orGateForTest2.getInputPortList().get(1).setValue(input2.getValue());
		} catch (InvalidSignalException e1) {
			Assert.fail();
			e1.printStackTrace();
		}

		  inputList.add(input2);
		  
		try {
			orGateForTest2.defaultBehaviour();
			SignalBus orGateOutput = orGateForTest2.getOutputPortList().get(0);
			Assert.assertEquals(orGateOutput.getBusWidth(), expected.getBusWidth());
				Assert.assertEquals(orGateOutput.getValue(),expected.getValue());
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
	  }
}
