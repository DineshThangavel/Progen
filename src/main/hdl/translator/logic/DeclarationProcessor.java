/**
 * 
 */
package hdl.translator.logic;

import java.util.List;

import de.upb.hni.vmagic.DeclarativeRegion;
import de.upb.hni.vmagic.VhdlElement;
import de.upb.hni.vmagic.declaration.Component;
import de.upb.hni.vmagic.declaration.DeclarationVisitor;
import de.upb.hni.vmagic.libraryunit.Architecture;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.VhdlObjectProvider;

/**
 * @author DINESH THANGAVEL
 *
 */
public class DeclarationProcessor extends DeclarationVisitor{
    
	private VhdlToElectronicsConverter hostConverter = null;
	
	/**
     * Visits a component declaration.
     * @param declaration the component declaration
     */
	protected DeclarationProcessor(VhdlToElectronicsConverter converter){
		this.hostConverter = converter;
	}
	
    protected void visitComponentDeclaration(Component declaration) {
    	String nameOfComponent = declaration.getIdentifier();
    	VhdlElement declarationParent = (VhdlElement) declaration.getParent();
    	if(declarationParent instanceof Architecture){
    		String entityName = ((Architecture) declarationParent).getEntity().getIdentifier();
    		
    		// check if entity with component name already present in parsed Entities
    		//TODO: if yes then exit
    		// if no then add to componentToBeResolvedMap
    		this.hostConverter.addUnresovedComponentToList(declaration);
    		this.hostConverter.addComponentDependencyForEntity(entityName, declaration);
    	}
    }
}
