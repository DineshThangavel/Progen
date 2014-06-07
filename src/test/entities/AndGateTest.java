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
		andGateForTest = new AndGate("AND_1", "testAndGate");
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
		List<SignalBus> inputList = new ArrayList();

		inputList.add(input1);
		inputList.add(input2);
		SignalBus andGateOutput;
		try {
			andGateOutput = andGateForTest.defaultBehaviour(inputList);
			Assert.assertEquals(andGateOutput.getBusWidth(),
					expected.getBusWidth());
			Assert.assertEquals(andGateOutput.getValue(), expected.getValue());

		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
	}
}
