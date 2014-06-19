/**
 * 
 */
package userinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import helper.Consts.ConsoleUIConstants;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class ConsoleUI {

	public void printEnterCommand() {
		System.out.println(ConsoleUIConstants.PROMPT_ENTER_COMMAND);
	}

	static public String readConsoleInput() throws IOException {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		return bufferRead.readLine();
	}
	
	static public void printMessage(String message){
		System.out.println(message);
	}
}
