package entities;

import helper.ProcGenException;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;

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
		try {
			testEntity.addInput(inputName, busWidth);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
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
		try {
			testEntity.addInput(inputName, busWidth);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
		
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
		try {
			testEntity.addOutput(outputName, busWidth);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
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
		try {
			testEntity.addOutput(outputName, busWidth);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
		
		int newNumOfOutputs = testEntity.getNumberOfOutputs();
		if(oldNumOfOutputs != 0)
		Assert.assertEquals(newNumOfOutputs, oldNumOfOutputs);
	}
	
	@Test
	public void testChangeEntityId(){
		Entity parentEntity = new Entity("","parent");
		Entity childEntity1 = new Entity("a","childEntity1");
		Entity childEntity2 = new Entity("b","childEntity2");
		Entity grandChildEntity1 = new Entity("c","grandChildEntity1");
		Entity grandChildEntity2 = new Entity("d","grandChildEntity2");
		
		parentEntity.addChildEntity(childEntity1);
		parentEntity.addChildEntity(childEntity2);
		childEntity1.addChildEntity(grandChildEntity1);
		childEntity2.addChildEntity(grandChildEntity2);
			
		EntityManager em = new EntityManager();
		em.addBaseEntity(parentEntity);
		Assert.assertEquals(childEntity1.getId(), "1-1");
		Assert.assertEquals(childEntity2.getId(), "1-2");
		Assert.assertEquals(grandChildEntity1.getId(), "1-1-1");
		Assert.assertEquals(grandChildEntity2.getId(), "1-2-1");

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
