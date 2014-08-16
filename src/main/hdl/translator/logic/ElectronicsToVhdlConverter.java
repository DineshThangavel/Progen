/**
 * 
 */
package hdl.translator.logic;

import java.io.IOException;
import java.util.List;

import de.upb.hni.vmagic.AssociationElement;
import de.upb.hni.vmagic.VhdlFile;
import de.upb.hni.vmagic.builtin.StdLogic1164;
import de.upb.hni.vmagic.concurrent.ComponentInstantiation;
import de.upb.hni.vmagic.declaration.Component;
import de.upb.hni.vmagic.libraryunit.Architecture;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.libraryunit.LibraryClause;
import de.upb.hni.vmagic.libraryunit.LibraryUnit;
import de.upb.hni.vmagic.libraryunit.UseClause;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.output.VhdlOutput;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Project;
import electronics.logic.helper.SignalBus;

/**
 * @author DINESH THANGAVEL
 * 
 */
public class ElectronicsToVhdlConverter {

	public void convertProjectToVhdl(Project project,String outputDirectory) throws IOException {
		VhdlFile outputFile = createVhdlFileForBaseProject(project, outputDirectory);
		String fileName = outputDirectory + "\\" + project.getName() + ".vhd";
		VhdlOutput.toFile(outputFile, fileName);
	}

	private VhdlFile createBasicVhdlFile() {
		VhdlFile file = new VhdlFile();

		// Add Elements
		file.getElements().add(new LibraryClause("IEEE"));
		file.getElements().add(StdLogic1164.USE_CLAUSE);
		file.getElements().add(new UseClause("ieee.std_logic_unsigned.all"));

		return file;
	}

	private VhdlFile createVhdlFileForBaseProject(Project project,
			String outputFolderPath) throws IOException {

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
				+ entityToConvert.getId());
		for (SignalBus inputSignal : entityToConvert.getInputPortList()) {
			Signal output = null;
			if (inputSignal.getBusWidth() > 1) {
				output = new Signal(
						inputSignal.getName(),
						Signal.Mode.IN,
						StdLogic1164.STD_LOGIC_VECTOR(inputSignal.getBusWidth()));
			} else if (inputSignal.getBusWidth() == 1) {
				output = new Signal(inputSignal.getName(), Signal.Mode.IN,
						StdLogic1164.STD_LOGIC);
			}

			if (output != null) {
				entity.getPort().add(output);
			}
		}

		return entity;
	}

	private LibraryUnit createProjectArchitecture(Entity entity,
			Project project, String outputFolderPath) throws IOException {

		EntityManager em = project.getEntityManager();
		Architecture architecture = new Architecture("rtl", entity);

		List<electronics.logic.helper.Entity> baseEntitiesList = em
				.getBaseEntities();
		// component declarations
		for (electronics.logic.helper.Entity baseEntity : baseEntitiesList) {
			// recursive function that creates vhdl file for all sub-entities
			createVhdlFileForEntity(baseEntity, outputFolderPath);
			Entity vMagicEntity = this
					.createVMagicEntityForProgenEntity(baseEntity);
			Component componentDeclaration = new Component(vMagicEntity);

			// component instantiations
			ComponentInstantiation compInstance = new ComponentInstantiation(
					componentDeclaration.getIdentifier(), componentDeclaration);
			compInstance.getPortMap().add(
					new AssociationElement(new Signal("CLK", Signal.Mode.IN,
							StdLogic1164.STD_LOGIC)));
			architecture.getDeclarations().add(componentDeclaration);
			architecture.getStatements().add(compInstance);
		}

		return architecture;
	}

	private void createVhdlFileForEntity(
			electronics.logic.helper.Entity entityToConvert, String outputFolderPath) throws IOException {

		VhdlFile file = this.createBasicVhdlFile();
		Entity vMagicEntity = this
				.createVMagicEntityForProgenEntity(entityToConvert);
		file.getElements().add(vMagicEntity);
		// file.getElements().add(createArchitecture(hostProjectEntity,project,outputFolderPath));

		Architecture architecture = new Architecture("rtl", vMagicEntity);
		for (electronics.logic.helper.Entity childEntity:entityToConvert.getChildEntityList()){
			createVhdlFileForEntity(childEntity,outputFolderPath);
			Entity vMagicChildEntity = this.createVMagicEntityForProgenEntity(childEntity);
			
			Component componentDeclaration = new Component(vMagicChildEntity);

			// component instantiations
			ComponentInstantiation compInstance = new ComponentInstantiation(
					componentDeclaration.getIdentifier(), componentDeclaration);
			
			architecture.getDeclarations().add(componentDeclaration);
			architecture.getStatements().add(compInstance);
		}
		
		file.getElements().add(architecture);
		
		String fileName = outputFolderPath + "\\" + entityToConvert.getName() + ".vhd";
		VhdlOutput.toFile(file, fileName);

	}

	private Entity createBaseProject(Project project) {

		Entity entity = new Entity(project.getName());
		final Signal clk = new Signal("CLK", Signal.Mode.IN,
				StdLogic1164.STD_LOGIC);
		entity.getPort().add(clk);
		return entity;
	}

}
