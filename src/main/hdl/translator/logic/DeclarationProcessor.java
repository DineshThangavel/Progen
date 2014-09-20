/**
 * 
 */
package hdl.translator.logic;

import java.util.List;

import de.upb.hni.vmagic.DeclarativeRegion;
import de.upb.hni.vmagic.VhdlElement;
import de.upb.hni.vmagic.declaration.Component;
import de.upb.hni.vmagic.declaration.DeclarationVisitor;
import de.upb.hni.vmagic.declaration.SignalDeclaration;
import de.upb.hni.vmagic.declaration.VariableDeclaration;
import de.upb.hni.vmagic.libraryunit.Architecture;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.Variable;
import de.upb.hni.vmagic.object.VhdlObjectProvider;

/**
 * @author DINESH THANGAVEL
 *
 */
public class DeclarationProcessor extends DeclarationVisitor{
    
	private VhdlToElectronicsConverter hostConverter = null;
	
	protected DeclarationProcessor(VhdlToElectronicsConverter converter){
		this.hostConverter = converter;
	}
	
	/**
     * Visits a component declaration.
     * @param declaration the component declaration
     */
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
    
    /**
     * Visits a signal declaration.
     * @param declaration the signal declaration
     */
    protected void visitSignalDeclaration(SignalDeclaration declaration) {
    	List<Signal> allSignalsList= declaration.getObjects();
    	
    	//set global variable declaredSignals
    	this.hostConverter.setDeclaredSignals(allSignalsList);
    }
    
    /**
     * Visits a variable declaration.
     * @param declaration the variable declaration
     */
    protected void visitVariableDeclaration(VariableDeclaration declaration) {
    	List<Variable> allVariablesList = declaration.getObjects();
    	
    	//set global variable declaredSignals
    	this.hostConverter.setDeclaredVariables(allVariablesList);
    }
}
