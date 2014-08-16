import java.io.IOException;

import logic.Command;
import logic.commandslist.DisplayAllEntities;
import hdl.translator.logic.ElectronicsToVhdlConverter;
import hdl.translator.logic.VhdlToElectronicsConverter;
import helper.ProcGenException;
import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.parser.VhdlParserException;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.Project;

/**
 * 
 */

/**
 * @author DINESH THANGAVEL
 *
 */
public class MainForVhdlConversionTesting {
	
	public static void main(String[] args) {
		VhdlToElectronicsConverter converter= new VhdlToElectronicsConverter();
//		VhdlFile vhdlFile=null;
//		try {
//			vhdlFile = converter.readVhdlFile("D:\\Processor_Creator\\VhdlHelper\\microprocessor.vhd");
//		} catch (IOException | VhdlParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			ElectronicsLogicFacade eleLogic = ElectronicsLogicFacade.getInstance();
			eleLogic.createNewProject("test");
			Project newProject = eleLogic.getActivePrjectInstance();
			Entity convertedEntity = converter.convertToProgen("D:\\Processor_Creator\\VhdlHelper\\vhdl_input\\A0088592M_LAB1\\Homework\\case_converter.vhd");
			
			newProject.getEntityManager().addEntity(convertedEntity);
			Command displayEntitiesCommand = new DisplayAllEntities();
			displayEntitiesCommand.execute(null);
			
			ElectronicsToVhdlConverter e = new ElectronicsToVhdlConverter();
			e.convertProjectToVhdl(newProject,"D:\\Processor_Creator\\VhdlHelper\\vhdl_output");
		} catch (ProcGenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
