/**
 * 
 */
package electronics.logic.helper;

/**
 * @author DINESH THANGAVEL
 *
 */

/*
 * A connection is associated with 1 signalbus only
 * A connection can map an input directly to an output
 * or can have others ways of connecting such as only three
 * wires of the input signal bus is mapped to the output 
 * signal bus 
 */
public class Connection {
	String sourceEntityId;
	String destinationEntityId;
	SignalBus inputSignal;
	SignalBus outputSignal;
	
	/*
	 * This is the attribute that decides if there is splitting,
	 * sign-extension etc in the connections or if it is direct one to one mapping.
	 */
	SignalBus inputOutputMapping;
	
	public Connection(String sourceEntityId,String destinationEntityId,SignalBus inputSignal,SignalBus outputSignal, ConnectionType connectType){
		this.sourceEntityId = sourceEntityId;
		this.destinationEntityId = destinationEntityId;
		this.inputSignal = inputSignal;
		this.outputSignal = outputSignal;
		this.inputOutputMapping = getMappingAsSignalFromType(connectType);
	}
	
	private Connection(){
		
	}

	/*
	 * This method identifies the input-output mapping
	 * eg. if it is Direct connection, then the input is directly connected to output. 
	 * The indices of inputOutputMapping correspond to the input signal  while 
	 * the value at the index refers to the output signal to which it is connected.
	 */
	private SignalBus getMappingAsSignalFromType(ConnectionType connectType) {
		if(connectType == ConnectionType.DIRECT_CONNECTION)
			return outputSignal;
		
		// TODO  What to do with default type ?
			return null;
	}
	
	public Connection deepCopy(){
		Connection newConnectionCopy = new Connection();
		newConnectionCopy.destinationEntityId = this.destinationEntityId;
		newConnectionCopy.sourceEntityId = this.sourceEntityId;
		newConnectionCopy.inputSignal = this.inputSignal.deepCopy();
		newConnectionCopy.outputSignal = this.outputSignal.deepCopy();
		newConnectionCopy.inputOutputMapping = this.inputOutputMapping.deepCopy();
	
		return newConnectionCopy;
	}

	public String getSourceEntityId() {
		return sourceEntityId;
	}

	public String getDestinationEntityId() {
		return destinationEntityId;
	}

	public SignalBus getInputSignal() {
		return inputSignal.deepCopy();
	}

	public SignalBus getOutputSignal() {
		return outputSignal.deepCopy();
	}

	public SignalBus getInputOutputMapping() {
		return inputOutputMapping.deepCopy();
	}
}
