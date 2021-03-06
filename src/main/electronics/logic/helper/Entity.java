/**
 *	Represents an Electronic entity
 *	 An enitty can be the topmost entity in a project (called baseEntity) or within other entities
 */
package electronics.logic.helper;

import hdl.translator.logic.HdlConsts.HdlConversionType;
import hdl.translator.logic.HdlConverter;
import helper.Consts;
import helper.ProcGenException;

import java.util.ArrayList;
import java.util.List;

import electronics.logic.simulation.EntitySimulator;

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
	private Entity parentEntity;
	private EntityTriggerType entityTriggerType;
	
	private List<SignalBus> inputList = new ArrayList<SignalBus>();
	private List<SignalBus> outputList = new ArrayList<SignalBus>();
	private List<Entity> entityList = new ArrayList<Entity>();
	
	// any change to the child entities connection is done through events while changes in the signal of current entity is done in addInput and addOutput directly
	private EntityConnectionManager entityConnectionManager = new EntityConnectionManager(this);
	private EntitySimulator entitySim = new EntitySimulator(this);
	
	// this attribute keeps track of whether the entity should be converted to a standalone entity
	private HdlConversionType hdlConversionType;

	// This type of entity can be created and no valid id is present
	public Entity(String name){
		this.id = "";
		this.name = name;
		this.parentEntity = null;
		this.hdlConversionType = HdlConversionType.SeparateEntity;
	}
	
	// Entity which have id are only assigned by entity manager
	protected Entity(String id, String name) {

		this.id = id;
		this.name = name;
		this.parentEntity = null;
		this.hdlConversionType = HdlConversionType.SeparateEntity;
	}

	// Deep Copy Constructor method for entity
	public Entity deepCopy() {
		Entity newCopyEntity = new Entity(this.id,this.name);
		newCopyEntity.parentEntity = this.parentEntity;
		newCopyEntity.name = this.name;
		newCopyEntity.id = this.id;
		
		for(Entity subEntity:this.entityList){
			Entity newSubEntityCopy = subEntity.deepCopy();
			newCopyEntity.entityList.add(newSubEntityCopy);
		}
		
		for(SignalBus input :this.inputList){
			newCopyEntity.inputList.add(input.deepCopy());
		}

		for(SignalBus output :this.outputList){
			newCopyEntity.outputList.add(output.deepCopy());
		}
		
		newCopyEntity.entityConnectionManager = this.entityConnectionManager.deepCopy(newCopyEntity);
		return newCopyEntity;
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
	
	public String getIdForHdlCode(){
		return this.id.replace("-", "_");
	}
	
	public String getName() {
		return this.name;
	}
	
	public Entity getParent() {
		return this.parentEntity;
	}
	
	public EntityConnectionManager getEntityConnectionManager(){
		return this.entityConnectionManager;
	}

	/**
	 * This method returns a reference to the actual input signal bus
	 * 
	 */
	public SignalBus getInputByName(String name) {
		SignalBus signalToReturn = null;

		for (SignalBus input : inputList) {
			if (input.getName().equals(name) && name != "") {
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
			if (output.getName().equals(name) && name != "") {
				signalToReturn = output;

			}
		}

		return signalToReturn;
	}
	
	public HdlConversionType getHdlConversionType() {
		return hdlConversionType;
	}

	public void setHdlConversionType(HdlConversionType hdlConversionType) {
		this.hdlConversionType = hdlConversionType;
	}

	/**
	 * Adds an input to the input list of the entity
	 * This should be used only during creation of new entity outside a Progen Project.
	 * Inside a ProgenProject use EntityManager's addInput.
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

	/*
	 * This should be used only during creation of new entity outside a Progen Project.
	 * Inside a ProgenProject use EntityManager's addOutput.
	 */
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


	public void defaultBehaviour()
			throws ProcGenException {

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

	/*
	 * This should be used only during creation of new child entity outside a Progen Project.
	 * Inside a ProgenProject use EntityManager's addEntity.
	 */
	public void addChildEntity(String childEntityId, Entity childEntity) {
		if (childEntity == null)
			return;

		// set current entity as child's parent
		childEntity.parentEntity = this;
		childEntity.id = childEntityId;
		this.entityList.add(childEntity);
	}
	
	public List<Entity> getChildEntityList(){
		return this.entityList;
	}
	
	public List<SignalBus> getOutputPortList(){
		return this.outputList;
	}
	
	public List<SignalBus> getInputPortList(){
		return this.inputList;
	}
	
	public List<String> getAllPortsName(){
		List<String> allPortsNameList = new ArrayList<String>();
		
		List<SignalBus> inputSignalList = this.getInputPortList();
		List<SignalBus> outputSignalList = this.getOutputPortList();
		
		for(SignalBus input: inputSignalList){
			allPortsNameList.add(input.getName());
		}
		
		for(SignalBus output: outputSignalList){
			allPortsNameList.add(output.getName());
		}
		
		return allPortsNameList;
	}
	
	public List<String> getInputPortNames(){
		List<String> inputPortNamesList = new ArrayList<String>();
		
		List<SignalBus> inputSignalList = this.getInputPortList();
		
		for(SignalBus input: inputSignalList){
			inputPortNamesList.add(input.getName());
		}
		
		return inputPortNamesList;
	}
	
	public List<String> getOutputPortNames(){
		List<String> outputPortNamesList = new ArrayList<String>();
		
		List<SignalBus> outputSignalList = this.getOutputPortList();
		
		for(SignalBus input: outputSignalList){
			outputPortNamesList.add(input.getName());
		}
		
		return outputPortNamesList;
	}
	
	public boolean isSignalPresentInInputByName(String name){
		for(SignalBus inputSignal : inputList){
			if(inputSignal.getName().equals(name)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isSignalPresentInOutputByName(String name){
		for(SignalBus outputSignal : outputList){
			if(outputSignal.getName().equals(name)){
				return true;
			}
		}
		
		return false;
	}
	
	// TODO: Check non-existent IDs
	public Entity getChildEntityById(String childEntityId){
		String[] entityId = childEntityId.split("-");
		Entity entitySearcher = this;
		
		// Ids start with 1. So need to subtract 1 to make it an index of array.
		for(int i=1;i<entityId.length;i++){
			entitySearcher = entitySearcher.entityList.get(Integer.valueOf(entityId[i])-1);			
		}
		
		return entitySearcher;
	}
	
	public void notifyEntityChangeEvent(EntityChangeEvent entityChangeEvent) throws ProcGenException{
		this.entityConnectionManager.updateAboutEvent(entityChangeEvent);
		}

	public void setParent(Entity parentEntity) {
		this.parentEntity = parentEntity;
		
	}
	
	public EntitySimulator getEntitySimulator(){
		return this.entitySim;
	}
	
	public EntityTriggerType getEntityTriggerType() {
		return entityTriggerType;
	}

	public void setEntityTriggerType(EntityTriggerType entityTriggerType) {
		this.entityTriggerType = entityTriggerType;
	}
	
	public enum EntityTriggerType{
		NON_CLOCKED,
		RISING_EDGE_TRIGGERED,
		FALLING_EDGE_TRIGGERED;
	}

	public Object convertToHdl(HdlConverter hdlConverter){
		return "";
	}
}
