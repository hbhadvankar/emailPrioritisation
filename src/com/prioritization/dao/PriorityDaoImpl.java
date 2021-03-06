package com.prioritization.dao;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class PriorityDaoImpl implements PriorityDao{
	final static Logger logger = Logger.getLogger(PriorityDaoImpl.class);
	@Override
	public void updatePriority(String priority) throws Exception{
		// TODO Auto-generated method stub
		try {
			logger.debug("in updatePriority Dao method.");
			DBCollection collection = getPriorityCollection();
			 
			// create a document to store key and value
			DBObject dbObject = (DBObject) JSON.parse(priority);
			ObjectId id = (ObjectId) dbObject.get("_id");
			BasicDBObject queryDbObject = new BasicDBObject().append("_id", id);
			
			collection.update(queryDbObject,dbObject);
			logger.debug("priority updated");

		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			logger.error("UnknownHostException in update priority", unknownHostException);
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			logger.error("MongoException in update priority", exception);
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
			logger.debug("in createPriority Dao method.");

			DBCollection collection = getPriorityCollection();
			 
			DBObject dbObject = (DBObject) JSON.parse(priority);

			collection.insert(dbObject);
			logger.debug("priority inserted");

		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			logger.error("UnknownHostException in creating priority", unknownHostException);
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			logger.error("MongoException in creating priority", exception);
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
					logger.debug("In getAllPriorities Dao Method");

					DBCollection collection = getPriorityCollection();
					 
					DBCursor dbCursor = collection.find();
					
					while(dbCursor.hasNext()){
						DBObject dbObject = dbCursor.next();
						JSONObject jsonObject = new JSONObject(JSON.serialize(dbObject));
						jsonArray.put(jsonObject);
					}
				} catch (UnknownHostException unknownHostException) {
					// TODO Auto-generated catch block
					logger.error("UnknownHostException in get all priority", unknownHostException);
					throw unknownHostException;
				} catch (MongoException exception) {
					// TODO Auto-generated catch block
					logger.error("MongoException in get all priority", exception);
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
			logger.debug("In getPriorityById Dao Method");

			DBCollection collection = getPriorityCollection();
			 
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", new ObjectId(priorityId));
			DBCursor cursor = collection.find(searchQuery);
			
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				jsonObject = new JSONObject(JSON.serialize(dbObject));
			}
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			logger.error("UnknownHostException in get priority by id", unknownHostException);
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			logger.error("MongoException in get priority by id", exception);
			throw exception;
		}catch (Exception exception) {
			// TODO Auto-generated catch block
			logger.error("MongoException in get priority by id", exception);
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
			logger.debug("In deletePriorityById Dao Method");

			DBCollection collection = getPriorityCollection();
			 
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", new ObjectId(priorityId));
			collection.remove(searchQuery);
			
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			logger.error("UnknownHostException in delete priority by id", unknownHostException);
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			logger.error("MongoException in delete priority by id", exception);
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
					logger.error("UnknownHostException in getAllPrioritiesHigherThanCurrentPriority", unknownHostException);
					throw unknownHostException;
				} catch (MongoException exception) {
					// TODO Auto-generated catch block
					logger.error("MongoException in getAllPrioritiesHigherThanCurrentPriority", exception);
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
		DB db = mongo.getDB("email_prioritisation_db");
		 
		// if collection doesn't exists, mongoDB will create it automatically
		DBCollection collection = db.getCollection("priorities");
		return collection;
	}
	

}
