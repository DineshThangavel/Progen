package vhdlOutputTests;

import java.io.File;
import java.io.IOException;

import hdl.translator.logic.ElectronicsToVhdlConverter;
import helper.ProcGenException;
import logic.LogicFacade;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;

public class TestConnectedGatesVhdlConversion {
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
				System.out.println(logicInterface.processInput("new_or_gate"));
				System.out.println(logicInterface.processInput("new_and_gate"));
				System.out.println(logicInterface.processInput("connect 1 2 output input0 default"));
				
				ec.convertProjectToVhdl(testProject,TestUtils.OutputFolderDirectory);
				
				String actualOutputFileName = TestUtils.OutputFolderDirectory + "\\" + testProject.getName() + ".vhd";
				String baselineFileName = TestUtils.BaselineFolderDirectory + "\\" + "ConnectedOrAndGates.bsl";
				
				File actualOutputFile = new File(actualOutputFileName);
				File baselineFile = new File(baselineFileName);
				
				Assert.assertEquals(FileUtils.readLines(actualOutputFile), FileUtils.readLines(baselineFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcGenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  } 
}
