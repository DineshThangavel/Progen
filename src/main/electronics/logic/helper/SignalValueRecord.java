/**
 * 
 */
package electronics.logic.helper;

import java.util.Arrays;

/**
 * @author DINESH THANGAVEL
 *
 */
public class SignalValueRecord{
	float time;
	Signal[] oldValue;
	Signal[] currentValue;
	
	public SignalValueRecord(float currentTime,Signal[] oldValue,Signal[] currentValue){
		
		assert(oldValue.length == currentValue.length);
		int widthOfBus = oldValue.length;
		
		this.time = currentTime;
		this.oldValue = new Signal[widthOfBus];
		this.currentValue = new Signal[widthOfBus];
		
		for(int i=0;i<widthOfBus;i++){
			this.oldValue[i] = oldValue[i];
			this.currentValue[i] = currentValue[i];
		}
	}
	
	public float getTimeOfRecord(){
		return this.time;
	}
	
	public String getDisplayStringForCurrentValue(){
		StringBuilder currentDisplay = new StringBuilder();
		for(int i=0;i<currentValue.length;i++){
			if(currentValue[i].equals(Signal.HIGH)){
				currentDisplay.append("H");
			}
			else if(currentValue[i].equals(Signal.LOW)){
				currentDisplay.append("L");
			}
			
			else{
				currentDisplay.append("Z");
			}
		}
		
		return currentDisplay.toString();
	}
	
	public String getDisplayStringForOldValue(){
		StringBuilder oldDisplay = new StringBuilder();
		for(int i=0;i<oldValue.length;i++){
			if(oldValue[i].equals(Signal.HIGH)){
				oldDisplay.append("H");
			}
			else if(currentValue[i].equals(Signal.LOW)){
				oldDisplay.append("L");
			}
			
			else{
				oldDisplay.append("Z");
			}
		}
		
		return oldDisplay.toString();
	}

	/*
	 * This method just compares the values of the signal in terms of high and low
	 * and does not check whether the signal buses are the same
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	@Override
	public boolean equals(Object obj) {
		SignalValueRecord other = (SignalValueRecord) obj;
		if (Float.floatToIntBits(time) != Float
				.floatToIntBits(other.time))
			return false;
		
		if (!Arrays.equals(currentValue, other.currentValue))
			return false;
		if (!Arrays.equals(oldValue, other.oldValue))
			return false;
		return true;
	}
	
	
}