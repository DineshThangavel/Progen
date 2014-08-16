/**
 * 
 */
package hdl.translator.logic;

import java.util.List;

import de.upb.hni.vmagic.concurrent.AbstractProcessStatement;
import de.upb.hni.vmagic.concurrent.ConcurrentStatementVisitor;
import de.upb.hni.vmagic.statement.SequentialStatement;

/**
 * @author DINESH THANGAVEL
 *
 */
//TODO: Complete implementation
public class ConcurrentStatementProcessor extends ConcurrentStatementVisitor{
	
    protected void visitProcessStatement(AbstractProcessStatement statement) {
    	List<SequentialStatement> seqStatements = statement.getStatements();  	
    	//TODO: Complete implementation
    }
}
