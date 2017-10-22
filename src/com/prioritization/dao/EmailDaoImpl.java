package com.prioritization.dao;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

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

public class EmailDaoImpl implements EmailDao {

	/**
	 * The method that returns a email json object based on id of email saved in
	 * database.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	@Override
	public JSONObject getEmailById(String emailId) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		try {
			DBCollection collection = getEmailCollection();

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", new ObjectId(emailId));
			DBCursor cursor = collection.find(searchQuery);

			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				jsonObject = new JSONObject(JSON.serialize(dbObject));
			}
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
		return jsonObject;
	}

	/**
	 * The method to insert a new email in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public void addNewEmail(DBObject dbObject) throws Exception {
		// TODO Auto-generated method stub
		try {
			DBCollection collection = getEmailCollection();

			Calendar calendar = Calendar.getInstance();
			Date timeStamp = calendar.getTime();

			dbObject.put("timestamp", timeStamp);

			ObjectId objectId = (ObjectId) dbObject.get("_id");
			if (objectId == null) {
				collection.insert(dbObject);
			} else {
				BasicDBObject queryDbObject = new BasicDBObject().append("_id", objectId);

				collection.update(queryDbObject, dbObject);
			}

			System.out.println("New email Inserted!!!!!");
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
	}

	/**
	 * The method which marks email as read in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	@Override
	public void markEmailAsRead(String emailId) throws Exception {
		// TODO Auto-generated method stub
		try {
			DBCollection collection = getEmailCollection();

			DBCollection priorityCollection = getPriorityCollection();

			JSONObject emailJson = getEmailById(emailId);
			String priorityLabel = (String) emailJson.get("priorityLabel");

			DBCursor dbCursor = priorityCollection.find(new BasicDBObject("priorityLabel", priorityLabel));
			DBObject dbObject = null;
			while (dbCursor.hasNext()) {
				dbObject = dbCursor.next();
			}
			int autoDeleteTimeInSeconds = 1;
			if (dbObject != null) {
				int priorityValue = (int) dbObject.get("priorityValue");
				autoDeleteTimeInSeconds = 345600 / priorityValue;
			}
			// create a document to store key and value
			// DBObject dbObject = (DBObject) JSON.parse(priority);
			BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(emailId));

			Calendar newCalendar = Calendar.getInstance();
			newCalendar.add(Calendar.SECOND, autoDeleteTimeInSeconds);

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("read", true));
			// .append("$set",new BasicDBObject().append("auto_delete_time",
			// newCalendar.getTime()));

			collection.update(searchQuery, newDocument);

			newDocument.append("$set", new BasicDBObject().append("auto_delete_time", newCalendar.getTime()));
			collection.update(searchQuery, newDocument);
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
	}

	/**
	 * The method which marks email as replied in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	@Override
	public void markEmailAsReplied(String emailId) throws Exception {
		try {
			DBCollection collection = getEmailCollection();

			BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(emailId));

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("replied", true));

			collection.update(searchQuery, newDocument);
			newDocument.append("$set", new BasicDBObject().append("read", true));
			collection.update(searchQuery, newDocument);
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
	}

	/**
	 * The method which returns all emails which are unread for the given
	 * priority label.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	@Override
	public JSONArray findAllUnReadEmailsByPriorityLabel(String priorityLabel) throws Exception {
		// TODO Auto-generated method stub
		JSONArray jsonArray = new JSONArray();
		try {
			DBCollection collection = getEmailCollection();

			DBCursor dbCursor = collection
					.find(new BasicDBObject().append("priorityLabel", priorityLabel).append("read", false));

			while (dbCursor.hasNext()) {
				DBObject dbObject = dbCursor.next();
				JSONObject jsonObject = new JSONObject(JSON.serialize(dbObject));
				jsonArray.put(jsonObject);
			}
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
		return jsonArray;
	}

	/**
	 * The method that deletes all emails based on below criteria. 1. Delete all
	 * Emails with lowest Priority. Lowest Priority is calculated as The
	 * priority from the priority collection which has the highest Priority
	 * value. 2. delete all emails which are read. 3. delete all email which has
	 * auto_delete_time field. All emails matching above criteria will be
	 * deleted if the deletion time is reached.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	@Override
	public void deleteEmails() throws Exception {
		try {
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.add(Calendar.SECOND, 400000);
			DBCollection collection = getEmailCollection();

			/*
			 * DBCollection priorityCollection = getEmailCollection();
			 * priorityCollection.find().sort(new BasicDBObject("priorityValue",
			 * -1));
			 */

