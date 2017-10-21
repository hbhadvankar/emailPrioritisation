package com.prioritization.dao;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.json.JSONArray;
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

public class PriorityDaoImpl implements PriorityDao{

	@Override
	public void updatePriority(String priority) throws Exception{
		// TODO Auto-generated method stub
		try {
			DBCollection collection = getPriorityCollection();
			 
			// create a document to store key and value
			DBObject dbObject = (DBObject) JSON.parse(priority);
			ObjectId id = (ObjectId) dbObject.get("_id");
			/*p.setId(id.toString());*/
			BasicDBObject queryDbObject = new BasicDBObject().append("_id", id);
			
			// save it into collection named "dineshonjavaCollection"
			collection.update(queryDbObject,dbObject);
			System.out.println("Priority updated!!!!!");
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
	}
	
	/**
	 * The method to insert a new priority in database.
	 * 
	 * @param priority The json which is represented in string format.
	 */
	public void createPriority(String priority) throws Exception{
		// TODO Auto-generated method stub
		try {
			DBCollection collection = getPriorityCollection();
			 
			// create a document to store key and value
			DBObject dbObject = (DBObject) JSON.parse(priority);
			
			// save it into collection named "dineshonjavaCollection"
			collection.insert(dbObject);
			System.out.println("New Priority Inserted!!!!!");
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
	}


	/**
	 * The method that returns all priorities.
	 * 
	 * @return The JSONArray Object which contains all priorities.
	 */
	@Override
	public JSONArray getAllPriorities() throws Exception{
		// TODO Auto-generated method stub
		JSONArray jsonArray = new JSONArray();
				try {
					DBCollection collection = getPriorityCollection();
					 
					// create a document to store key and value
					//DBObject doc = toDBObject(priority);
					
					// save it into collection named "dineshonjavaCollection"
					DBCursor dbCursor = collection.find();
					
					while(dbCursor.hasNext()){
						DBObject dbObject = dbCursor.next();
						JSONObject jsonObject = new JSONObject(JSON.serialize(dbObject));
						jsonArray.put(jsonObject);
					}
				} catch (UnknownHostException unknownHostException) {
					// TODO Auto-generated catch block
					System.out.println("Exception = "+unknownHostException.getMessage());
					throw unknownHostException;
				} catch (MongoException exception) {
					// TODO Auto-generated catch block
					System.out.println("Exception = "+exception.getMessage());
					throw exception;
				}
				return jsonArray;
	}
	
	/**
	 * The method that returns a priority json object based on priority id.
	 * 
	 * @param priorityId The priority id.
	 * @return The priority Json Object.
	 */
	@Override
	public JSONObject getPriorityById(String priorityId) throws Exception{
		JSONObject jsonObject = null;
		try {
			DBCollection collection = getPriorityCollection();
			 
			// create a document to store key and value
			//DBObject doc = toDBObject(priority);
			
			// save it into collection named "dineshonjavaCollection"
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", new ObjectId(priorityId));
			DBCursor cursor = collection.find(searchQuery);
			
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				jsonObject = new JSONObject(JSON.serialize(dbObject));
			}
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
		return jsonObject;
	}
	
	/**
	 * The method that deletes a priority json object based on priority id.
	 * 
	 * @param priorityId The priority id.
	 * @return The priority Json Object.
	 */
	@Override
	public void deletePriorityById(String priorityId) throws Exception{
		try {
			DBCollection collection = getPriorityCollection();
			 
			// create a document to store key and value
			//DBObject doc = toDBObject(priority);
			
			// save it into collection named "dineshonjavaCollection"
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", new ObjectId(priorityId));
			collection.remove(searchQuery);
			
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
	}
	
	/**
	 * The method that returns all priorities.
	 * 
	 * @return The JSONArray Object which contains all priorities.
	 */
	@Override
	public JSONArray getAllPrioritiesHigherThanCurrentPriority(String priorityLabel) throws Exception{
		// TODO Auto-generated method stub
		JSONArray jsonArray = new JSONArray();
				try {
					DBCollection collection = getPriorityCollection();
					 
					// create a document to store key and value
					//DBObject doc = toDBObject(priority);
					
					// save it into collection named "dineshonjavaCollection"
					DBCursor dbCursor = collection.find(new BasicDBObject("priorityLabel", priorityLabel));
					DBObject dbObject = null;
					while(dbCursor.hasNext()){
						dbObject = dbCursor.next();
					}
					if(dbObject != null){
						int priorityValue = (int) dbObject.get("priorityValue");
						DBCursor prioritiesCursor = collection.find(new BasicDBObject()
								.append("priorityValue", new BasicDBObject("$lt", priorityValue)));
						while(prioritiesCursor.hasNext()){
							DBObject priorityDbObject = prioritiesCursor.next();
							JSONObject jsonObject = new JSONObject(JSON.serialize(priorityDbObject));
							jsonArray.put(jsonObject);
						}
					}
					
				} catch (UnknownHostException unknownHostException) {
					// TODO Auto-generated catch block
					System.out.println("Exception = "+unknownHostException.getMessage());
					throw unknownHostException;
				} catch (MongoException exception) {
					// TODO Auto-generated catch block
					System.out.println("Exception = "+exception.getMessage());
					throw exception;
				}
				return jsonArray;
	}
	
	/**
	 * The method which returns priority collection from mongodb.
	 * 
	 * @return The priority Collection.
	 * @throws UnknownHostException
	 */
	private DBCollection getPriorityCollection() throws UnknownHostException {
		// connect to mongoDB, IP and port number
		Mongo mongo = new Mongo("localhost", 27017);
		//Mongo mongo = new Mongo("localhostqwse", 27017221);
		// get database from MongoDB,
		// if database doesn't exists, mongoDB will create it automatically
		DB db = mongo.getDB("email_priotisation_db");
		 
		// Get collection from MongoDB, database named "dineshonjavaDB"
		// if collection doesn't exists, mongoDB will create it automatically
		DBCollection collection = db.getCollection("priorities");
		return collection;
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
