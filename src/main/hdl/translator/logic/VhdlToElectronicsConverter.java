/**
 * 
 */
package hdl.translator.logic;

import java.io.IOException;
import java.util.List;

import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.libraryunit.LibraryUnit;
import de.upb.hni.vmagic.parser.VhdlParser;
import de.upb.hni.vmagic.parser.VhdlParserException;

/**
 * @author DINESH THANGAVEL
 *
 */
public class VhdlToElectronicsConverter {
	
	public VhdlFile readVhdlFile(String filePath) throws IOException, VhdlParserException{
		VhdlFile inFile = VhdlParser.parseFile(filePath);
		return inFile;
	}
	
	public void parseVhdlInput(VhdlFile vhdlInput){
		LibraryUnitParser libParse = new LibraryUnitParser();
		List<LibraryUnit> inVhdlElements =  vhdlInput.getElements(); 
		libParse.visit(inVhdlElements);
	}
}

