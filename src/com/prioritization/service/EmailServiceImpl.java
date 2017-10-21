package com.prioritization.service;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.prioritization.dao.EmailDao;
import com.prioritization.dao.EmailDaoImpl;
import com.prioritization.dao.PriorityDaoImpl;

public class EmailServiceImpl implements EmailService {

	private EmailDao emailDao;
	private PriorityDaoImpl priorityDao;
	
	public EmailServiceImpl() {
		emailDao = new EmailDaoImpl();
		priorityDao = new PriorityDaoImpl();
	}


	/**
	 * The method that returns a email json object based on id of email saved in database.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	@Override
	public JSONObject getEmailById(String emailId) throws Exception {
		JSONObject jsonObject = null;
		try {
			jsonObject = emailDao.getEmailById(emailId);
			String priorityLabel = (String) jsonObject.get("priorityLabel");
			
			JSONArray higherPrioritiesJsonArray = 
					priorityDao.getAllPrioritiesHigherThanCurrentPriority(priorityLabel);
			if(higherPrioritiesJsonArray != null && higherPrioritiesJsonArray.length() > 0){
				for (Object object : higherPrioritiesJsonArray) {
					JSONObject priorityJsonObject = (JSONObject) object;
					String higherPriorityLabel = (String) priorityJsonObject.get("priorityLabel");
					JSONArray unReadEmailsJsonArray = 
							emailDao.findAllUnReadEmailsByPriorityLabel(higherPriorityLabel);
					if(unReadEmailsJsonArray != null && unReadEmailsJsonArray.length() > 0){
						return null;
					}
				}
			}
			
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
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
	@Override
	public void applyPrioritiesToEmail(String email) throws Exception {
		try {
			JSONArray priorityJsonArray = priorityDao.getAllPriorities();
			DBObject dbObject = (DBObject) JSON.parse(email);
			int latestApplyOrderValue = 10000;
			for (Object object : priorityJsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				JSONObject conditionJsonObject = (JSONObject) jsonObject.get("condition");
				if(conditionJsonObject!= null){
					String conditionParameter = (String) conditionJsonObject.get("parameter");
					String conditionParameterValue = (String) conditionJsonObject.get("value");
					int applyOrder = (int) conditionJsonObject.get("applyOrder");
					String priorityLabel = (String) jsonObject.get("priorityLabel");
					
					JSONObject popupJsonObject = (JSONObject) jsonObject.get("popUp");
					Calendar calendar = Calendar.getInstance();
					Date timeStamp = calendar.getTime();
					
					boolean isAutoDelete = (boolean)jsonObject.get("autoDelete");
					if ("email".equalsIgnoreCase(conditionParameter)){
						String fromEmailId = (String) dbObject.get("from_emailId");
						if(fromEmailId != null 
								&& fromEmailId.equalsIgnoreCase(conditionParameterValue)
								&& latestApplyOrderValue > applyOrder){
							latestApplyOrderValue = applyOrder;
							dbObject.put("priorityLabel", priorityLabel);
							
							int popupTimeInSec = (int) popupJsonObject.get("popUpTime");
							calendar.add(Calendar.SECOND, popupTimeInSec);
							timeStamp = calendar.getTime();
							dbObject.put("read_popup_time", timeStamp);
							if (isAutoDelete){
								Calendar newCalendar = Calendar.getInstance();
								newCalendar.add(Calendar.SECOND, 172800);
								dbObject.put("auto_delete_time", newCalendar.getTime());
							}
							/*
							 * Apply Label to email
							 */
						}
					} else if ("subject".equalsIgnoreCase(conditionParameter)){
						String subject = (String) dbObject.get("subject");
						if(subject != null 
								&& subject.contains(conditionParameterValue)
								&& latestApplyOrderValue > applyOrder){
							latestApplyOrderValue = applyOrder;
							dbObject.put("priorityLabel", priorityLabel);
							
							int popupTimeInSec = (int) popupJsonObject.get("popUpTime");
							calendar.add(Calendar.SECOND, popupTimeInSec);
							timeStamp = calendar.getTime();
							dbObject.put("read_popup_time", timeStamp);
							if (isAutoDelete){
								Calendar newCalendar = Calendar.getInstance();
								newCalendar.add(Calendar.SECOND, 172800);
								dbObject.put("auto_delete_time", newCalendar.getTime());
							}
							/*
							 * Apply Label to email
							 */
						}
					} else {
						dbObject.put("priorityLabel", "Low");
						Calendar newCalendar = Calendar.getInstance();
						newCalendar.add(Calendar.SECOND, 172800);
						dbObject.put("auto_delete_time", newCalendar.getTime());
					}
				}

			}
			emailDao.addNewEmail(dbObject);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
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
		try {
			emailDao.markEmailAsRead(emailId);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
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
			emailDao.markEmailAsReplied(emailId);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
	}

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
	@Override
	public void deleteEmails() throws Exception {
		try {
			emailDao.deleteEmails();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
	}
	public static void applyPrioritiesToEmailMethod(String email) throws Exception {
		try {
			PriorityDaoImpl priorityDao = new PriorityDaoImpl();
			JSONArray priorityJsonArray = priorityDao.getAllPriorities();
			DBObject dbObject = (DBObject) JSON.parse(email);
			int latestApplyOrderValue = 10000;
			for (Object object : priorityJsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				JSONObject conditionJsonObject = (JSONObject) jsonObject.get("condition");
				if(conditionJsonObject!= null){
					String conditionParameter = (String) conditionJsonObject.get("parameter");
					String conditionParameterValue = (String) conditionJsonObject.get("value");
					String priorityLabel = (String) jsonObject.get("priorityLabel");
					
					int applyOrder = (int) conditionJsonObject.get("applyOrder");
					if ("email".equalsIgnoreCase(conditionParameter)){
						String fromEmailId = (String) dbObject.get("from_emailId");
						if(fromEmailId != null 
								&& fromEmailId.equalsIgnoreCase(conditionParameterValue)
								&& latestApplyOrderValue > applyOrder){
							latestApplyOrderValue = applyOrder;
							dbObject.put("priorityLabel", priorityLabel);
							/*
							 * Apply Label to email
							 */
						}
					} else if ("subject".equalsIgnoreCase(conditionParameter)){
						String subject = (String) dbObject.get("subject");
						if(subject != null 
								&& subject.contains(conditionParameterValue)
								&& latestApplyOrderValue > applyOrder){
							latestApplyOrderValue = applyOrder;
							dbObject.put("priorityLabel", priorityLabel);
							/*
							 * Apply Label to email
							 */
						}
					}
				}

			}
			//System.out.println(dbObject);
			//emailDao.addNewEmail(dbObject);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
	}
	
	public static JSONObject getEmailByIdMethod(String emailId) throws Exception {
		JSONObject jsonObject = null;
		try {
			PriorityDaoImpl priorityDao = new PriorityDaoImpl();
			EmailDao emailDao = new EmailDaoImpl();
			jsonObject = emailDao.getEmailById(emailId);
			String priorityLabel = (String) jsonObject.get("priorityLabel");
			
			JSONArray higherPrioritiesJsonArray = 
					priorityDao.getAllPrioritiesHigherThanCurrentPriority(priorityLabel);
			if(higherPrioritiesJsonArray != null && higherPrioritiesJsonArray.length() > 0){
				for (Object object : higherPrioritiesJsonArray) {
					JSONObject priorityJsonObject = (JSONObject) object;
					String higherPriorityLabel = (String) priorityJsonObject.get("priorityLabel");
					JSONArray unReadEmailsJsonArray = 
							emailDao.findAllUnReadEmailsByPriorityLabel(higherPriorityLabel);
					if(unReadEmailsJsonArray != null && unReadEmailsJsonArray.length() > 0){
						return null;
					}
				}
			}
			
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
		return jsonObject;
	}
	
	public static void main(String[] args) throws Exception {
		//getAllPrioritiesMethod();
		//applyPrioritiesToEmailMethod("{ \"from_emailId\": \"manager@company.com\", \"to_emailId\": \"abcd@gmail.com\", \"subject\": \"Urgent Work Please Reply\", \"read\": false, \"replied\": false}");
		getEmailByIdMethod("59eb24e98d6a51a06d4b0485");
	}
	
}
