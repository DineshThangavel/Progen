/**
 *This class start the flow of the program 
 */
package electronics.logic.simulation;

import helper.ProcGenException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import logic.LogicFacade;
import electronics.logic.helper.Project;
import electronics.logic.helper.Signal;
import electronics.logic.helper.SignalBus;
import electronics.logic.helper.SignalBusObserver;

/**
 * @author DINESH THANGAVEL
 *
 */
public class ProjectSimulator extends Simulator{
	
	Project hostProject = null;
	SignalBus projectClock = new SignalBus("PrjClk",1); //main clock generated by the crystal. It is modified by project simulator
	SignalBus risingClockEdge = new SignalBus("RisingClkEdge",1);
	SignalBus fallingClockEdge = new SignalBus("FallingClkEdge",1);
	
	float currentTime; // maintains the sense of time across all units
	List<Float> debugPointsInTime = new ArrayList<Float>();
	List<CircuitStimulus> inputCommandTreeForSimulation = new ArrayList<CircuitStimulus>();
	LogicFacade logicInterface = new LogicFacade();
	
	// key is name of the inputSimulator
	HashMap<String,SignalBus> inputSimulationConnectionDirectory = new HashMap<String,SignalBus>();
	HashMap<String,InputSimulator> inputSimulationDirectory = new HashMap<String,InputSimulator>();
	
	HashMap<String,SignalBus> signalsConnectedToProjectClk = new HashMap<String,SignalBus>();
	
	private HashMap<SignalBus,SignalBusObserver> signalObserverList = new HashMap<SignalBus,SignalBusObserver>();
	
	public HashMap<SignalBus,SignalBusObserver> getSignalObserverMap() {
		return signalObserverList;
	}

	public ProjectSimulator(Project hostProject){
		this.hostProject = hostProject;
	}
	
	public void runSimulation(float timeToSimulate, float clockPeriod, boolean isDebugEnabled, boolean isAutomaticInputChangeEnabled) throws ProcGenException{
		currentTime = 0; // starting the simulation at time = 0
		projectClock.setValue(Signal.HIGH);
		
		int debugTimeIndex = 0;
		Iterator<CircuitStimulus> inputStimulusIterator = inputCommandTreeForSimulation.iterator();
		
		CircuitStimulus nextStimulus = null;
		if(inputCommandTreeForSimulation.size() >0 ){
			nextStimulus = (CircuitStimulus) inputStimulusIterator.next();
		}
		
		while(currentTime < timeToSimulate){
			
			togglePrjClk();
			
			float nextTickTime = currentTime + clockPeriod/2;
			
			if(isDebugEnabled){
				float debugTime = debugPointsInTime.get(debugTimeIndex);
				if(debugTime < currentTime &&  debugTime > nextTickTime){
					// receive command from user
				}
			}
			
			if(isAutomaticInputChangeEnabled){
				while(true){
				if(nextStimulus != null && nextStimulus.time >= currentTime && nextStimulus.time< nextTickTime){
					
					currentTime = nextStimulus.time;
						// clear the event change signals
						fallingClockEdge.setValue(Signal.LOW);
						risingClockEdge.setValue(Signal.LOW);
						
						logicInterface.processInput(nextStimulus.commandToExecuteAtTime);
						
						if(inputStimulusIterator.hasNext()){
							nextStimulus = (CircuitStimulus) inputStimulusIterator
								.next();
						}
						else{
							break;
						}
				}
				
				else{
					break;
				}
			}
			}

			currentTime =nextTickTime ;
		} 		
	}

	public void togglePrjClk() {
		assert(projectClock.getValue().length == 1);
		if(projectClock.getValue()[0].equals(Signal.HIGH)){
			projectClock.setValue(Signal.LOW);
			fallingClockEdge.setValue(Signal.HIGH);
		}
		
		else{
			projectClock.setValue(Signal.HIGH);
			risingClockEdge.setValue(Signal.HIGH);
		}
		
	}

	/*
	 * @input
	 */
	public void addInputSimulator(InputSimulator newInputSimulator, SignalBus signalBusToSimulate) {
	
			this.inputSimulationConnectionDirectory.put(newInputSimulator.getName(),signalBusToSimulate);
			this.inputSimulationDirectory.put(newInputSimulator.getName(), newInputSimulator);
	}
	
	public SignalBus getRisingEdgeTriggerSignal(){
		return this.risingClockEdge;
	}
	
	public SignalBus getFallingEdgeTriggerSignal(){
		return this.fallingClockEdge;
	}

	public float getCurrentTime() {
		return this.currentTime;
	}
	
	public SignalBus getSignalBusConnectedToInputSimulator(String inputSimulatorName){
		if(this.inputSimulationConnectionDirectory.containsKey(inputSimulatorName)){
			return this.inputSimulationConnectionDirectory.get(inputSimulatorName);
		}
		return null;
	}

	public InputSimulator getInputSimulator(String nameOfInputSimulator) {
		return this.inputSimulationDirectory.get(nameOfInputSimulator);
	}
	
	public void addInputStimulus(CircuitStimulus circuitStimulusToAdd){
		this.inputCommandTreeForSimulation.add(circuitStimulusToAdd);
	}
}