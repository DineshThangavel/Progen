/**
 * 
 */
package logic.commandslist;

import electronics.logic.helper.ConnectionManager;
import electronics.logic.helper.ConnectionType;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;
import helper.Consts;
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

		if (entityConnectionType == EntityConnectionType.INTER_BASE_ENTITIES_CONNECTION) {
			return establishInterBaseEntityConnection(sourceSignal,
					destinationSignal, activeAppDetails, sourceEntity,
					destinationEntity);
		}

		else if (entityConnectionType == EntityConnectionType.INTER_CHILD_CONNECTION) {
			return establishInterChildEntityConnection(sourceSignal,
					destinationSignal, activeAppDetails, sourceEntity,
					destinationEntity);
		}

		else if (entityConnectionType == EntityConnectionType.PARENT_CHILD_CONNECTION) {
			return establishParentChildConnection(sourceSignal,
					destinationSignal, activeAppDetails, sourceEntity,
					destinationEntity);
		}
		return null;
	}

	private String establishParentChildConnection(String sourceSignal,
			String destinationSignal, ElectronicsLogicFacade activeAppDetails,
			Entity sourceEntity, Entity destinationEntity) throws ProcGenException {

		// if it is connection between entities signal is from output
				SignalBus inputSignal = sourceEntity.getInputByName(sourceSignal);
				SignalBus outputSignal = destinationEntity
						.getInputByName(destinationSignal);

				assert (sourceEntity.getId() == destinationEntity.getParentId());

				ConnectionManager cm = sourceEntity.getEntityConnectionManager();

				cm.createConnectionBetweenBaseEntities(sourceEntity, destinationEntity,
						inputSignal, outputSignal, ConnectionType.DIRECT_CONNECTION);
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

		assert (sourceEntity.getEntityConnectionManager() == destinationEntity
				.getEntityConnectionManager());

		String parentId = sourceEntity.getParentId();
		EntityManager em = activeAppDetails.getActivePrjectInstance()
				.getEntityManager();
		Entity parentEntity = em.getEntityById(parentId);

		ConnectionManager cm = parentEntity.getEntityConnectionManager();

		cm.createConnectionBetweenBaseEntities(sourceEntity, destinationEntity,
				inputSignal, outputSignal, ConnectionType.DIRECT_CONNECTION);
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
		return Consts.CommandResults.SUCCESS_NEW_CONNECTION_CREATION
				+ sourceEntity.getName() + "-" + sourceSignal + ":"
				+ destinationEntity.getName() + "-" + destinationSignal;
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
			if (destinationEntity.getParentId() == sourceEntity.getId()) {
				return EntityConnectionType.PARENT_CHILD_CONNECTION;
			}
		}

		// connection between child's output and parent's output
		if (sourceEntityIdSplit.length == destinationEntityIdSplit.length + 1) {
			if (sourceEntity.getParentId() == destinationEntity.getId()) {
				return EntityConnectionType.CHILD_PARENT_CONNECTION;
			}
		}

		// connection between child entities
		if (sourceEntityIdSplit.length == destinationEntityIdSplit.length) {
			if (destinationEntity.getParentId() == sourceEntity.getParentId()) {
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
