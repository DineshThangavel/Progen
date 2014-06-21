import java.io.IOException;

import hdl.translator.logic.VhdlToElectronicsConverter;
import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.parser.VhdlParserException;

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
		VhdlFile vhdlFile=null;
		try {
			vhdlFile = converter.readVhdlFile("D:\\Processor_Creator\\VhdlHelper\\counter.vhd");
		} catch (IOException | VhdlParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		converter.parseVhdlInput(vhdlFile);
	}
}
