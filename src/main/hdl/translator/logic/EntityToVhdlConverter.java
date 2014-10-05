/**
 * 
 */
package hdl.translator.logic;

import de.upb.hni.vmagic.VhdlElement;
import de.upb.hni.vmagic.concurrent.ConditionalSignalAssignment;
import de.upb.hni.vmagic.expression.And;
import de.upb.hni.vmagic.expression.Or;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.output.VhdlOutput;
import electronics.logic.entities.AndGate;
import electronics.logic.entities.Multiplexer;
import electronics.logic.entities.OrGate;
import electronics.logic.entities.Register;
import electronics.logic.helper.SignalBus;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class EntityToVhdlConverter implements HdlConverter {

	@Override
	public Object convertAndGate(AndGate andGate) {

		SignalBus input1 = andGate.getInputPortList().get(0);
		SignalBus input2 = andGate.getInputPortList().get(1);
		SignalBus output1 = andGate.getOutputPortList().get(0);

		String input1Name = andGate.getName() + andGate.getId() + "_" + input1.getName();
		String input2Name = andGate.getName() + andGate.getId() + "_" + input2.getName();
		String outputName = andGate.getName() + andGate.getId() + "_" + output1.getName();

		Signal andInput1 = new Signal(input1Name, null);
		Signal andInput2 = new Signal(input2Name, null);
		Signal andOutput1 = new Signal(outputName, null);

		And andStatement = new And(andInput1, andInput2);

		ConditionalSignalAssignment andGateAssignment = new ConditionalSignalAssignment(
				andOutput1, andStatement);
		VhdlOutput.print(andGateAssignment);
		return andGateAssignment;
	}

	@Override
	public VhdlElement convertMux(Multiplexer multiplexer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object convertOrGate(OrGate orGate) {

		SignalBus input1 = orGate.getInputPortList().get(0);
		SignalBus input2 = orGate.getInputPortList().get(1);
		SignalBus output1 = orGate.getOutputPortList().get(0);

		String input1Name = orGate.getName() + orGate.getId() + "_" + input1.getName();
		String input2Name = orGate.getName() + orGate.getId() + "_" + input2.getName();
		String outputName = orGate.getName() + orGate.getId() + "_" + output1.getName();

		Signal orInput1 = new Signal(input1Name, null);
		Signal orInput2 = new Signal(input2Name, null);
		Signal orOutput1 = new Signal(outputName, null);

		Or orStatement = new Or(orInput1, orInput2);

		ConditionalSignalAssignment andGateAssignment = new ConditionalSignalAssignment(
				orOutput1, orStatement);
		VhdlOutput.print(andGateAssignment);
		return andGateAssignment;
	}

	@Override
	public VhdlElement convertRegister(Register register) {
		// TODO Auto-generated method stub
		return null;
	}
}
