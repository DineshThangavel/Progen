/**
 * 
 */
package hdl.translator.logic;

import java.util.List;

import de.upb.hni.vmagic.concurrent.ConcurrentStatement;
import de.upb.hni.vmagic.declaration.BlockDeclarativeItem;
import de.upb.hni.vmagic.libraryunit.Architecture;
import de.upb.hni.vmagic.libraryunit.Configuration;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.libraryunit.LibraryClause;
import de.upb.hni.vmagic.libraryunit.LibraryUnitVisitor;
import de.upb.hni.vmagic.libraryunit.PackageBody;
import de.upb.hni.vmagic.libraryunit.PackageDeclaration;
import de.upb.hni.vmagic.libraryunit.UseClause;

/**
 * @author DINESH THANGAVEL
 *
 */
public class LibraryUnitParser extends LibraryUnitVisitor{

	@Override
	protected void visitArchitecture(Architecture architecture) {
		List<BlockDeclarativeItem> blockDeclaration = architecture.getDeclarations();
		List<ConcurrentStatement> concurrentStatements = architecture.getStatements();
	}

	@Override
	protected void visitConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		super.visitConfiguration(configuration);
	}

	@Override
	protected void visitEntity(Entity entity) {
		System.out.println("Entity name is " + entity.getIdentifier());
		VMagicProgenConverter.convertToProgenEntity(entity);
	}

	@Override
	protected void visitPackageBody(PackageBody packageBody) {
		// TODO Auto-generated method stub
		super.visitPackageBody(packageBody);
	}

	@Override
	protected void visitPackageDeclaration(PackageDeclaration packageDeclaration) {
		// TODO Auto-generated method stub
		super.visitPackageDeclaration(packageDeclaration);
	}

	@Override
	protected void visitLibraryClause(LibraryClause libraryClause) {
		// TODO Auto-generated method stub
		super.visitLibraryClause(libraryClause);
	}

	@Override
	protected void visitUseClause(UseClause useClause) {
		// TODO Auto-generated method stub
		super.visitUseClause(useClause);
	}

}
