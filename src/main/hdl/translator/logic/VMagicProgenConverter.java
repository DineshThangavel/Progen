/**
 * 
 */
package hdl.translator.logic;

import java.util.Iterator;
import java.util.List;

import userinterface.EntityDetailsRetriever.EntityDetailsFromUser;
import de.upb.hni.vmagic.declaration.EntityDeclarativeItem;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.VhdlObject.Mode;
import de.upb.hni.vmagic.object.VhdlObjectProvider;
import de.upb.hni.vmagic.parser.antlr.VhdlAntlrParser.type_declaration_return;
import de.upb.hni.vmagic.type.IndexSubtypeIndication;
import de.upb.hni.vmagic.type.SubtypeIndication;
import de.upb.hni.vmagic.type.Type;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class VMagicProgenConverter {
	public static String convertToProgenEntity(Entity vMagicEntity) {
		EntityDetailsFromUser newEntityDetails = new EntityDetailsFromUser();
		newEntityDetails.nameOfEntity = vMagicEntity.getIdentifier();

		List<EntityDeclarativeItem> d = vMagicEntity.getDeclarations();
		System.out.println(d.get(0).toString());
		
//		for (VhdlObjectProvider<Signal> portsList : vMagicEntity.getPort()) {
//			Iterator<Signal> iterator = portsList.getVhdlObjects().iterator();
//			while (iterator.hasNext()) {
//				Signal port = iterator.next();
//				System.out.println("Signal is " + port.getIdentifier());
//				System.out.println("Signal width is " + port.getPrecedence());
//				System.out.println("Signal type is " + port.getType());
//				
//				if(port.getType() instanceof IndexSubtypeIndication){
//					IndexSubtypeIndication s = (IndexSubtypeIndication) port.getType();
//					System.out.println("Base type is" + s.getBaseType());
//					System.out.println("Range is" + s.getRanges().get(0));
//				}
//				
//			}
//		}
		return null;

	}
}
