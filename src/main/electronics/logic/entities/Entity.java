/**
 *	Represents an Electronic entity
 *	 An enitty can be the topmost entity in a project (called baseEntity) or within other entities
 */
package electronics.logic.entities;

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
	 * should be taken care of when the entity is being created
	 */

	private String id;
	private String name;
	private String parentId;
	private List<SignalBus> inputList = new ArrayList<SignalBus>();
	private List<SignalBus> outputList = new ArrayList<SignalBus>();
	private List<Entity> entityList = new ArrayList<Entity>();

	public Entity(String id, String name) {

		this.id = id;
		this.name = name;
		this.parentId = null;
	}
	
	/*
	 *  parentId decides if the entity is base entity
	 *  For base entity parentId is null and this is assumed by default  
	 */
	public Entity(String id, String name,String parentId){
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
	 */
	public boolean addInput(String inputName, int signalBusWidth) {

		if (!inputList.contains(inputName)) {
			inputList.add(new SignalBus(inputName, signalBusWidth));
			return true;
		}

		// TODO throw exception for it
		else
			System.out.println("Input already present..");
		return false;

	}

	public boolean addOutput(String outputName, int signalBusWidth) {
		if (!outputList.contains(outputName)) {
			outputList.add(new SignalBus(outputName, signalBusWidth));
			return true;
		}

		// TODO throw exception for it
		else
			System.out.println("Output already present..");
		return false;

	}

	public boolean makeInvisible(Signal Input) {
		return true;
	}

	public SignalBus defaultBehaviour(List<SignalBus> inputList) {

		return null;
	}

}
