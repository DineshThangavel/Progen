/**
 * 
 */
package electronics.logic.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author DINESH THANGAVEL
 *
 */
public class EntityConnectionManager extends ConnectionManager{
	private Entity hostEntity;
	
	public EntityConnectionManager(Entity hostEntity){
		this.hostEntity = hostEntity;
	}
	
	public EntityConnectionManager deepCopy(Entity copiedEntity) {
		EntityConnectionManager newEntityConnectionManagerCopy = new EntityConnectionManager(copiedEntity);
		HashMap<String, HashMap<String, List<Connection>>> connectionDirectoryCopy = new HashMap<String, HashMap<String, List<Connection>>>();

		Iterator iterator = this.connectionDirectory.keySet().iterator();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			HashMap<String, List<Connection>> signalConnectionMapping = this.connectionDirectory
					.get(key);
			HashMap<String, List<Connection>> signalConnectionMappingCopy = new HashMap<String, List<Connection>>();

			Iterator signalConnIterator = signalConnectionMapping.keySet()
					.iterator();
			while (signalConnIterator.hasNext()) {
				String signalName = (String) signalConnIterator.next();
				List<Connection> connectionList = signalConnectionMapping
						.get(signalName);
				List<Connection> connectionListCopy = new ArrayList<Connection>();

				for (Connection connection : connectionList) {
					Connection newConnectionCopy = connection.deepCopy();
					connectionListCopy.add(newConnectionCopy);
				}
				signalConnectionMappingCopy.put(signalName, connectionListCopy);
			}
			connectionDirectoryCopy.put(key, signalConnectionMappingCopy);
		}

		newEntityConnectionManagerCopy.connectionDirectory = connectionDirectoryCopy;
		return newEntityConnectionManagerCopy;
	}
}
