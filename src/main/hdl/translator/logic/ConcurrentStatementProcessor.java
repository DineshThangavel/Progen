/**
 * 
 */
package hdl.translator.logic;

import de.upb.hni.vmagic.concurrent.AbstractProcessStatement;
import de.upb.hni.vmagic.concurrent.ComponentInstantiation;
import de.upb.hni.vmagic.concurrent.ConcurrentStatementVisitor;
import de.upb.hni.vmagic.concurrent.ConditionalSignalAssignment;

/**
 * @author DINESH THANGAVEL
 *
 */
//TODO: Complete implementation
public class ConcurrentStatementProcessor extends ConcurrentStatementVisitor{
	
	private VhdlToElectronicsConverter hostConverter = null;
	
	protected ConcurrentStatementProcessor(VhdlToElectronicsConverter converter){
		this.hostConverter = converter;
	}
	
    protected void visitProcessStatement(AbstractProcessStatement statement) {
    	this.hostConverter.addProcessStatementsToConverter(statement);
    	
    }
    
    protected void visitComponentInstantiation(ComponentInstantiation statement) {
    	
    	this.hostConverter.addComponentInstantiationToConverter(statement);
    }
    
    /**
     * Visits a conditional signal assignment.
     * @param statement the statement
     */
    protected void visitConditionalSignalAssignment(ConditionalSignalAssignment statement) {
    	this.hostConverter.addStatementAssignment(statement);
    }
}
