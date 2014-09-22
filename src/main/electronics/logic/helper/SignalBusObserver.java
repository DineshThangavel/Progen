/**
 * 
 */
package electronics.logic.helper;

import helper.Consts;
import helper.ProcGenException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import electronics.logic.simulation.EntitySimulator;
import electronics.logic.simulation.ProjectSimulator;

// based on observer pattern from http://www.vogella.com/tutorials/DesignPatternObserver/article.html
/**
 * @author DINESH THANGAVEL
 *
 */
public class SignalBusObserver implements PropertyChangeListener{
	
	ProjectSimulator hostSimulator; // signal observer keeps track of hostSimulator so as to synchronize time with the simulator
	List<SignalValueRecord> signalLogValues = new ArrayList<SignalValueRecord>();
	
	List<EntitySimulator> entitySimulatorListeners = new ArrayList<EntitySimulator>();
	
	public SignalBusObserver(SignalBus signalBusToObserve,ProjectSimulator hostSimulator) {
		this.hostSimulator = hostSimulator;
		signalBusToObserve.addChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		float currentTime = hostSimulator.getCurrentTime();
		Signal[] oldValue = (Signal[])evt.getOldValue();
		Signal[] newValue = (Signal[])evt.getNewValue();
		
		if(!Arrays.deepEquals(oldValue, newValue)){
		this.signalLogValues.add(new SignalValueRecord(currentTime,oldValue,newValue));
		
		for(EntitySimulator eSim:entitySimulatorListeners ){
			try {
				eSim.processInputChange(evt);
			} catch (ProcGenException e) {
				e.printStackTrace();
				// TODO: Change this to log message
				System.out.println(Consts.CommandResults.ERROR_SIM_NOT_SUCCESSFUL);
			}
		}
		}
	}
	
	public Signal[] getLatestValueOfSignal(){
		int lastIndexPosition = this.signalLogValues.size()-1;
		SignalValueRecord latestRecord = this.signalLogValues.get(lastIndexPosition);
		return latestRecord.currentValue;
	}
	
	public void printSignalLogToConsole(){
		for(SignalValueRecord signalRecord:signalLogValues){
			System.out.println(signalRecord.time + " " + signalRecord.getDisplayStringForCurrentValue());
		}
	}
	
	public void addEntitySimulatorListener(EntitySimulator eSim){
		this.entitySimulatorListeners.add(eSim);
	}
	
	/*
	 * Do not alter the log values, it is meant to be used as a getter only.
	 * Currently being used for unit-testing
	 */
	public List<SignalValueRecord> getSignalValueRecords(){
		return this.signalLogValues;
	}
}
