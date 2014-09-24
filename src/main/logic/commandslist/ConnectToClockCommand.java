/**
 * 
 */
package logic.commandslist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import electronics.logic.helper.Connection;
import electronics.logic.helper.ConnectionType;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.SignalBus;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.helper.Entity.EntityTriggerType;
import electronics.logic.simulation.ProjectSimulator;
import helper.Consts;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ConnectToClockCommand implements Command{

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
		
		if(entityReqd == null)
			throw new ProcGenException(Consts.ExceptionMessages.ENTITY_NOT_FOUND);
		
		SignalBus reqdSignalBus = entityReqd.getInputByName(signalName); 
		
		if(reqdSignalBus == null){ // if not in input check output
			reqdSignalBus = entityReqd.getOutputByName(signalName);
		}
		
		if(reqdSignalBus == null)
			throw new ProcGenException(Consts.ExceptionMessages.SIGNAL_NOT_RECOGNISED);
		
		ProjectSimulator prjSim = activeAppDetails.getActivePrjectInstance().getProjectSimulator();
		
		HashMap<SignalBus,SignalBusObserver> prjClkObserverMap = prjSim.getSignalObserverMap();
		
		SignalBusObserver prjClkTrigger = null;
		EntityTriggerType destinationEntityTriggerType = entityReqd.getEntityTriggerType();
		
		Connection newConnectionToAdd = null;
		
		if(destinationEntityTriggerType.equals(EntityTriggerType.RISING_EDGE_TRIGGERED) ){
			if (prjClkObserverMap.containsKey(prjSim.getRisingEdgeTriggerSignal())) {

				prjClkTrigger = prjClkObserverMap.get(prjSim
						.getRisingEdgeTriggerSignal());
			}
			
			else{
				prjClkTrigger = new SignalBusObserver(prjSim.getRisingEdgeTriggerSignal(),prjSim,"project-clk-observer");
			}
			
			newConnectionToAdd = new Connection("",entityReqd.getId(),prjSim.getRisingEdgeTriggerSignal(),reqdSignalBus,ConnectionType.DIRECT_CONNECTION);
		}
		
		else if(destinationEntityTriggerType.equals(EntityTriggerType.FALLING_EDGE_TRIGGERED)){
			if (prjClkObserverMap.containsKey(prjSim.getFallingEdgeTriggerSignal())) {

				prjClkTrigger = prjClkObserverMap.get(prjSim
						.getFallingEdgeTriggerSignal());
			}
			
			else{
				prjClkTrigger = new SignalBusObserver(prjSim.getFallingEdgeTriggerSignal(),prjSim,"project-clk-observer");
			}
			
			newConnectionToAdd = new Connection("",entityReqd.getId(),prjSim.getFallingEdgeTriggerSignal(),reqdSignalBus,ConnectionType.DIRECT_CONNECTION);
		}
		
		else{
				throw new ProcGenException(Consts.ExceptionMessages.INCORRECT_TRIGGER_TYPE + "Registers");
		}

			
			prjClkTrigger.addEntitySimulatorListener(entityReqd.getEntitySimulator());
			List<Connection> connectionsUpdatedByClock = prjClkTrigger.getConnectionsUpdatedByObserver();
			
			
			if(connectionsUpdatedByClock == null){
				connectionsUpdatedByClock = new ArrayList<Connection>();
			}
			
			connectionsUpdatedByClock.add(newConnectionToAdd);
			prjClkTrigger.updateConnectionList(connectionsUpdatedByClock);
			
		return Consts.CommandResults.SUCCESS_NEW_CONNECTION_CREATION + Consts.CommandResults.CLOCK + entityReqd.getName() + ":" + reqdSignalBus.getName();
	}

}
