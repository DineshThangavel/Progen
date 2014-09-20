/**
 * 
 */
package hdl.translator.logic;

import de.upb.hni.vmagic.statement.IfStatement;
import de.upb.hni.vmagic.statement.SequentialStatementVisitor;

/**
 * @author DINESH THANGAVEL
 *
 */
public class SequentialStatementProcessor extends SequentialStatementVisitor{
	  /**
     * Visits a if statement.
     * @param statement the statement
     */
    protected void visitIfStatement(IfStatement statement) {
        visit(statement.getStatements());
        for (IfStatement.ElsifPart elsifPart : statement.getElsifParts()) {
            visitIfStatementElsifPart(elsifPart);
        }
        visit(statement.getElseStatements());
    }

    /**
     * Visits the elsif part of a if statement.
     * @param part the elsif part
     */
    protected void visitIfStatementElsifPart(IfStatement.ElsifPart part) {
        visit(part.getStatements());
    }

}
