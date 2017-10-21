package test;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import com.prioritization.dataObject.Priority;

public class PriorityTest {

	
	private static void getAllPrioritiesMethod() {
		// TODO Auto-generated method stub
		try {
			// connect to mongoDB, IP and port number
			Mongo mongo = new Mongo("localhost", 27017);
			 
			// get database from MongoDB,
			// if database doesn't exists, mongoDB will create it automatically
			DB db = mongo.getDB("priorities");
			 
			// Get collection from MongoDB, database named "dineshonjavaDB"
			// if collection doesn't exists, mongoDB will create it automatically
			DBCollection collection = db.getCollection("priority");
			 
			// create a document to store key and value
			/*BasicDBObject document = new BasicDBObject();
			document.put("id", 1000);
			document.put("msg", "Hello World mongoDB in Java! Dinesh On Java");*/
			//DBObject doc = toDBObject(priority);
			
			// save it into collection named "dineshonjavaCollection"
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", new ObjectId("59e9d0828d6a9de529877fd4"));
			DBCursor cursor = collection.find(searchQuery);
			JSONObject jsonObject;
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				jsonObject = new JSONObject(JSON.serialize(dbObject));
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

	
	public static void updatePriorityMethod(String priority) {
		// TODO Auto-generated method stub
		try {
			// connect to mongoDB, IP and port number
			Mongo mongo = new Mongo("localhost", 27017);
			 
			// get database from MongoDB,
			// if database doesn't exists, mongoDB will create it automatically
			DB db = mongo.getDB("priorities");
			 
			// Get collection from MongoDB, database named "dineshonjavaDB"
			// if collection doesn't exists, mongoDB will create it automatically
			DBCollection collection = db.getCollection("priority");
			 
			// create a document to store key and value
			DBObject dbObject = (DBObject) JSON.parse(priority);
			ObjectId id = (ObjectId) dbObject.get("_id");
			/*p.setId(id.toString());*/
			BasicDBObject queryDbObject = new BasicDBObject().append("_id", id);
			
			// save it into collection named "dineshonjavaCollection"
			collection.update(queryDbObject,dbObject);
			System.out.println("Priority updated!!!!!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//getAllPrioritiesMethod();
		updatePriorityMethod("{\"condition\":{\"applyOrder\":1,\"parameter\":\"email\",\"parameterValue\":\"bhadvankar@gmail.com\"},\"priorityValue\":2,\"prioritylabel\":\"MyNewLabel_Harish_updated\",\"autoDelete\":false,\"_id\":{\"$oid\":\"59e9d0828d6a9de529877fd4\"},\"readPopupTime\":1600,\"mustReply\":true}");
	}

	
	public static DBObject toDBObject(Priority p) {

		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.append("priorityLabel", p.getPrioritylabel()).append("priorityValue", p.getPriorityValue());
		/*if (p.getId() != null)
			builder = builder.append("_id", new ObjectId(p.getId()));*/
		return builder.get();
	}

	// convert DBObject Object to Person
	// take special note of converting ObjectId to String
	public static Priority toPerson(DBObject doc) {
		Priority p = new Priority();
		p.setPrioritylabel((String) doc.get("priorityLabel"));
		p.setPriorityValue((Integer) doc.get("priorityValue"));
		/*ObjectId id = (ObjectId) doc.get("_id");
		p.setId(id.toString());*/
		return p;

	}

}
