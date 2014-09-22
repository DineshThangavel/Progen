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

import electronics.logic.entities.AndGate;
import electronics.logic.helper.Entity;
import electronics.logic.helper.SignalBus;

public class AndGateTest {

	Entity andGateForTest;

	@BeforeMethod
	public void beforeMethod() {
		try {
			andGateForTest = new AndGate("AND_1", "testAndGate",2,4);
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
		try {
			return new Object[][] {
					new Object[] { new SignalBus("input1", "1100"),
							new SignalBus("input2", "0001"),
							new SignalBus("expected", "0000") },
					new Object[] { new SignalBus("input1", "1111"),
							new SignalBus("input2", "0001"),
							new SignalBus("expected", "0001") },
					new Object[] { new SignalBus("input1", "1111"),
							new SignalBus("input2", "1111"),
							new SignalBus("expected", "1111") },
					new Object[] { new SignalBus("input1", "0000"),
							new SignalBus("input2", "0011"),
							new SignalBus("expected", "0000") }, };
		} catch (InvalidSignalException e) {
			Assert.fail();
			e.printStackTrace();
		}
		return null;
	}

	@Test(dataProvider = "dp")
	public void defaultBehaviourTest(SignalBus input1, SignalBus input2,
			SignalBus expected) {

		
		try {
			andGateForTest.getInputPortList().get(0).setValue(input1.getValue());
			andGateForTest.getInputPortList().get(1).setValue(input2.getValue());
		} catch (InvalidSignalException e1) {
			Assert.fail();
			e1.printStackTrace();
		}
		
		try {
			andGateForTest.defaultBehaviour();
			SignalBus andGateOutput = andGateForTest.getOutputPortList().get(0);
			Assert.assertEquals(andGateOutput.getBusWidth(),
					expected.getBusWidth());
			Assert.assertEquals(andGateOutput.getValue(), expected.getValue());

		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}
}
