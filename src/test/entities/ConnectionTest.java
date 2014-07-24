package entities;

import java.util.List;

import helper.ProcGenException;

import org.testng.Assert;
import org.testng.annotations.Test;

import electronics.logic.helper.Connection;
import electronics.logic.helper.ConnectionManager;
import electronics.logic.helper.ConnectionType;
import electronics.logic.helper.Entity;
import electronics.logic.helper.EntityChangeEvent;
import electronics.logic.helper.EntityChangeEvent.EntityChangeType;
import electronics.logic.helper.EntityManager;
import electronics.logic.helper.Project;

public class ConnectionTest {
	@Test
	public void testBasicConnectionCreation() {
		Entity testEntity1 = new Entity("testEntity1");
		Entity testEntity2 = new Entity("testEntity2");

		Project  testProject = new Project("testProject");
		EntityManager em = testProject.getEntityManager();
		try {
			em.addBaseEntity(testEntity1);
			em.addBaseEntity(testEntity2);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
		
		Assert.assertEquals(testEntity1.getId(), "1");
		Assert.assertEquals(testEntity2.getId(), "2");
		
		
		try {
			em.addOutputSignal(testEntity1.getId(), "outputPort1", 2);
			em.addInputSignal(testEntity2.getId(),"inputPort1", 2);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
		
		ConnectionManager cm = testProject.getConnectionManager();
		try {
			cm.createConnectionBetweenBaseEntities(testEntity1, testEntity2, testEntity1.getOutputByName("outputPort1"), testEntity2.getInputByName("inputPort1"), ConnectionType.DIRECT_CONNECTION);
		} catch (ProcGenException e) {
			Assert.fail();
			e.printStackTrace();
		}
		
		List<Connection> connectionListForOutputPort1 = cm.getConnectionsForEntity(testEntity1.getId(), "outputPort1");
		Assert.assertEquals(connectionListForOutputPort1.size(), 1);
		Assert.assertEquals(connectionListForOutputPort1.get(0).getSourceEntityId(), testEntity1.getId());
		Assert.assertEquals(connectionListForOutputPort1.get(0).getDestinationEntityId(), testEntity2.getId());
		Assert.assertEquals(connectionListForOutputPort1.get(0).getInputSignal().getName(),"outputPort1");
		Assert.assertEquals(connectionListForOutputPort1.get(0).getOutputSignal().getName(),"inputPort1");
	}
	
}
