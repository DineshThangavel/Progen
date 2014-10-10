package vhdlOutputTests;

import hdl.translator.logic.ElectronicsToVhdlConverter;
import hdl.translator.logic.EntityToVhdlConverter;
import helper.ProcGenException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import logic.LogicFacade;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import electronics.logic.entities.AndGate;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;

public class AndGateToVhdlTest {
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
		  public void testNewInputSimulatorWithAndGate() {
				try {
					System.out.println(logicInterface.processInput("new_and_gate"));
					AndGate andGate = (AndGate) testProject.getEntityManager().getEntityById("1");
					EntityToVhdlConverter e = new EntityToVhdlConverter();
					System.out.println("Converting And Gate...");
					
					ElectronicsToVhdlConverter ec = new ElectronicsToVhdlConverter(testProject);
					ec.convertProjectToVhdl(testProject,TestUtils.OutputFolderDirectory);

					String actualOutputFileName = TestUtils.OutputFolderDirectory + "\\" + testProject.getName() + ".vhd";
					String baselineFileName = TestUtils.BaselineFolderDirectory + "\\" + "AndGateVhdlCode.bsl";
					
					File actualOutputFile = new File(actualOutputFileName);
					File baselineFile = new File(baselineFileName);
					
					Assert.assertEquals(FileUtils.readLines(actualOutputFile), FileUtils.readLines(baselineFile));
				}		
				catch (ProcGenException e) {
					Assert.fail();
					e.printStackTrace();
				} catch (IOException e1) {
					Assert.fail();
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}
		  	}
}
