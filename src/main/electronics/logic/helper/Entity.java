/**
 *	Represents an Electronic entity
 *	 An enitty can be the topmost entity in a project (called baseEntity) or within other entities
 */
package electronics.logic.helper;

import helper.Consts;
import helper.InvalidSignalException;
import helper.ProcGenException;

import java.util.ArrayList;
import java.util.List;

import electronics.logic.helper.*;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class Entity {

	/**
	 * id should be unique for every new entity that is created. This uniqueness
	 * is taken care by EntityMangaer when adding the entity to the project
	 */

	private String id;
	private String name;
	private String parentId;
	// TODO check if this can be changed to hashmap for better performance
	private List<SignalBus> inputList = new ArrayList<SignalBus>();
	private List<SignalBus> outputList = new ArrayList<SignalBus>();
	private List<Entity> entityList = new ArrayList<Entity>();
	private ConnectionManager entityConnectionManager = new ConnectionManager();

	public Entity(String id, String name) {

		this.id = id;
		this.name = name;
		this.parentId = null;
	}

	/*
	 * parentId decides if the entity is base entity For base entity parentId is
	 * null and this is assumed by default
	 */
	public Entity(String id, String name, String parentId) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}

	public int getNumberOfInputs() {
		return inputList.size();
	}

	public int getNumberOfOutputs() {
		return outputList.size();
	}

	public String getId() {
		return this.id;
	}

	/**
	 * This method returns a reference to the actual input signal bus
	 * 
	 */
	public SignalBus getInputByName(String name) {
		SignalBus signalToReturn = null;

		for (SignalBus input : inputList) {
			if (input.getName() == name && name != "") {
				signalToReturn = input;

			}
		}

		return signalToReturn;
	}

	/**
	 * This method returns a reference to the actual output signal bus
	 * 
	 */
	public SignalBus getOutputByName(String name) {
		SignalBus signalToReturn = null;

		for (SignalBus output : outputList) {
			if (output.getName() == name && name != "") {
				signalToReturn = output;

			}
		}

		return signalToReturn;
	}

	/**
	 * Adds an input to the input list of the entity
	 * 
	 * @throws ProcGenException
	 */
	public boolean addInput(String inputName, int signalBusWidth)
			throws ProcGenException {

		if (!isSignalPresentInInputByName(inputName)) {
			inputList.add(new SignalBus(inputName, signalBusWidth));
			return true;
		}

		else
			throw new ProcGenException(
					Consts.ExceptionMessages.INPUT_ALREADY_PRESENT);

	}

	public boolean addOutput(String outputName, int signalBusWidth)
			throws ProcGenException {
		if (!isSignalPresentInOutputByName(outputName)) {
			outputList.add(new SignalBus(outputName, signalBusWidth));
			return true;
		}

		else
			throw new ProcGenException(
					Consts.ExceptionMessages.OUTPUT_ALREADY_PRESENT);

	}

	public SignalBus defaultBehaviour(List<SignalBus> inputList)
			throws ProcGenException {

		return null;
	}

	/*
	 * This method should only be used by entity manager Any change to the
	 * parent entity Id will automatically update all the children entity ids
	 */
	protected boolean changeEntityId(String newId) {
		this.id = newId;
		int idCount = 1;
		for (Entity componentEntity : this.entityList) {
			componentEntity.changeEntityId(newId + "-"
					+ String.valueOf(idCount));
			idCount++;
		}
		return true;
	}

	public void addChildEntity(Entity childEntity) {
		if (childEntity == null)
			return;

		// set current entity as child's parent
		childEntity.parentId = this.id;
		this.entityList.add(childEntity);
	}
	
	public List<SignalBus> getOutputPortList(){
		return this.outputList;
	}
	
	public List<SignalBus> getInputPortList(){
		return this.inputList;
	}
	
	public boolean isSignalPresentInInputByName(String name){
		for(SignalBus inputSignal : inputList){
			if(inputSignal.getName() == name){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isSignalPresentInOutputByName(String name){
		for(SignalBus outputSignal : outputList){
			if(outputSignal.getName() == name){
				return true;
			}
		}
		
		return false;
	}

}
