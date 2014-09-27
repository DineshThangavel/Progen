/**
 * 
 */
package logic.commandslist;

import electronics.logic.entities.Multiplexer;
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
public class NewMultiplexerCommand implements UndoableCommand{

	@Override
	public String execute(String arguments) throws ProcGenException {
		ElectronicsLogicFacade activeAppDetails = ElectronicsLogicFacade.getInstance();
		if(activeAppDetails.getActivePrjectInstance()==null){
			return Consts.CommandResults.CREATE_PROJECT_BEFORE_ENTITY;
		}
		
		String[] splitArguments = arguments.split("\\s+");
		
		String name = splitArguments[0];
		String selectionWidthAsString = splitArguments[1];
		String inputBusWidthAsString = splitArguments[2];
		String parentId = splitArguments[3];
		
		assert((splitArguments.length - 4)%2 == 0);

		Multiplexer newMux = null;
		
		try{
		int selectionWidth = Integer.parseInt(selectionWidthAsString);
		int inputBusWidth = Integer.parseInt(inputBusWidthAsString);
		newMux = new Multiplexer(name,selectionWidth,inputBusWidth);
		}
		
		catch(NumberFormatException e){
			throw new ProcGenException(Consts.ExceptionMessages.INPUT_NOT_RECOGNISED);
		}
		
		for(int i = 4;i< splitArguments.length ; i++){
			SignalBus tempSignalBus = new SignalBus("temp",splitArguments[i]);
			i++;
			newMux.programMux(tempSignalBus.getDisplayValue(), splitArguments[i]);
		}
		
		if(parentId.length()>0 && !parentId.equals("0")){
			EntityManager entityManager = activeAppDetails.getActivePrjectInstance().getEntityManager();
			Entity parentEntity = entityManager.getEntityById(parentId);
			if(parentEntity != null)
				newMux.setParent(parentEntity);
			else{
				throw new ProcGenException(Consts.ExceptionMessages.ENTITY_NOT_FOUND);
			}
		}
		String entityId = activeAppDetails.getActivePrjectInstance().getEntityManager().addEntity(newMux);

		return Consts.CommandResults.SUCCESS_NEW_MUX_CREATION + entityId;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
