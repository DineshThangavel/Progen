/**
 * 
 */
package hdl.translator.logic;

import helper.ProcGenException;

import java.io.IOException;
import java.util.List;

import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.parser.VhdlParser;
import de.upb.hni.vmagic.parser.VhdlParserException;
import de.upb.hni.vmagic.util.VhdlCollections;

/**
 * @author DINESH THANGAVEL
 *
 */
public class VhdlToElectronicsConverter {
	
	public VhdlFile readVhdlFile(String filePath) throws IOException, VhdlParserException{
		VhdlFile inFile = VhdlParser.parseFile(filePath);
		return inFile;
	}
	
	public void convertToProgen(VhdlFile vhdlFileInput) throws ProcGenException{
		List<Entity> entityList = VhdlCollections.getAll(vhdlFileInput.getElements(), Entity.class);
		for(Entity entity:entityList){
			VMagicProgenConverter.convertToProgenEntity(entity);	
		}
		
	}
	
	public void convertToProgen(String fileName) throws ProcGenException{
		VhdlFile vhdFile;
		try {
			vhdFile = this.readVhdlFile(fileName);
			this.convertToProgen(vhdFile);
		} catch (IOException | VhdlParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

