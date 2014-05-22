package entities;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import electronics.logic.entities.Entity;

public class EntityTest {
	
	Entity testEntity;
	
	@BeforeMethod
	public void beforeMethod() {
		testEntity = new Entity("myTestEntity1","myTestEntity");
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		
		
	}

	@AfterClass
	public void afterClass() {
	}


	@DataProvider
	public Object[][] dpForAddInput() {
		return new Object[][] { new Object[] { 1, "a" },
				new Object[] { 2, "b" } };
	}
	
	
	@Test(dataProvider = "dpForAddInput")
	public void addInputTest(int busWidth, String inputName) {
		
		int oldNumOfInputs = testEntity.getNumberOfInputs(); 
		testEntity.addInput(inputName, busWidth);
		int newNumOfInputs = testEntity.getNumberOfInputs();
		
		Assert.assertEquals(newNumOfInputs, oldNumOfInputs + 1);
		Assert.assertEquals(testEntity.getInputByName(inputName).getBusWidth(), busWidth);
		
	}
	
	@DataProvider
	public Object[][] dpForAddInputDuplicateInput() {
		return new Object[][] { new Object[] { 1, "a" },
				new Object[] { 1, "a" } };
	}

	@Test(dataProvider = "dpForAddInputDuplicateInput")
	public void addInputDuplicateInputTest(int busWidth, String inputName){
		
		int oldNumOfInputs = testEntity.getNumberOfInputs();
		testEntity.addInput(inputName, busWidth);
		
		int newNumOfInputs = testEntity.getNumberOfInputs();
		if(oldNumOfInputs != 0)
		Assert.assertEquals(newNumOfInputs, oldNumOfInputs);
	}

	@DataProvider
	public Object[][] dpForAddOutput() {
		return new Object[][] { new Object[] { 1, "a" },
				new Object[] { 2, "b" } };
	}
		
	@Test(dataProvider = "dpForAddOutput")
	public void addOutput(int busWidth, String outputName) {
		int oldNumOfOutputs = testEntity.getNumberOfOutputs(); 
		testEntity.addOutput(outputName, busWidth);
		int newNumOfOutputs = testEntity.getNumberOfOutputs();
		
		Assert.assertEquals(newNumOfOutputs, oldNumOfOutputs + 1);
		Assert.assertEquals(testEntity.getOutputByName(outputName).getBusWidth(), busWidth);
	}

	
	@DataProvider
	public Object[][] dpForAddOutputDuplicateInput() {
		return new Object[][] { new Object[] { 1, "a" },
				new Object[] { 1, "a" } };
	}

	@Test(dataProvider = "dpForAddOutputDuplicateInput")
	public void addOutputDuplicateOutputTest(int busWidth, String outputName){
		
		int oldNumOfOutputs = testEntity.getNumberOfOutputs();
		testEntity.addOutput(outputName, busWidth);
		
		int newNumOfOutputs = testEntity.getNumberOfOutputs();
		if(oldNumOfOutputs != 0)
		Assert.assertEquals(newNumOfOutputs, oldNumOfOutputs);
	}
	
//	@Test
//	public void makeInvisible() {
//		throw new RuntimeException("Test not implemented");
//	}
//
//	@Test
//	public void startOperation() {
//		throw new RuntimeException("Test not implemented");
//	}
}
