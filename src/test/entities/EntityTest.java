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
import electronics.logic.helper.Project;

public class EntityTest {
	
	Entity testEntity;
	EntityManager em;
	
	@BeforeMethod
	public void beforeMethod() {
		Project testProject = new Project("testProject");
		testEntity = new Entity("myTestEntity");
		try {
			em = testProject.getEntityManager();
			em.addBaseEntity(testEntity);
		} catch (ProcGenException e) {
			e.printStackTrace();
		}
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
			em.addInputSignal(testEntity.getId(), inputName, busWidth);
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
			em.addInputSignal(testEntity.getId(),inputName, busWidth);
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
			em.addOutputSignal(testEntity.getId(), outputName, busWidth);
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
			em.addOutputSignal(testEntity.getId(), outputName, busWidth);
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
		Entity parentEntity = new Entity("parent");
		Entity childEntity1 = new Entity("childEntity1");
		Entity childEntity2 = new Entity("childEntity2");
		Entity grandChildEntity1 = new Entity("grandChildEntity1");
		Entity grandChildEntity2 = new Entity("grandChildEntity2");
		
		Project testProject = new Project("testProject");
		EntityManager em = testProject.getEntityManager();
		try {
			em.addBaseEntity(parentEntity);
			em.addChildEntity(parentEntity.getId(), childEntity1);
			em.addChildEntity(parentEntity.getId(), childEntity2);
			em.addChildEntity(childEntity1.getId(), grandChildEntity1);
			em.addChildEntity(childEntity2.getId(), grandChildEntity2);
		} catch (ProcGenException e) {
			e.printStackTrace();
			Assert.fail();
		}
		

		Assert.assertEquals(childEntity1.getId(), "1-1");
		Assert.assertEquals(childEntity2.getId(), "1-2");
		Assert.assertEquals(grandChildEntity1.getId(), "1-1-1");
		Assert.assertEquals(grandChildEntity2.getId(), "1-2-1");

	}
	
	
	@Test
	public void testGetChildEntityById(){
		Entity parentEntity = new Entity("parent");
		Entity childEntity1 = new Entity("childEntity1");
		Entity childEntity2 = new Entity("childEntity2");
		Entity grandChildEntity1 = new Entity("grandChildEntity1");
		Entity grandChildEntity2 = new Entity("grandChildEntity2");

		Project testProject = new Project("testProject");
		EntityManager em = testProject.getEntityManager();
		try {
			em.addBaseEntity(parentEntity);
			em.addChildEntity(parentEntity.getId(), childEntity1);
			em.addChildEntity(parentEntity.getId(), childEntity2);
			em.addChildEntity(childEntity1.getId(), grandChildEntity1);
			em.addChildEntity(childEntity2.getId(), grandChildEntity2);
			
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
		

		Entity testChildEntity1 = em.getEntityById("1-1");
		Entity testChildEntity2 = em.getEntityById("1-2");
		Entity testGrandChildEntity1 = em.getEntityById("1-1-1");
		Entity testGrandChildEntity2 = em.getEntityById("1-2-1");
		
		System.out.println("testChildEntity1: " + testChildEntity1.getId());
		System.out.println("testChildEntity2: " + testChildEntity2.getId());
		System.out.println("testGrandChildEntity1: " + testGrandChildEntity1.getId());
		System.out.println("testGrandChildEntity2: " + testGrandChildEntity2.getId());
		
		Assert.assertEquals(testChildEntity1, childEntity1);
		Assert.assertEquals(testChildEntity2, childEntity2);
		Assert.assertEquals(testGrandChildEntity1, grandChildEntity1);
		Assert.assertEquals(testGrandChildEntity2, grandChildEntity2);
		
	}	

}
