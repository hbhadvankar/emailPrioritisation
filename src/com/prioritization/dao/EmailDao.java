package com.prioritization.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DBObject;

public interface EmailDao {

	/**
	 * The method that returns a email json object based on id of email saved in database.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	public JSONObject getEmailById(String emailId) throws Exception;

	/**
	 * The method to insert a new email in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public void addNewEmail(DBObject dbObject) throws Exception;
	
	/**
	 * The method which marks email as read in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public void markEmailAsRead(String emailId) throws Exception;
	
	/**
	 * The method which marks email as replied in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public void markEmailAsReplied(String emailId) throws Exception;
	
	/**
	 * The method which returns all emails which are unread for the given priority label.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public JSONArray findAllUnReadEmailsByPriorityLabel(String priorityLabel) throws Exception;
	
	/**
	 * The method that deletes all emails based on below criteria.
	 * 	1. Delete all Emails with lowest Priority.
	 * 		Lowest Priority is calculated as The priority from the priority collection
	 * 		which has the highest Priority value.
	 *  2. delete all emails which are read.
	 *  3. delete all email which has auto_delete_time field.
	 *  All emails matching above criteria will be deleted if the deletion time is reached.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	public void deleteEmails() throws Exception;
}
