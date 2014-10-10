/**
 * 
 */
package hdl.translator.logic;

import hdl.translator.logic.HdlConsts.HdlConversionType;
import helper.Consts;
import helper.ProcGenException;
import helper.UnconnectedPortException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.upb.hni.vmagic.AssociationElement;
import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.builtin.StdLogic1164;
import de.upb.hni.vmagic.concurrent.ComponentInstantiation;
import de.upb.hni.vmagic.concurrent.ConcurrentStatement;
import de.upb.hni.vmagic.concurrent.ConditionalSignalAssignment;
import de.upb.hni.vmagic.declaration.Component;
import de.upb.hni.vmagic.declaration.SignalDeclaration;
import de.upb.hni.vmagic.libraryunit.Architecture;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.libraryunit.LibraryClause;
import de.upb.hni.vmagic.libraryunit.LibraryUnit;
import de.upb.hni.vmagic.libraryunit.UseClause;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.VhdlObjectProvider;
import de.upb.hni.vmagic.output.VhdlOutput;
import de.upb.hni.vmagic.util.VhdlCollections;
import electronics.logic.helper.Connection;
import electronics.logic.helper.ConnectionManager;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBus;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class ElectronicsToVhdlConverter {
	
	// This keeps track of whether ports have been connected or left open
	private HashMap<String,Boolean> unconnectedPortTracker = new HashMap<String,Boolean>();
	HdlConverter entityToVhdlConverter  = new EntityToVhdlConverter();
	Project project;
	
	public ElectronicsToVhdlConverter(Project project){
		this.project = project;
	}
	
	public void convertProjectToVhdl(Project project,String outputDirectory) throws IOException, ProcGenException {
		VhdlFile outputFile = createVhdlFileForBaseProject(project, outputDirectory);
		String fileName = outputDirectory + "\\" + project.getName() + ".vhd";
		VhdlOutput.toFile(outputFile, fileName);
	}

	/*
	 * Adds vhdl header elements
	 * 
	 */
	private VhdlFile createBasicVhdlFile() {
		VhdlFile file = new VhdlFile();

		// Add Elements
		file.getElements().add(new LibraryClause("IEEE"));
		file.getElements().add(StdLogic1164.USE_CLAUSE);
		file.getElements().add(new UseClause("ieee.std_logic_unsigned.all"));

		return file;
	}

	/*
	 * This method converts the project into an entity
	 */
	private VhdlFile createVhdlFileForBaseProject(Project project,
			String outputFolderPath) throws IOException, ProcGenException {

		VhdlFile file = this.createBasicVhdlFile();
		Entity hostProjectEntity = createBaseProject(project);
		file.getElements().add(hostProjectEntity);
		file.getElements().add(
				createProjectArchitecture(hostProjectEntity, project,
						outputFolderPath));
		return file;
	}

	private Entity createVMagicEntityForProgenEntity(
			electronics.logic.helper.Entity entityToConvert) {
		Entity entity = new Entity(entityToConvert.getName() + "_"
				+ entityToConvert.getIdForHdlCode());
		for (SignalBus inputSignal : entityToConvert.getInputPortList()) {
			Signal input = null;
			if (inputSignal.getBusWidth() > 1) {
				input = new Signal(
						entityToConvert.getName() + "_" + entityToConvert.getIdForHdlCode() + "_"  + inputSignal.getName(),
						Signal.Mode.IN,
						StdLogic1164.STD_LOGIC_VECTOR(inputSignal.getBusWidth()));
			} else if (inputSignal.getBusWidth() == 1) {
				input = new Signal(entityToConvert.getName() + "_" + entityToConvert.getIdForHdlCode() + "_" + inputSignal.getName(), Signal.Mode.IN,
						StdLogic1164.STD_LOGIC);
			}

			if (input != null) {
				entity.getPort().add(input);
			}
		}
		
		for (SignalBus outputSignal : entityToConvert.getOutputPortList()) {
			Signal output = null;
			if (outputSignal.getBusWidth() > 1) {
				output = new Signal(
						entityToConvert.getName() + "_" + entityToConvert.getIdForHdlCode() + "_" + outputSignal.getName(),
						Signal.Mode.OUT,
						StdLogic1164.STD_LOGIC_VECTOR(outputSignal.getBusWidth()));
			} else if (outputSignal.getBusWidth() == 1) {
				output = new Signal(entityToConvert.getName() + "_" + entityToConvert.getIdForHdlCode() + "_" + outputSignal.getName(), Signal.Mode.OUT,
						StdLogic1164.STD_LOGIC);
			}

			if (output != null) {
				entity.getPort().add(output);
			}
		}

		
		return entity;
	}

	/*
	 * creates the architecture for the entity representing entire project
	 */
	private LibraryUnit createProjectArchitecture(Entity entity,
			Project project, String outputFolderPath) throws IOException, ProcGenException {

		EntityManager em = project.getEntityManager();
		Architecture architecture = new Architecture("rtl", entity);

		List<electronics.logic.helper.Entity> baseEntitiesList = em
				.getBaseEntities();
		
		for (electronics.logic.helper.Entity baseEntity : baseEntitiesList) {
			// for entities that are wanted in a separate file -component declarations needed
			convertEntityToVhdl(outputFolderPath, architecture,
					entityToVhdlConverter, baseEntity);
			
			convertAllConnectionsInHomeProject(project, architecture, baseEntity);
			
		}

		return architecture;
	}

	public void convertEntityToVhdl(String outputFolderPath,
			Architecture architecture, HdlConverter entityToVhdlConverter,
			electronics.logic.helper.Entity baseEntity) throws IOException, ProcGenException {
		
		// create a new file if the type is Separate Entity
		
		if(baseEntity.getHdlConversionType().equals(HdlConversionType.SeparateEntity))
		{
			// recursive function that creates vhdl file for all sub-entities
			createVhdlFileForEntity(baseEntity, outputFolderPath);
			Entity vMagicEntity = this
					.createVMagicEntityForProgenEntity(baseEntity);
			Component componentDeclaration = new Component(vMagicEntity);

			// component instantiations
			ComponentInstantiation compInstance = new ComponentInstantiation(
					componentDeclaration.getIdentifier(), componentDeclaration);
			
			for (VhdlObjectProvider<Signal> sProvider : compInstance.getComponent().getPort()) {
	            List<Signal> signals = VhdlCollections.getAll(sProvider.getVhdlObjects(), Signal.class);
	            for (Signal s : signals) {
	                compInstance.getPortMap().add(new AssociationElement(s.getIdentifier(), s));
	            }

	        }
			
			architecture.getDeclarations().add(componentDeclaration);
			architecture.getStatements().add(compInstance);
		}
		
		// Write in the same file if the type is Inline conversion
		
		else if(baseEntity.getHdlConversionType().equals(HdlConversionType.InlineConversion)){
			
			Object elementToAdd= baseEntity.convertToHdl(entityToVhdlConverter);
			
			architecture.getStatements().add((ConcurrentStatement) elementToAdd);
		}
		
		for(SignalBus port: baseEntity.getInputPortList()){
			String signalName = baseEntity.getName() + "_" + baseEntity.getIdForHdlCode() + "_" + port.getName();
			Signal signal = null;
			if(port.getBusWidth() > 1){
				signal = new Signal(signalName,StdLogic1164.STD_LOGIC_VECTOR(port.getBusWidth()));
			}
			
			else{
				signal = new Signal(signalName,StdLogic1164.STD_LOGIC);
			}
			SignalDeclaration newSignalDeclarationToAdd = new SignalDeclaration(signal);
			architecture.getDeclarations().add(newSignalDeclarationToAdd);
		}
		
		for(SignalBus port: baseEntity.getOutputPortList()){
			String signalName = baseEntity.getName() + "_" + baseEntity.getIdForHdlCode() + "_" + port.getName();
			Signal signal = null;
			if(port.getBusWidth() > 1){
				signal = new Signal(signalName,StdLogic1164.STD_LOGIC_VECTOR(port.getBusWidth()));
			}
			
			else{
				signal = new Signal(signalName,StdLogic1164.STD_LOGIC);
			}
			SignalDeclaration newSignalDeclarationToAdd = new SignalDeclaration(signal);
			architecture.getDeclarations().add(newSignalDeclarationToAdd);
		}
	}

	public void convertAllConnectionsInHomeProject(Project project,
			Architecture architecture,
			electronics.logic.helper.Entity baseEntity) throws ProcGenException {
		// convert connection to signal assignment
		ConnectionManager cm = project.getConnectionManager();
		List<Connection> allConnectionsList = cm.getAllConnectionsInEntityAsList(baseEntity.getId());
		List<ConditionalSignalAssignment> vhdlStatementsList = convertConnectionToVhdl(allConnectionsList,project);
		for(ConditionalSignalAssignment assignment: vhdlStatementsList){
			architecture.getStatements().add(assignment);
		}
	}

	private List<ConditionalSignalAssignment> convertConnectionToVhdl(List<Connection> allConnectionsList,Project project) throws ProcGenException {
	
		EntityManager em = project.getEntityManager();
		List<ConditionalSignalAssignment> assignmentList = new ArrayList<ConditionalSignalAssignment>();
		for(Connection connectionToConvert: allConnectionsList){
			
			electronics.logic.helper.Entity sourceEntity = em.getEntityById(connectionToConvert.getSourceEntityId());
			electronics.logic.helper.Entity destinationEntity = em.getEntityById(connectionToConvert.getDestinationEntityId());

			if(sourceEntity == null){
				throw new ProcGenException(Consts.ExceptionMessages.ENTITY_NOT_FOUND + connectionToConvert.getSourceEntityId());
			}
			
			if(destinationEntity == null){
				throw new ProcGenException(Consts.ExceptionMessages.ENTITY_NOT_FOUND + connectionToConvert.getDestinationEntityId());
			}
			
			
			String sourceSignalName = sourceEntity.getName() + "_"  + connectionToConvert.getSourceEntityId().replace("-", "_") + "_" + connectionToConvert.getInputSignal().getName();
			String destinationSignalName = destinationEntity.getName() + "_" + connectionToConvert.getDestinationEntityId().replace("-", "_") + "_" + connectionToConvert.getOutputSignal().getName();
			
			Signal sourceSignal = new Signal(sourceSignalName,null);
			Signal destinationSignal = new Signal(destinationSignalName,null);
			
			ConditionalSignalAssignment signalAssignment = new ConditionalSignalAssignment(
					destinationSignal,sourceSignal);
			assignmentList.add(signalAssignment);
			VhdlOutput.print(signalAssignment);
			
			// update that the port has been connected
			unconnectedPortTracker.put(connectionToConvert.getSourceEntityId() + "-" + connectionToConvert.getInputSignal().getName(), false);
			unconnectedPortTracker.put(connectionToConvert.getDestinationEntityId() + "-" + connectionToConvert.getOutputSignal().getName(), false);
		}
		
		return assignmentList;
		
	}

	private void createVhdlFileForEntity(
			electronics.logic.helper.Entity entityToConvert, String outputFolderPath) throws IOException, ProcGenException {

		VhdlFile file = this.createBasicVhdlFile();
		Entity vMagicEntity = this
				.createVMagicEntityForProgenEntity(entityToConvert);
		file.getElements().add(vMagicEntity);
		// file.getElements().add(createArchitecture(hostProjectEntity,project,outputFolderPath));

		Architecture architecture = new Architecture("rtl", vMagicEntity);
		for (electronics.logic.helper.Entity childEntity:entityToConvert.getChildEntityList()){

			convertEntityToVhdl(outputFolderPath, architecture,
					entityToVhdlConverter, childEntity);
		}
		
		convertAllConnectionsInEntityToVhdl(entityToConvert,architecture);
		file.getElements().add(architecture);
		
		try{
			// check whether all ports have been connected and resolve them if they are not connected
			resolveUnconnectedSignals(entityToConvert);
		}
		
		catch(UnconnectedPortException e){
			List<String> unconnectedPortNames = e.getUnconnectedPortNames();
			// TODO : ask the user interface to handle the problem
		}
		
		String fileName = outputFolderPath + "\\" + entityToConvert.getName() + ".vhd";
		VhdlOutput.toFile(file, fileName);

	}


	private void convertAllConnectionsInEntityToVhdl(
			electronics.logic.helper.Entity entityToConvert,
			Architecture architecture) throws ProcGenException {
		
		ConnectionManager cm = entityToConvert.getEntityConnectionManager();
		List<Connection> allConnectionsList = cm.getAllConnectionsInEntityAsList(entityToConvert.getId());
		
		// add connections details about children
		for(electronics.logic.helper.Entity childEntity: entityToConvert.getChildEntityList()){
			allConnectionsList.addAll(cm.getAllConnectionsInEntityAsList(childEntity.getId()));
		}
		
		List<ConditionalSignalAssignment> vhdlStatementsList = convertConnectionToVhdl(allConnectionsList,project);
		for(ConditionalSignalAssignment assignment: vhdlStatementsList){
			architecture.getStatements().add(assignment);
		}

		
	}

	private Entity createBaseProject(Project project) {

		Entity entity = new Entity(project.getName());
		final Signal clk = new Signal("CLK", Signal.Mode.IN,
				StdLogic1164.STD_LOGIC);
		entity.getPort().add(clk);
		return entity;
	}

	private void resolveUnconnectedSignals(electronics.logic.helper.Entity entityToCheck) throws ProcGenException{
		
		assert(entityToCheck != null);
		List<String> unconnectedPortNames = new ArrayList<String>();
		
		for(String portName :entityToCheck.getAllPortsName()){
			if(unconnectedPortTracker.containsKey(entityToCheck.getId() + "-" + portName));
				boolean isPortConnected = true;
			if(!isPortConnected){
				unconnectedPortNames.add(portName);
			}
		}
		
		if(unconnectedPortNames.size() > 0){
			throw new UnconnectedPortException(unconnectedPortNames.size() + Consts.ExceptionMessages.UNCONNECTED_INPUT_FOUND_IN + entityToCheck.getName(),unconnectedPortNames);
		}
	}
}
