/**
 * 
 */
package logic.commandslist;

import java.util.HashMap;
import java.util.List;

import electronics.logic.helper.Connection;
import electronics.logic.helper.ConnectionManager;
import electronics.logic.helper.ConnectionType;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityConnectionManager;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Project;
import electronics.logic.helper.ProjectConnectionManager;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.simulation.ProjectSimulator;
import helper.Consts;
import helper.InvalidSignalException;
import helper.ProcGenException;
import logic.UndoableCommand;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class ConnectCommand implements UndoableCommand {

	public enum EntityConnectionType {
		INTER_BASE_ENTITIES_CONNECTION, // Connection between two base entities
		INTER_CHILD_CONNECTION, // Connection between two child entities
		PARENT_CHILD_CONNECTION, // Connection between parent's input and
									// child's input
		CHILD_PARENT_CONNECTION // Connection between child's output and
								// parent's output
	}

	@Override
	public String execute(String arguments) throws ProcGenException {
		String[] splitArguments = arguments.split("\\s+");
		if (splitArguments.length != 5) {
			throw new ProcGenException(
					Consts.ErrorCodes.INVALID_COMMAND_ARGUMENT,
					Consts.ExceptionMessages.ERROR_CREATING_CONNECTION);
		}

		String sourceEntityId = splitArguments[0];
		String destinationEntityId = splitArguments[1];
		String sourceSignal = splitArguments[2];
		String destinationSignal = splitArguments[3];
		String connectionType = splitArguments[4];

		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade
				.getInstance();
		if (activeAppDetails.getActivePrjectInstance() == null) {
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}

		EntityManager em = activeAppDetails.getActivePrjectInstance()
				.getEntityManager();
		Entity sourceEntity = em.getEntityById(sourceEntityId);
		Entity destinationEntity = em.getEntityById(destinationEntityId);

		EntityConnectionType entityConnectionType = identifyConnectionType(
				sourceEntity, destinationEntity);

		String feedback =null;
		
		if (entityConnectionType == EntityConnectionType.INTER_BASE_ENTITIES_CONNECTION) {
			feedback = establishInterBaseEntityConnection(sourceSignal,
					destinationSignal, activeAppDetails, sourceEntity,
					destinationEntity);
		}

		else if (entityConnectionType == EntityConnectionType.INTER_CHILD_CONNECTION) {
			feedback =  establishInterChildEntityConnection(sourceSignal,
					destinationSignal, activeAppDetails, sourceEntity,
					destinationEntity);
		}

		else if (entityConnectionType == EntityConnectionType.PARENT_CHILD_CONNECTION) {
			feedback =  establishParentChildConnection(sourceSignal,
					destinationSignal, activeAppDetails, sourceEntity,
					destinationEntity);
		}
		
		else if (entityConnectionType == EntityConnectionType.CHILD_PARENT_CONNECTION) {
			feedback = establishChildParentConnection(sourceSignal,
					destinationSignal, activeAppDetails, sourceEntity,
					destinationEntity);
		}
		return feedback;
	}

	private String establishParentChildConnection(String sourceSignal,
			String destinationSignal, ElectronicsLogicFacade activeAppDetails,
			Entity sourceEntity, Entity destinationEntity) throws ProcGenException {

		// if it is connection between entities signal is from output
				SignalBus inputSignal = sourceEntity.getInputByName(sourceSignal);
				SignalBus outputSignal = destinationEntity
						.getInputByName(destinationSignal);

				assert (sourceEntity.getId().equals(destinationEntity.getParent().getId()));

				ConnectionManager cm = sourceEntity.getEntityConnectionManager();

				cm.createConnectionBetweenBaseEntities(sourceEntity, destinationEntity,
						inputSignal, outputSignal, ConnectionType.DIRECT_CONNECTION);
				
				initialiseConnectionTrigger(activeAppDetails,sourceEntity,destinationEntity,inputSignal,outputSignal,EntityConnectionType.PARENT_CHILD_CONNECTION);
				assignValueToOutputSignal(inputSignal, outputSignal,ConnectionType.DIRECT_CONNECTION);
				destinationEntity.getEntitySimulator().runSimulation();
				
				return Consts.CommandResults.SUCCESS_NEW_CONNECTION_CREATION
						+ sourceEntity.getName() + "-" + sourceSignal + ":"
						+ destinationEntity.getName() + "-" + destinationSignal;
	}

	public void assignValueToOutputSignal(SignalBus inputSignal,
			SignalBus outputSignal, ConnectionType directConnection) throws InvalidSignalException {
		// TODO: Check for connection type here
		outputSignal.setValue(inputSignal.getValue());
	}

	private String establishChildParentConnection(String sourceSignal,
			String destinationSignal, ElectronicsLogicFacade activeAppDetails,
			Entity sourceEntity, Entity destinationEntity) throws ProcGenException {

		// if it is connection between entities signal is from output
		SignalBus inputSignal = sourceEntity.getOutputByName(sourceSignal);
		SignalBus outputSignal = destinationEntity
				.getOutputByName(destinationSignal);

		assert (sourceEntity.getParent().getId().equals(destinationEntity.getId()));

		ConnectionManager cm = destinationEntity.getEntityConnectionManager();

		cm.createConnectionBetweenBaseEntities(sourceEntity, destinationEntity,
				inputSignal, outputSignal, ConnectionType.DIRECT_CONNECTION);
		
		initialiseConnectionTrigger(activeAppDetails,sourceEntity,destinationEntity,inputSignal,outputSignal,EntityConnectionType.CHILD_PARENT_CONNECTION);
		assignValueToOutputSignal(inputSignal, outputSignal,ConnectionType.DIRECT_CONNECTION);
		
		return Consts.CommandResults.SUCCESS_NEW_CONNECTION_CREATION
				+ sourceEntity.getName() + "-" + sourceSignal + ":"
				+ destinationEntity.getName() + "-" + destinationSignal;
	}
	
	private String establishInterChildEntityConnection(String sourceSignal,
			String destinationSignal, ElectronicsLogicFacade activeAppDetails,
			Entity sourceEntity, Entity destinationEntity)
			throws ProcGenException {

		// if it is connection between entities signal is from output
		SignalBus inputSignal = sourceEntity.getOutputByName(sourceSignal);
		SignalBus outputSignal = destinationEntity
				.getInputByName(destinationSignal);

		String parentId = sourceEntity.getParent().getId();
		EntityManager em = activeAppDetails.getActivePrjectInstance()
				.getEntityManager();
		Entity parentEntity = em.getEntityById(parentId);

		ConnectionManager cm = parentEntity.getEntityConnectionManager();

		cm.createConnectionBetweenBaseEntities(sourceEntity, destinationEntity,
				inputSignal, outputSignal, ConnectionType.DIRECT_CONNECTION);
		
		initialiseConnectionTrigger(activeAppDetails,sourceEntity,destinationEntity,inputSignal,outputSignal,EntityConnectionType.INTER_CHILD_CONNECTION);
		assignValueToOutputSignal(inputSignal, outputSignal,ConnectionType.DIRECT_CONNECTION);
		destinationEntity.getEntitySimulator().runSimulation();
		
		return Consts.CommandResults.SUCCESS_NEW_CONNECTION_CREATION
				+ sourceEntity.getName() + "-" + sourceSignal + ":"
				+ destinationEntity.getName() + "-" + destinationSignal;
	}

	private String establishInterBaseEntityConnection(String sourceSignal,
			String destinationSignal, ElectronicsLogicFacade activeAppDetails,
			Entity sourceEntity, Entity destinationEntity)
			throws ProcGenException {

		// if it is connection between entities signal is from output
		SignalBus inputSignal = sourceEntity.getOutputByName(sourceSignal);
		SignalBus outputSignal = destinationEntity
				.getInputByName(destinationSignal);

		ConnectionManager cm = activeAppDetails.getActivePrjectInstance()
				.getConnectionManager();
		cm.createConnectionBetweenBaseEntities(sourceEntity, destinationEntity,
				inputSignal, outputSignal, ConnectionType.DIRECT_CONNECTION);
		
		initialiseConnectionTrigger(activeAppDetails,sourceEntity,destinationEntity,inputSignal,outputSignal,EntityConnectionType.INTER_BASE_ENTITIES_CONNECTION);
		assignValueToOutputSignal(inputSignal, outputSignal,ConnectionType.DIRECT_CONNECTION);
		destinationEntity.getEntitySimulator().runSimulation();
		
		return Consts.CommandResults.SUCCESS_NEW_CONNECTION_CREATION
				+ sourceEntity.getName() + "-" + sourceSignal + ":"
				+ destinationEntity.getName() + "-" + destinationSignal;
	}
	
	
	
	private void initialiseConnectionTrigger(ElectronicsLogicFacade activeAppDetails,Entity sourceEntity,Entity destinationEntity,
			SignalBus sourceSignal,SignalBus destinationSignal,EntityConnectionType connectionType){

		Project activeProjectInstance = activeAppDetails.getActivePrjectInstance();
		ProjectSimulator pSim = activeProjectInstance.getProjectSimulator();
		
		SignalBusObserver sourceBusMonitor =null;
		if(pSim.getSignalObserverMap().containsKey(sourceSignal)){
			sourceBusMonitor = pSim.getSignalObserverMap().get(sourceSignal);
		}
		else{
			sourceBusMonitor = new SignalBusObserver(sourceSignal, pSim, sourceEntity.getId()+"-"+sourceSignal.getName());
		}	
		
		ConnectionManager pcm = null;
		// initialise with appropriate connection manager
		if(connectionType.equals(EntityConnectionType.INTER_BASE_ENTITIES_CONNECTION)){
			pcm = activeProjectInstance.getConnectionManager();
		}
		
		else if(connectionType.equals(EntityConnectionType.INTER_CHILD_CONNECTION)){
			Entity parentEntity = sourceEntity.getParent();
			
			assert(parentEntity != null);
				pcm = parentEntity.getEntityConnectionManager();
		}
		
		else if(connectionType.equals(EntityConnectionType.PARENT_CHILD_CONNECTION)){
			pcm = sourceEntity.getEntityConnectionManager();
		}
		
		else if(connectionType.equals(EntityConnectionType.CHILD_PARENT_CONNECTION)){
			pcm = destinationEntity.getEntityConnectionManager();
		}
		
			
			List<Connection> newConnectionListForSourceSignal = pcm.getConnectionForEntity(sourceEntity.getId()).get(sourceSignal.getName());
			if(newConnectionListForSourceSignal!=null){
				sourceBusMonitor.updateConnectionList(newConnectionListForSourceSignal);
			}
			
			if(!sourceBusMonitor.isEntitySimulatorListenerPresent(destinationEntity.getEntitySimulator())){
			// we do not want to start the simulation again for destination
			// entity as only output is changing
				if(connectionType != EntityConnectionType.CHILD_PARENT_CONNECTION) 
				sourceBusMonitor.addEntitySimulatorListener(destinationEntity.getEntitySimulator());
			}
			pSim.getSignalObserverMap().put(sourceSignal, sourceBusMonitor);
		
	}

	private EntityConnectionType identifyConnectionType(Entity sourceEntity,
			Entity destinationEntity) {

		String[] sourceEntityIdSplit = sourceEntity.getId().split("-");
		String[] destinationEntityIdSplit = destinationEntity.getId()
				.split("-");

		// connection between base entitites
		if (sourceEntityIdSplit.length == 1
				&& destinationEntityIdSplit.length == 1) {
			return EntityConnectionType.INTER_BASE_ENTITIES_CONNECTION;
		}

		// connection between parent's input and child's input
		if (sourceEntityIdSplit.length + 1 == destinationEntityIdSplit.length) {
			if (destinationEntity.getParent().getId().equals(sourceEntity.getId())) {
				return EntityConnectionType.PARENT_CHILD_CONNECTION;
			}
		}

		// connection between child's output and parent's output
		if (sourceEntityIdSplit.length == destinationEntityIdSplit.length + 1) {
			if (sourceEntity.getParent().getId().equals(destinationEntity.getId())) {
				return EntityConnectionType.CHILD_PARENT_CONNECTION;
			}
		}

		// connection between child entities
		if (sourceEntityIdSplit.length == destinationEntityIdSplit.length) {
			if (destinationEntity.getParent() == sourceEntity.getParent()) {
				return EntityConnectionType.INTER_CHILD_CONNECTION;
			}
		}
		return null;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
