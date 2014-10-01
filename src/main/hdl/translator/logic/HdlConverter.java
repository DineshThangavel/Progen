/*This interface is meant to be used for converting Progen Entity to any HDL*
 * 
 */
package hdl.translator.logic;

import electronics.logic.entities.*;

/**
 * @author DINESH THANGAVEL
 *
 */
public interface HdlConverter {

	Object convertAndGate(AndGate andGate);

	Object convertMux(Multiplexer multiplexer);

	Object convertOrGate(OrGate orGate);

	Object convertRegister(Register register);

}
