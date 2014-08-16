/**
 * 
 */
package hdl.translator.logic;

import helper.ProcGenException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.concurrent.ConcurrentStatement;
import de.upb.hni.vmagic.declaration.BlockDeclarativeItem;
import de.upb.hni.vmagic.declaration.Component;
import de.upb.hni.vmagic.declaration.DeclarativeItem;
import de.upb.hni.vmagic.libraryunit.Architecture;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.parser.VhdlParser;
import de.upb.hni.vmagic.parser.VhdlParserException;
import de.upb.hni.vmagic.util.VhdlCollections;
import electronics.logic.helper.ElectronicsLogicFacade;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Project;

/**
 * @author DINESH THANGAVEL
 *
 */
public class VhdlToElectronicsConverter {
	
	// keep track of the entity and the list of components
	HashMap<String,List<Component>> entityComponentDependencyMap = new HashMap<String,List<Component>>();
	
	// keeps track of the components that are to be resolved
	List<Component> componentsToBeResolved = new ArrayList<Component>();
	String workSpace;
	
	private VhdlFile readVhdlFile(String filePath) throws IOException, VhdlParserException{
		VhdlFile inFile = VhdlParser.parseFile(filePath);
		return inFile;
	}
	
	/*
	 * Convert to Progen entity and add it to project specified in parameter2
	 */
	private void convertToProgen(VhdlFile vhdlFileInput, Project progenProject) throws ProcGenException{
		List<Entity> entityList = VhdlCollections.getAll(vhdlFileInput.getElements(), Entity.class);
		assert(entityList.size() == 1);
		
		// add the entity
		userinterface.EntityDetailsRetriever.EntityDetailsFromUser newEntityDeatils = VMagicProgenConversionAider.convertToProgenEntity(entityList.get(0));
		EntityManager em =progenProject.getEntityManager();
		assert(em!= null);
		em.addEntity(newEntityDeatils);
		
		// Connections
		List<Architecture> architectureList = VhdlCollections.getAll(vhdlFileInput.getElements(), Architecture.class);
		//assuming only one architecture
		assert(architectureList.size() == 1);
		Architecture newEntityArchitecture = architectureList.get(0);
		processVMagicArchitecture(newEntityArchitecture);
		
	}

	private void processVMagicArchitecture(Architecture newEntityArchitecture) throws ProcGenException {
		
		// identify the registers
		List<BlockDeclarativeItem> declarationList = newEntityArchitecture.getDeclarations();
		processDeclarationStatements(declarationList);

		// resolve dependencies on components
		resolveComponentDepencies();
		
		// identify concurrent statements
		List<ConcurrentStatement> concurrentStatements = newEntityArchitecture.getStatements();
		
		processConcurrentStatements(concurrentStatements,declarationList);
	}
	
	private void resolveComponentDepencies() throws ProcGenException {
		for(Component componentToResolve:componentsToBeResolved){
			String nameOfComponent = componentToResolve.getIdentifier();
			String fileName = getFileNameFromWorkspace(nameOfComponent);
			VhdlToElectronicsConverter newInstance = new  VhdlToElectronicsConverter();
			newInstance.convertToProgen(fileName);
		}		
		// TODO: clear the components to be resolved
	}

	private String getFileNameFromWorkspace(String nameOfComponent) {
		if(this.workSpace !=null){
			File directory  = new File(this.workSpace);
			String[] fileList = directory.list();
			String fileNameToSearch = nameOfComponent + ".vhd";
			for(String directoryContent:fileList){
				if(directoryContent.equals(fileNameToSearch)){
					String fileName = this.workSpace.concat("\\"+ fileNameToSearch);
					return fileName;
				}
			}
		}
		return null;
	}

	private void processDeclarationStatements(
			List<BlockDeclarativeItem> declarationList) {
		
		DeclarationProcessor declarationProcessor = new DeclarationProcessor(this);
		for(BlockDeclarativeItem declaration:declarationList){
			if(declaration instanceof DeclarativeItem){
				declarationProcessor.visit((DeclarativeItem) declaration);
			}
		}
		
	}

	private void processConcurrentStatements(
			List<ConcurrentStatement> concurrentStatementList,
			List<BlockDeclarativeItem> declarationList) {
		
		ConcurrentStatementProcessor concurrentStatementProcessor = new ConcurrentStatementProcessor();
		for(ConcurrentStatement concurrentStatement:concurrentStatementList){
			concurrentStatementProcessor.visit(concurrentStatement);
		}
	}

	public void convertToProgen(String fileName) throws ProcGenException{
		VhdlFile vhdFile;
		try {
			setWorkSpaceDetails(fileName);
			vhdFile = this.readVhdlFile(fileName);
			Project activeProject = ElectronicsLogicFacade.getInstance().getActivePrjectInstance();
			this.convertToProgen(vhdFile,activeProject);
		} catch (IOException | VhdlParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void setWorkSpaceDetails(String fileName) {
		File file = new File(fileName);
		this.workSpace = file.getParent();		
	}

	protected boolean isComponentAlreadyParsed(Component component){
		String componentName = component.getIdentifier();
		boolean isParsed = this.entityComponentDependencyMap.containsKey(componentName);
		return isParsed;	 
	}
	
	protected void addComponentDependencyForEntity(String entityName,Component component){
		if(!this.entityComponentDependencyMap.containsKey(entityName)){
			List<Component> componentList = new ArrayList<Component>();
			this.entityComponentDependencyMap.put(entityName, componentList);
		}
		
		this.entityComponentDependencyMap.get(entityName).add(component);
	}

	protected void addUnresovedComponentToList(Component component){
		this.componentsToBeResolved.add(component);
	}
}

