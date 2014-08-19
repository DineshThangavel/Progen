/**
 * 
 */
package hdl.translator.logic;

import java.util.List;

import de.upb.hni.vmagic.AssociationElement;
import de.upb.hni.vmagic.concurrent.AbstractProcessStatement;
import de.upb.hni.vmagic.concurrent.ComponentInstantiation;
import de.upb.hni.vmagic.concurrent.ConcurrentStatementVisitor;
import de.upb.hni.vmagic.expression.Expression;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.statement.SequentialStatement;

/**
 * @author DINESH THANGAVEL
 *
 */
//TODO: Complete implementation
public class ConcurrentStatementProcessor extends ConcurrentStatementVisitor{
	
    protected void visitProcessStatement(AbstractProcessStatement statement) {
    	List<SequentialStatement> seqStatements = statement.getStatements();  	
 	
    }
    
    protected void visitComponentInstantiation(ComponentInstantiation statement) {
    	List<AssociationElement> associationList =  statement.getPortMap();
    	for(AssociationElement association :associationList){
    		Expression actualExpression = association.getActual();
    		String formalExpression = association.getFormal();
    		if(actualExpression instanceof Signal){
    			((Signal) actualExpression).getMode();
    			((Signal) actualExpression).getType();
    		}
    		
    	}
    }
}
