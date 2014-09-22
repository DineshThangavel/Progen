/**
 * 
 */
package logic.commandslist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import electronics.logic.helper.Connection;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Project;
import electronics.logic.helper.ProjectConnectionManager;
import electronics.logic.helper.SignalBus;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.simulation.ProjectSimulator;
import helper.Consts;
import helper.ProcGenException;
import logic.Command;

/**
 * @author DINESH THANGAVEL
 *
 */
/*
 * arguments isDegubEnabled,
 */
public class StartSimulation implements Command{
	
	@Override
	public String execute(String arguments) throws ProcGenException {

		boolean isDebugEnabled = false;
		boolean isAutomaticInputChangeEnabled = false;
		
		String[] splitArguments = arguments.split("\\s+");
		
		if(splitArguments.length > 0 ){
			try{
				int debugEnable = Integer.parseInt(splitArguments[0]);
				if(debugEnable == 1){
					isDebugEnabled = true;
				}
			}
			catch(NumberFormatException e){
				throw e;
			}
		}
		
		if(splitArguments.length > 1 ){
			try{
				int automatedInputEnable = Integer.parseInt(splitArguments[1]);
				if(automatedInputEnable == 1){
					isAutomaticInputChangeEnabled = true;
				}
			}
			catch(NumberFormatException e){
				throw e;
			}
		}
		
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		Project activeProjectInstance = activeAppDetails.getActivePrjectInstance();
		if(activeProjectInstance==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}

		initialiseConnectionTriggersInProject(activeProjectInstance);
		ProjectSimulator projectSim = activeProjectInstance.getProjectSimulator();
		
		float timeToSimulate = 1000;
		float clockPeriod = 200;
		projectSim.runSimulation(timeToSimulate, clockPeriod,isDebugEnabled,isAutomaticInputChangeEnabled);
		return Consts.CommandResults.SUCCESS_SIMULATION_COMPLETED;
	}
	
	private void initialiseConnectionTriggersInProject(Project activeProjectInstance){
		
		ProjectConnectionManager pcm = activeProjectInstance.getConnectionManager();
		EntityManager em = activeProjectInstance.getEntityManager();
		List<Entity> baseEntityList = em.getBaseEntities();
		
		for(Entity baseEntity : baseEntityList){
			HashMap<String,List<Connection>> entityConnectionDetails = pcm.getConnectionForEntity(baseEntity.getId());
			List<SignalBus> signalsInEntity = baseEntity.getOutputPortList();
			for(SignalBus outputSignal: signalsInEntity){
				SignalBusObserver busMonitor = new SignalBusObserver(outputSignal,activeProjectInstance.getProjectSimulator());
				
				List<Connection> connectionListForOutputSignal= entityConnectionDetails.get(outputSignal);
				if(connectionListForOutputSignal!=null){
				for(Connection signalConnection:connectionListForOutputSignal){
					String destinationEntityId = signalConnection.getDestinationEntityId();
					Entity destinationEntity = em.getEntityById(destinationEntityId);
					
					if(destinationEntity != null){
						// TODO: take care of case where one output is connected to multiple inputs
						busMonitor.addEntitySimulatorListener(destinationEntity.getEntitySimulator());
					}
				}
				}
				activeProjectInstance.getProjectSimulator().getSignalObserverList().add(busMonitor);
			}
		}
	}

}