			collection.remove(new BasicDBObject().append("priorityLabel", "Low").append("auto_delete_time",
					new BasicDBObject("$lte", newCalendar.getTime())));
			collection.remove(new BasicDBObject().append("read", true).append("auto_delete_time",
					new BasicDBObject("$lte", newCalendar.getTime())));
			collection.remove(
					new BasicDBObject().append("auto_delete_time", new BasicDBObject("$lte", newCalendar.getTime())));

		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
	}

	/**
	 * The method which returns all emails with expired read popup time from
	 * database.
	 * 
	 */
	@Override
	public JSONArray getAllEmailsWithExpiredReadPopupTime() throws Exception {
		JSONArray jsonArray = new JSONArray();
		try {
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.add(Calendar.SECOND, 400000);
			DBCollection collection = getEmailCollection();

			/*
			 * DBCollection priorityCollection = getEmailCollection();
			 * priorityCollection.find().sort(new BasicDBObject("priorityValue",
			 * -1));
			 */

			DBCursor dbCursor = collection.find(new BasicDBObject().append("read", false).append("read_popup_time",
					new BasicDBObject("$lte", newCalendar.getTime())));
			while (dbCursor.hasNext()) {
				DBObject dbObject = dbCursor.next();
				JSONObject jsonObject = new JSONObject(JSON.serialize(dbObject));
				jsonArray.put(jsonObject);
			}

		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
		return jsonArray;
	}

	/**
	 * The method which returns emails collection from mongodb.
	 * 
	 * @return The priority Collection.
	 * @throws UnknownHostException
	 */
	private static DBCollection getEmailCollection() throws UnknownHostException {
		// connect to mongoDB, IP and port number
		Mongo mongo = new Mongo("localhost", 27017);
		// Mongo mongo = new Mongo("localhostqwse", 27017221);
		// get database from MongoDB,
		// if database doesn't exists, mongoDB will create it automatically
		DB db = mongo.getDB("email_priotisation_db");

		// if collection doesn't exists, mongoDB will create it automatically
		DBCollection collection = db.getCollection("emails");
		return collection;
	}

	/**
	 * The method which returns priority collection from mongodb.
	 * 
	 * @return The priority Collection.
	 * @throws UnknownHostException
	 */
	private static DBCollection getPriorityCollection() throws UnknownHostException {
		// connect to mongoDB, IP and port number
		Mongo mongo = new Mongo("localhost", 27017);
		// Mongo mongo = new Mongo("localhostqwse", 27017221);
		// get database from MongoDB,
		// if database doesn't exists, mongoDB will create it automatically
		DB db = mongo.getDB("email_priotisation_db");

		// if collection doesn't exists, mongoDB will create it automatically
		DBCollection collection = db.getCollection("priorities");
		return collection;
	}

	public static void markEmailAsReadMethod(String emailId) throws Exception {
		// TODO Auto-generated method stub
		try {
			DBCollection collection = getEmailCollection();

			DBCollection priorityCollection = getPriorityCollection();

			JSONObject emailJson = getEmailByIdMethod(emailId);
			String priorityLabel = (String) emailJson.get("priorityLabel");

			DBCursor dbCursor = priorityCollection.find(new BasicDBObject("priorityLabel", priorityLabel));
			DBObject dbObject = null;
			while (dbCursor.hasNext()) {
				dbObject = dbCursor.next();
			}
			int autoDeleteTimeInSeconds = 1;
			if (dbObject != null) {
				int priorityValue = (int) dbObject.get("priorityValue");
				autoDeleteTimeInSeconds = 345600 / priorityValue;
			}
			// create a document to store key and value
			// DBObject dbObject = (DBObject) JSON.parse(priority);
			BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(emailId));

			Calendar newCalendar = Calendar.getInstance();
			newCalendar.add(Calendar.SECOND, autoDeleteTimeInSeconds);

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("read", false).append("$set",
					new BasicDBObject().append("auto_delete_time", newCalendar.getTime())));

			collection.update(searchQuery, newDocument);

			// newDocument.append("$set",new
			// BasicDBObject().append("auto_delete_time",
			// newCalendar.getTime()));
			// collection.update(searchQuery, newDocument);

		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
	}

	public static JSONArray getAllEmailsWithExpiredReadPopupTimeMethod() throws Exception {
		JSONArray jsonArray = new JSONArray();
		try {
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.add(Calendar.SECOND, 400000);
			DBCollection collection = getEmailCollection();

			/*
			 * DBCollection priorityCollection = getEmailCollection();
			 * priorityCollection.find().sort(new BasicDBObject("priorityValue",
			 * -1));
			 */

			DBCursor dbCursor = collection.find(new BasicDBObject().append("read", false).append("read_popup_time",
					new BasicDBObject("$lte", newCalendar.getTime())));
			while (dbCursor.hasNext()) {
				DBObject dbObject = dbCursor.next();
				JSONObject jsonObject = new JSONObject(JSON.serialize(dbObject));
				jsonArray.put(jsonObject);
			}

		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
		return jsonArray;
	}

	public static void main(String[] args) throws Exception {
		// markEmailAsReadMethod("59ec73768d6ad29939a4be2e");
		getAllEmailsWithExpiredReadPopupTimeMethod();
	}

	public static JSONObject getEmailByIdMethod(String emailId) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		try {
			DBCollection collection = getEmailCollection();

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", new ObjectId(emailId));
			DBCursor cursor = collection.find(searchQuery);

			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				jsonObject = new JSONObject(JSON.serialize(dbObject));
			}
		} catch (UnknownHostException unknownHostException) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + unknownHostException.getMessage());
			throw unknownHostException;
		} catch (MongoException exception) {
			// TODO Auto-generated catch block
			System.out.println("Exception = " + exception.getMessage());
			throw exception;
		}
		return jsonObject;
	}

}
