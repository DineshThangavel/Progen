package vhdlOutputTests;

import java.io.File;
import java.io.IOException;

import hdl.translator.logic.ElectronicsToVhdlConverter;
import hdl.translator.logic.HdlConsts.HdlConversionType;
import helper.ProcGenException;
import logic.LogicFacade;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.Project;

public class TestParentChildConvertionToVhdl {
  
	Project testProject = null;
	LogicFacade logicInterface = new LogicFacade();

	@BeforeMethod
	public void beforeMethod() {
	
		// Create a new project
		try {
			logicInterface.processInput("new_project testProject");
			testProject = ElectronicsLogicFacade.getInstance().getActivePrjectInstance();
			TestUtils.clearFilesInOutputFolder();
		} catch (ProcGenException e) {
			
			Assert.fail();
			e.printStackTrace();
		} catch (IOException e) {
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
  public void testMultipleGateVhdlConversion(){
	  ElectronicsToVhdlConverter ec = new ElectronicsToVhdlConverter(testProject);
	  try {
		  	// Entity1 with 2 or gates
			System.out.println(logicInterface.processInput("new_entity entity1 3 1 0 input0 1 input1 1 input2 1 output 1"));
			System.out.println(logicInterface.processInput("new_or_gate orGateTest 2 1"));
			System.out.println(logicInterface.processInput("new_or_gate orGateTest 2 1"));
			
			System.out.println(logicInterface.processInput("connect 1 1-1 input0 input0 default"));
			System.out.println(logicInterface.processInput("connect 1 1-1 input1 input1 default"));
			System.out.println(logicInterface.processInput("connect 1-1 1-2 output input0 default"));
			System.out.println(logicInterface.processInput("connect 1 1-2 input2 input1 default"));
			System.out.println(logicInterface.processInput("connect 1-2 1 output output default"));
			
			System.out.println(logicInterface.processInput("new_entity entity2 3 1 0 input0 1 input1 1 input2 1 output 1"));
			System.out.println(logicInterface.processInput("new_and_gate andGateTest 2 2"));
			System.out.println(logicInterface.processInput("new_and_gate andGateTest 2 2"));
			
			System.out.println(logicInterface.processInput("connect 2 2-1 input0 input0 default"));
			System.out.println(logicInterface.processInput("connect 2 2-1 input1 input1 default"));
			System.out.println(logicInterface.processInput("connect 2-1 2-2 output input0 default"));
			System.out.println(logicInterface.processInput("connect 2 2-2 input2 input1 default"));
			System.out.println(logicInterface.processInput("connect 2-2 2 output output default"));
			
			System.out.println(logicInterface.processInput("connect 1 2 output input0 default"));
			
			ec.convertProjectToVhdl(testProject,TestUtils.OutputFolderDirectory);
			
			String testProjectVhdFileName = TestUtils.OutputFolderDirectory + "\\" + testProject.getName() + ".vhd";
			String testProjectVhdbaselineFileName = TestUtils.BaselineFolderDirectory + "\\" + "ParentChildTestProject.bsl";
			
			File actualOutputFile = new File(testProjectVhdFileName);
			File baselineFile = new File(testProjectVhdbaselineFileName);
			
			Assert.assertEquals(FileUtils.readLines(actualOutputFile), FileUtils.readLines(baselineFile));
			
			// check entity1 file:
			String entity1VhdFileName = TestUtils.OutputFolderDirectory + "\\" + "entity1" + ".vhd";
			String entity1VhdbaselineFileName = TestUtils.BaselineFolderDirectory + "\\" + "ParentChildEntity1InlineAndOr.bsl";
			
			File enitity1ActualOutputFile = new File(entity1VhdFileName);
			File enitity1BaselineFile = new File(entity1VhdbaselineFileName);
			
			Assert.assertEquals(FileUtils.readLines(enitity1ActualOutputFile), FileUtils.readLines(enitity1BaselineFile));
			
			// check entity2 file:
			String entity2VhdFileName = TestUtils.OutputFolderDirectory + "\\"
					+ "entity2" + ".vhd";
			String entity2VhdbaselineFileName = TestUtils.BaselineFolderDirectory
					+ "\\" + "ParentChildEntity2InlineAndOr.bsl";

			File enitity2ActualOutputFile = new File(entity2VhdFileName);
			File enitity2BaselineFile = new File(entity2VhdbaselineFileName);

			Assert.assertEquals(FileUtils.readLines(enitity2ActualOutputFile),
					FileUtils.readLines(enitity2BaselineFile));
			
	  } catch (IOException e) {
		Assert.fail();
		e.printStackTrace();
	} catch (ProcGenException e) {
		Assert.fail();
		e.printStackTrace();
	}
  }
  
  
  @Test
  public void testMultipleGateVhdlConversionWithSeparateAndOrGates(){
	  ElectronicsToVhdlConverter ec = new ElectronicsToVhdlConverter(testProject);
	  try {
		  	// Entity1 with 2 or gates
			System.out.println(logicInterface.processInput("new_entity entity1 3 1 0 input0 1 input1 1 input2 1 output 1"));
			System.out.println(logicInterface.processInput("new_or_gate orGateTest 2 1"));
			System.out.println(logicInterface.processInput("new_or_gate orGateTest 2 1"));
			
			Entity entityToChange = testProject.getEntityManager().getEntityById("1-1");
			entityToChange.setHdlConversionType(HdlConversionType.SeparateEntity);
			
			System.out.println(logicInterface.processInput("connect 1 1-1 input0 input0 default"));
			System.out.println(logicInterface.processInput("connect 1 1-1 input1 input1 default"));
			System.out.println(logicInterface.processInput("connect 1-1 1-2 output input0 default"));
			System.out.println(logicInterface.processInput("connect 1 1-2 input2 input1 default"));
			System.out.println(logicInterface.processInput("connect 1-2 1 output output default"));
			
			System.out.println(logicInterface.processInput("new_entity entity2 3 1 0 input0 1 input1 1 input2 1 output 1"));
			System.out.println(logicInterface.processInput("new_and_gate andGateTest 2 2"));
			System.out.println(logicInterface.processInput("new_and_gate andGateTest 2 2"));
			
			System.out.println(logicInterface.processInput("connect 2 2-1 input0 input0 default"));
			System.out.println(logicInterface.processInput("connect 2 2-1 input1 input1 default"));
			System.out.println(logicInterface.processInput("connect 2-1 2-2 output input0 default"));
			System.out.println(logicInterface.processInput("connect 2 2-2 input2 input1 default"));
			System.out.println(logicInterface.processInput("connect 2-2 2 output output default"));
			
			System.out.println(logicInterface.processInput("connect 1 2 output input0 default"));
			
			ec.convertProjectToVhdl(testProject,TestUtils.OutputFolderDirectory);
			
			String testProjectVhdFileName = TestUtils.OutputFolderDirectory + "\\" + testProject.getName() + ".vhd";
			String testProjectVhdbaselineFileName = TestUtils.BaselineFolderDirectory + "\\" + "ParentChildTestProject.bsl";
			
			File actualOutputFile = new File(testProjectVhdFileName);
			File baselineFile = new File(testProjectVhdbaselineFileName);
			
			Assert.assertEquals(FileUtils.readLines(actualOutputFile), FileUtils.readLines(baselineFile));
			
			// check entity1 file:
			String entity1VhdFileName = TestUtils.OutputFolderDirectory + "\\" + "entity1" + ".vhd";
			String entity1VhdbaselineFileName = TestUtils.BaselineFolderDirectory + "\\" + "ParentChildEntity1SeparateOr.bsl";
			
			File enitity1ActualOutputFile = new File(entity1VhdFileName);
			File enitity1BaselineFile = new File(entity1VhdbaselineFileName);
			
			Assert.assertEquals(FileUtils.readLines(enitity1ActualOutputFile), FileUtils.readLines(enitity1BaselineFile));
			
			// check entity2 file:
			String entity2VhdFileName = TestUtils.OutputFolderDirectory + "\\"
					+ "entity2" + ".vhd";
			String entity2VhdbaselineFileName = TestUtils.BaselineFolderDirectory
					+ "\\" + "ParentChildEntity2InlineAndOr.bsl";

			File enitity2ActualOutputFile = new File(entity2VhdFileName);
			File enitity2BaselineFile = new File(entity2VhdbaselineFileName);

			Assert.assertEquals(FileUtils.readLines(enitity2ActualOutputFile),
					FileUtils.readLines(enitity2BaselineFile));
			
	} catch (IOException e) {
		Assert.fail();
		e.printStackTrace();
	} catch (ProcGenException e) {
		Assert.fail();
		e.printStackTrace();
	}
  }
  
}
