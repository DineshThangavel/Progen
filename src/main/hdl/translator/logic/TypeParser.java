/**
 * 
 */
package hdl.translator.logic;

import de.upb.hni.vmagic.type.AccessType;
import de.upb.hni.vmagic.type.ConstrainedArray;
import de.upb.hni.vmagic.type.EnumerationType;
import de.upb.hni.vmagic.type.FileType;
import de.upb.hni.vmagic.type.IncompleteType;
import de.upb.hni.vmagic.type.IntegerType;
import de.upb.hni.vmagic.type.PhysicalType;
import de.upb.hni.vmagic.type.RecordType;
import de.upb.hni.vmagic.type.Type;
import de.upb.hni.vmagic.type.TypeVisitor;
import de.upb.hni.vmagic.type.UnconstrainedArray;

/**
 * @author DINESH THANGAVEL
 *
 */
public class TypeParser extends TypeVisitor{

	@Override
	public void visit(Type type) {
		super.visit(type);
	}

	@Override
	protected void visitAccessType(AccessType type) {
		// TODO Auto-generated method stub
		super.visitAccessType(type);
	}

	@Override
	protected void visitConstrainedArray(ConstrainedArray type) {
		// TODO Auto-generated method stub
		super.visitConstrainedArray(type);
	}

	@Override
	protected void visitEnumerationType(EnumerationType type) {
		// TODO Auto-generated method stub
		super.visitEnumerationType(type);
	}

	@Override
	protected void visitFileType(FileType type) {
		// TODO Auto-generated method stub
		super.visitFileType(type);
	}

	@Override
	protected void visitIncompleteType(IncompleteType type) {
		// TODO Auto-generated method stub
		super.visitIncompleteType(type);
	}

	@Override
	protected void visitIntegerType(IntegerType type) {
		// TODO Auto-generated method stub
		super.visitIntegerType(type);
	}

	@Override
	protected void visitPhysicalType(PhysicalType type) {
		// TODO Auto-generated method stub
		super.visitPhysicalType(type);
	}

	@Override
	protected void visitRecordType(RecordType type) {
		// TODO Auto-generated method stub
		super.visitRecordType(type);
	}

	@Override
	protected void visitUnconstrainedArray(UnconstrainedArray type) {
		// TODO Auto-generated method stub
		super.visitUnconstrainedArray(type);
	}
	
}
