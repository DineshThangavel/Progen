/**
 * 
 */
package logic.commandslist;

import java.util.List;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.SignalBus;
import helper.Consts;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
public class GetSignalBusValueCommand implements Command{

	@Override
	public String execute(String arguments) throws ProcGenException {
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		String[] splitArguments = arguments.split("\\s+");
		
		assert(splitArguments.length == 2);
		
		String entityId = splitArguments[0];
		String signalName = splitArguments[1];
		
		EntityManager em = activeAppDetails.getActivePrjectInstance().getEntityManager();
		Entity entityReqd = em.getEntityById(entityId);
		
		SignalBus reqdSignalBus = entityReqd.getInputByName(signalName); 
		
		if(reqdSignalBus == null){
			reqdSignalBus = entityReqd.getOutputByName(signalName);
		}
		
		if(reqdSignalBus != null)
			return reqdSignalBus.getDisplayValue();
		
		return Consts.ExceptionMessages.SIGNAL_NOT_RECOGNISED;
	}
	
}
