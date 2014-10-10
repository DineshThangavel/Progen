/**
 * 
 */
package hdl.translator.logic;

import hdl.translator.logic.VhdlToElectronicsConverter.VhdlStatementAnalyser.InputOutputPort;
import helper.ProcGenException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.upb.hni.vmagic.AssociationElement;
import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.WaveformElement;
import de.upb.hni.vmagic.concurrent.AbstractProcessStatement;
import de.upb.hni.vmagic.concurrent.ComponentInstantiation;
import de.upb.hni.vmagic.concurrent.ConcurrentStatement;
import de.upb.hni.vmagic.concurrent.ConditionalSignalAssignment;
import de.upb.hni.vmagic.concurrent.ConditionalSignalAssignment.ConditionalWaveformElement;
import de.upb.hni.vmagic.declaration.BlockDeclarativeItem;
import de.upb.hni.vmagic.declaration.Component;
import de.upb.hni.vmagic.declaration.DeclarativeItem;
import de.upb.hni.vmagic.declaration.ProcessDeclarativeItem;
import de.upb.hni.vmagic.expression.Expression;
import de.upb.hni.vmagic.libraryunit.Architecture;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.Variable;
import de.upb.hni.vmagic.parser.VhdlParser;
import de.upb.hni.vmagic.parser.VhdlParserException;
import de.upb.hni.vmagic.statement.SequentialStatement;
import de.upb.hni.vmagic.util.VhdlCollections;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.Project;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class VhdlToElectronicsConverter {

	// keep track of the entity and the list of components
	HashMap<String, List<Component>> entityComponentDependencyMap = new HashMap<String, List<Component>>();
	List<Signal> declaredSignals = null;
	List<Variable> declaredVariables = null;
	List<ComponentInstantiation> componentInstantiation = new ArrayList<ComponentInstantiation>();
	List<AbstractProcessStatement> processStatments = new ArrayList<AbstractProcessStatement>();
	List<ConditionalSignalAssignment> signalAssignmentList = new ArrayList<ConditionalSignalAssignment>();

	HashMap<String, VhdlStatementAnalyser> analyser = new HashMap<String, VhdlStatementAnalyser>();

	// keeps track of the components that are to be resolved
	List<Component> componentsToBeResolved = new ArrayList<Component>();
	HashMap<String, String> entityNameIdMap = new HashMap<String, String>();

	String workSpace;
	Architecture coreEntityArchitecture = null;

	private VhdlFile readVhdlFile(String filePath) throws IOException,
			VhdlParserException {
		VhdlFile inFile = VhdlParser.parseFile(filePath);
		return inFile;
	}

	/*
	 * Convert to Progen entity and add it to project specified in parameter2
	 */
	private electronics.logic.helper.Entity convertToProgen(
			VhdlFile vhdlFileInput, Project progenProject)
			throws ProcGenException {
		List<Entity> entityList = VhdlCollections.getAll(
				vhdlFileInput.getElements(), Entity.class);
		assert (entityList.size() == 1);

		// add the entity
		electronics.logic.helper.Entity newProgenEntity = VMagicProgenConversionAider
				.convertToProgenEntity(entityList.get(0));

		// Connections
		List<Architecture> architectureList = VhdlCollections.getAll(
				vhdlFileInput.getElements(), Architecture.class);
		// assuming only one architecture
		assert (architectureList.size() == 1);
		Architecture newEntityArchitecture = architectureList.get(0);
		processVMagicArchitecture(newEntityArchitecture, newProgenEntity);

		// saving architecture so that it can be reused later
		this.coreEntityArchitecture = newEntityArchitecture;
		return newProgenEntity;
	}

	private void processVMagicArchitecture(Architecture newEntityArchitecture,
			electronics.logic.helper.Entity newProgenEntity)
			throws ProcGenException {

		// identify the registers
		List<BlockDeclarativeItem> declarationList = newEntityArchitecture
				.getDeclarations();
		processDeclarationStatements(declarationList);
	}

	public void processConnectionsInArchitecture(
			Architecture newEntityArchitecture,
			electronics.logic.helper.Entity convertedEntity,
			HashMap<String, electronics.logic.helper.Entity> customLibrary,
			Project activeProject) throws ProcGenException {

		// resolve dependencies on components and update childEntity details
		resolveComponentDepencies(convertedEntity, customLibrary);

		// identify concurrent statements
		List<ConcurrentStatement> concurrentStatements = newEntityArchitecture
				.getStatements();

		processConcurrentStatements(concurrentStatements, convertedEntity);

		decideConnections(activeProject, customLibrary);
	}

	// core logic for making connections
	private void decideConnections(Project activeProject,
			HashMap<String, electronics.logic.helper.Entity> customLibrary)
			throws ProcGenException {
		
		analyseComponentInstantiationStatements(activeProject, customLibrary);
		analyseProcessStatements();
		analyseSignalAssignmentStatements();
	}
	
	private void analyseProcessStatements(){
		for(AbstractProcessStatement statement:processStatments){
			List<ProcessDeclarativeItem> declarationsInProcess = statement.getDeclarations();
			List<Signal> signalsInSensitivityList = statement.getSensitivityList();
			List<SequentialStatement> statementsList = statement.getStatements();
			
			for(SequentialStatement sequentialStatement:statementsList){
				SequentialStatementProcessor statementProcessor = new  SequentialStatementProcessor();
				statementProcessor.visit(sequentialStatement);
			}
		}
	}

	private void analyseComponentInstantiationStatements(Project activeProject,
			HashMap<String, electronics.logic.helper.Entity> customLibrary)
			throws ProcGenException {
		for (ComponentInstantiation statement : this.componentInstantiation) {
			Component componentType = statement.getComponent();

			electronics.logic.helper.Entity customLibraryEntity = customLibrary
					.get(componentType.getIdentifier());
			String entityId = activeProject.getEntityManager().addEntity(
					customLibraryEntity);
			this.entityNameIdMap.put(statement.getLabel(), entityId);

			List<AssociationElement> associationList = statement.getPortMap();

			int indexCount = 0;

			for (AssociationElement association : associationList) {
				Expression<?> actualExpression = association.getActual();
				String formalExpression = association.getFormal();
				if (actualExpression instanceof Signal) {
					String signalName = ((Signal) actualExpression)
							.getIdentifier();
					
					assert(componentType.getPort().get(indexCount).getVhdlObjects().size() == 1);
					String originalPortName = componentType.getPort().get(indexCount).getVhdlObjects().get(0).getIdentifier();
					electronics.logic.helper.Entity entityUnderConsideration = activeProject
							.getEntityManager().getEntityById(entityId);

					if (entityUnderConsideration.getInputByName(originalPortName) !=null) {

						VhdlStatementAnalyser newAnalysisStatement = new VhdlStatementAnalyser();
						
						electronics.logic.helper.SignalBus inputSignalPort =activeProject
								.getEntityManager().getEntityById(entityId).getInputByName(originalPortName);

						
						if (this.analyser.containsKey(signalName)) {
							this.analyser.get(signalName).destination
									.add(new InputOutputPort(entityId,
											inputSignalPort.getName()));

						}

						else {
							newAnalysisStatement.destination
									.add(new InputOutputPort(entityId,
											inputSignalPort.getName()));
							newAnalysisStatement.statementSource = VhdlStatementAnalyser.SourceOfAnalysis.ComponentInstantiation;
							newAnalysisStatement.typeOfStatement = VhdlStatementAnalyser.StatementType.Signal;

							this.analyser.put(signalName, newAnalysisStatement);
						}

					}

					else if (entityUnderConsideration.getOutputByName(originalPortName)!=null) {
						// output of an entity
						electronics.logic.helper.SignalBus outputSignal =activeProject
								.getEntityManager().getEntityById(entityId).getOutputByName(originalPortName);
						
						
						if (this.analyser.containsKey(signalName)) {
							this.analyser.get(signalName).source = new InputOutputPort(
									entityId, outputSignal.getName());
						}

						else {

							VhdlStatementAnalyser newAnalysisStatement = new VhdlStatementAnalyser();

							newAnalysisStatement.source = new InputOutputPort(
									entityId, outputSignal.getName());
							newAnalysisStatement.statementSource = VhdlStatementAnalyser.SourceOfAnalysis.ComponentInstantiation;
							newAnalysisStatement.typeOfStatement = VhdlStatementAnalyser.StatementType.Signal;

							this.analyser.put(signalName, newAnalysisStatement);
						}

					}
					
					indexCount++;
				}

			}
		}
	}

	private void analyseSignalAssignmentStatements(){
		
		for (ConditionalSignalAssignment signalAssignment : this.signalAssignmentList) {
			Signal rhsSignal = null;
			Signal lhsSignal = null;

			List<ConditionalWaveformElement> waveForms = signalAssignment
					.getConditionalWaveforms();

			assert (waveForms.size() == 1);
			List<WaveformElement> testWaveFormList = waveForms.get(0)
					.getWaveform();

			assert (testWaveFormList.size() == 1);
			WaveformElement testWaveForm = testWaveFormList.get(0);
//			System.out.println(testWaveForm.getValue());

			if (testWaveForm.getValue() instanceof Signal) {
				rhsSignal = (Signal) testWaveForm.getValue();
			}

			if (rhsSignal != null){
//				System.out.println("RHS of assignment is "
//						+ rhsSignal.getIdentifier());
			}
			
			if (signalAssignment.getTarget() instanceof Signal) {
				lhsSignal = (Signal) signalAssignment.getTarget();
			}

			if (lhsSignal != null) {
//				System.out.println("LHS of assignment is "
//						+ lhsSignal.getIdentifier());
			}

			if (lhsSignal != null && rhsSignal != null) {
				VhdlStatementAnalyser newAnalysisStatement = new VhdlStatementAnalyser();
				// rhs of the assignment is the signal that connects to this
				// signal
				newAnalysisStatement.source = new InputOutputPort(null,
						rhsSignal.getIdentifier());
				newAnalysisStatement.statementSource = VhdlStatementAnalyser.SourceOfAnalysis.SignalAssignment;
				newAnalysisStatement.typeOfStatement = VhdlStatementAnalyser.StatementType.Signal;

				if (!this.analyser.containsKey(lhsSignal.getIdentifier())) {
					this.analyser.put(lhsSignal.getIdentifier(),
							newAnalysisStatement);
				}

				else {
					VhdlStatementAnalyser analysisStatement = this.analyser.get(lhsSignal.getIdentifier());
					assert(analysisStatement.source==null);
					analysisStatement.source = new InputOutputPort(null,
							rhsSignal.getIdentifier());
					this.analyser.put(lhsSignal.getIdentifier(), analysisStatement);
				}
			}
		}
	}
	
	private void resolveComponentDepencies(
			electronics.logic.helper.Entity newProgenEntity,
			HashMap<String, electronics.logic.helper.Entity> customLibrary)
			throws ProcGenException {
		for (Component componentToResolve : componentsToBeResolved) {
			String nameOfComponent = componentToResolve.getIdentifier();
			String fileName = getFileNameFromWorkspace(nameOfComponent);
			VhdlToElectronicsConverter newConverterInstance = new VhdlToElectronicsConverter();
			electronics.logic.helper.Entity newChildEntity = newConverterInstance
					.convertToProgen(fileName);
			newChildEntity.setParent(newProgenEntity);

			customLibrary.put(newChildEntity.getName(), newChildEntity);
			newConverterInstance.resolveComponentDepencies(newChildEntity,
					customLibrary);
		}
		// TODO: clear the components to be resolved
	}

	private String getFileNameFromWorkspace(String nameOfComponent) {
		if (this.workSpace != null) {
			File directory = new File(this.workSpace);
			String[] fileList = directory.list();
			String fileNameToSearch = nameOfComponent + ".vhd";
			for (String directoryContent : fileList) {
				if (directoryContent.equals(fileNameToSearch)) {
					String fileName = this.workSpace.concat("\\"
							+ fileNameToSearch);
					return fileName;
				}
			}
		}
		return null;
	}

	private void processDeclarationStatements(
			List<BlockDeclarativeItem> declarationList) {

		DeclarationProcessor declarationProcessor = new DeclarationProcessor(
				this);
		for (BlockDeclarativeItem declaration : declarationList) {
			if (declaration instanceof DeclarativeItem) {
				declarationProcessor.visit((DeclarativeItem) declaration);
			}
		}

	}

	private void processConcurrentStatements(
			List<ConcurrentStatement> concurrentStatementList,
			electronics.logic.helper.Entity convertedEntity) {

		ConcurrentStatementProcessor concurrentStatementProcessor = new ConcurrentStatementProcessor(
				this);
		for (ConcurrentStatement concurrentStatement : concurrentStatementList) {
			concurrentStatementProcessor.visit(concurrentStatement);
		}
	}

	public electronics.logic.helper.Entity convertToProgen(String fileName)
			throws ProcGenException {
		VhdlFile vhdFile;
		try {
			setWorkSpaceDetails(fileName);
			vhdFile = this.readVhdlFile(fileName);
			Project activeProject = ElectronicsLogicFacade.getInstance()
					.getActivePrjectInstance();
			return this.convertToProgen(vhdFile, activeProject);
		} catch (IOException | VhdlParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private void setWorkSpaceDetails(String fileName) {
		File file = new File(fileName);
		this.workSpace = file.getParent();
	}

	protected boolean isComponentAlreadyParsed(Component component) {
		String componentName = component.getIdentifier();
		boolean isParsed = this.entityComponentDependencyMap
				.containsKey(componentName);
		return isParsed;
	}

	protected void addComponentDependencyForEntity(String entityName,
			Component component) {
		if (!this.entityComponentDependencyMap.containsKey(entityName)) {
			List<Component> componentList = new ArrayList<Component>();
			this.entityComponentDependencyMap.put(entityName, componentList);
		}

		this.entityComponentDependencyMap.get(entityName).add(component);
	}

	protected void addUnresovedComponentToList(Component component) {
		this.componentsToBeResolved.add(component);
	}

	public Architecture getArchitecture() {
		return this.coreEntityArchitecture;
	}

	public void setDeclaredSignals(List<Signal> allDeclaredSignalsList) {
		this.declaredSignals = allDeclaredSignalsList;
	}

	public void setDeclaredVariables(List<Variable> allVariablesList) {
		this.declaredVariables = allVariablesList;
	}

	public void addComponentInstantiationToConverter(
			ComponentInstantiation componentInstantiation) {
		this.componentInstantiation.add(componentInstantiation);
	}

	public void addProcessStatementsToConverter(
			AbstractProcessStatement processStatement) {
		this.processStatments.add(processStatement);
	}

	public void addStatementAssignment(ConditionalSignalAssignment statement) {
		this.signalAssignmentList.add(statement);
	}

	public static class VhdlStatementAnalyser {

		public static class InputOutputPort {
			public InputOutputPort(String entityId, String signalBus) {
				this.entityId = entityId;
				this.signalBusName = signalBus;
			}

			String entityId;
			String signalBusName;
		}

		InputOutputPort source;
		List<InputOutputPort> destination = new ArrayList<InputOutputPort>();
		StatementType typeOfStatement;
		SourceOfAnalysis statementSource;

		public enum StatementType {
			Signal, variable;
		}

		public enum SourceOfAnalysis {
			ComponentInstantiation,
			SignalAssignment;
		}
	}
}
