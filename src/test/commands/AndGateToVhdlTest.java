package commands;

import java.io.IOException;

import hdl.translator.logic.ElectronicsToVhdlConverter;
import hdl.translator.logic.EntityToVhdlConverter;
import helper.ProcGenException;
import logic.LogicFacade;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.upb.hni.vmagic.output.VhdlOutput;
import electronics.logic.entities.AndGate;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
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
		  public void testNewInputSimulatorWithAndGate() {
				try {
					System.out.println(logicInterface.processInput("new_and_gate"));
					AndGate andGate = (AndGate) testProject.getEntityManager().getEntityById("1");
					EntityToVhdlConverter e = new EntityToVhdlConverter();
					System.out.println("Converting And Gate...");
					
					ElectronicsToVhdlConverter ec = new ElectronicsToVhdlConverter();
					ec.convertProjectToVhdl(testProject,"D:\\Processor_Creator\\VhdlHelper\\vhdl_output1");
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
