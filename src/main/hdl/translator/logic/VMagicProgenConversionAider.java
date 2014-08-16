/**
 * 
 */
package hdl.translator.logic;

import helper.Consts;
import helper.ProcGenException;

import java.util.Iterator;

import userinterface.EntityDetailsRetriever.EntityDetailsFromUser;
import de.upb.hni.vmagic.Range;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.literal.DecimalLiteral;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.VhdlObject.Mode;
import de.upb.hni.vmagic.object.VhdlObjectProvider;
import de.upb.hni.vmagic.type.IndexSubtypeIndication;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.EntityManager;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class VMagicProgenConversionAider {
	
	public static  EntityDetailsFromUser convertToProgenEntity(Entity vMagicEntity) throws ProcGenException {
		EntityDetailsFromUser newEntityDetails = new EntityDetailsFromUser();
		newEntityDetails.nameOfEntity = vMagicEntity.getIdentifier();
		// TODO: Hardcoded value;
		newEntityDetails.parentOfEntity = ""; 
		
		for (VhdlObjectProvider<Signal> portsList : vMagicEntity.getPort()) {
			Iterator<Signal> iterator = portsList.getVhdlObjects().iterator();
			while (iterator.hasNext()) {
				Signal port = iterator.next();
				
				// default signalBusWidth
				int signalBusWidth = 1; 
				
				if(port.getType() instanceof IndexSubtypeIndication){
					IndexSubtypeIndication s = (IndexSubtypeIndication) port.getType();
					Range range = (Range)s.getRanges().get(0);
					
					int startingIndex = Integer.valueOf(((DecimalLiteral)range.getFrom()).getValue());
					int endingIndex = Integer.valueOf(((DecimalLiteral)range.getTo()).getValue());
					signalBusWidth = (startingIndex - endingIndex) + 1;
				}
				
				if(port.getMode() == Mode.IN){
					newEntityDetails.inputSignalNames.put(port.getIdentifier(), signalBusWidth);
				}
				
				else if(port.getMode() == Mode.OUT){
					newEntityDetails.outputSignalNames.put(port.getIdentifier(), signalBusWidth);
				}
				
				// TODO: Take care of other Modes as well
			}
		}
		
		return newEntityDetails;
	}
}
