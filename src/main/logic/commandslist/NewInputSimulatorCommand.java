/**
 * 
 */
package logic.commandslist;

import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Project;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;
import electronics.logic.helper.SignalBusObserver;
import electronics.logic.simulation.InputSimulator;
import electronics.logic.simulation.ProjectSimulator;
import helper.Consts;
import helper.ProcGenException;
import logic.UndoableCommand;

/**
 * @author DINESH THANGAVEL
 *
 */
public class NewInputSimulatorCommand implements UndoableCommand{
	
	@Override
	public String execute(String arguments) throws ProcGenException {
		// parentid should be 0 or ""
		String[] splitArgs = arguments.split("\\s+");
		String nameOfInputSimulator = splitArgs[0];
		int widthOfSimulator = 0 ;
		try{
			widthOfSimulator = Integer.parseInt(splitArgs[1]);
		}
				
		catch(NumberFormatException e){
			throw new ProcGenException(Consts.ErrorCodes.INVALID_COMMAND_ARGUMENT,Consts.ExceptionMessages.ERROR_CREATING_INPUT_SIM);
		}

		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		Project activeProjectInstance = activeAppDetails.getActivePrjectInstance();
		if(activeProjectInstance==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		EntityManager em = activeProjectInstance.getEntityManager();
		String destinationEntityId = splitArgs[2];
		String signalName = splitArgs[3];
		
		Entity entityToConnect = em.getEntityById(destinationEntityId);
		if(entityToConnect == null)
			throw new ProcGenException(Consts.ExceptionMessages.ENTITY_NOT_FOUND);
		
		SignalBus portToConnect = entityToConnect.getInputByName(signalName);
		if(portToConnect == null)
			throw new ProcGenException(Consts.ExceptionMessages.SIGNAL_NOT_RECOGNISED);
			
		InputSimulator newInputSimulator = new InputSimulator(nameOfInputSimulator, widthOfSimulator);
		ProjectSimulator proSim =activeProjectInstance.getProjectSimulator(); 
		proSim.addInputSimulator(newInputSimulator, portToConnect);

		newInputSimulator.setValue(Signal.LOW);

		portToConnect.setValue(Signal.LOW);
		entityToConnect.defaultBehaviour();
		
		SignalBusObserver newSignalBusObserver = new SignalBusObserver(newInputSimulator,activeProjectInstance.getProjectSimulator());
		
		// hook up the entity simulator to the signal observer
		newSignalBusObserver.addEntitySimulatorListener(entityToConnect.getEntitySimulator());
		proSim.getSignalObserverList().add(newSignalBusObserver);
		
		return Consts.CommandResults.SUCCESS_NEW_INPUT_SIMULATOR;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
