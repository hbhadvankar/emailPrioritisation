package com.prioritization.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.prioritization.dao.EmailDao;
import com.prioritization.dao.EmailDaoImpl;
import com.prioritization.dao.PriorityDaoImpl;
import com.prioritization.rest.EmailResource;

public class EmailServiceImpl implements EmailService {
	final static Logger logger = Logger.getLogger(EmailServiceImpl.class);
	private EmailDao emailDao;
	private PriorityDaoImpl priorityDao;

	public EmailServiceImpl() {
		emailDao = new EmailDaoImpl();
		priorityDao = new PriorityDaoImpl();
	}

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
		JSONObject jsonObject = null;
		try {
			if (emailId != null) {
				logger.debug("fetching email by id = " + emailId);
				jsonObject = emailDao.getEmailById(emailId);
				String priorityLabel = (String) jsonObject.get("priorityLabel");

				logger.debug("calling method to get all priorities by priority label = " + priorityLabel);
				JSONArray higherPrioritiesJsonArray = new JSONArray();
				if ("Low".equalsIgnoreCase(priorityLabel)) {
					higherPrioritiesJsonArray = priorityDao.getAllPriorities();
				} else {
					higherPrioritiesJsonArray = priorityDao.getAllPrioritiesHigherThanCurrentPriority(priorityLabel);
				}
				if (higherPrioritiesJsonArray != null && higherPrioritiesJsonArray.length() > 0) {
					for (Object object : higherPrioritiesJsonArray) {
						JSONObject priorityJsonObject = (JSONObject) object;
						String higherPriorityLabel = priorityJsonObject.has("priorityLabel")
								? (String) priorityJsonObject.get("priorityLabel") : null;
						if (higherPriorityLabel != null && higherPriorityLabel.trim().length() != 0) {
							JSONArray unReadEmailsJsonArray = emailDao
									.findAllUnReadEmailsByPriorityLabel(higherPriorityLabel);
							if (unReadEmailsJsonArray != null && unReadEmailsJsonArray.length() > 0) {
								return null;
							}
						}
					}
				}
			}
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			logger.error("Exception Occured in getting email by id due to follwing reasons", exception);
			throw exception;
		}
		return jsonObject;
	}

	/**
	 * The method to insert a new email in database. and assign priorities.
	 * 
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	@Override
	public void applyPrioritiesToEmail(String email) throws Exception {
		try {
			logger.debug("In applyPrioritiesToEmail method = ");

			JSONArray priorityJsonArray = priorityDao.getAllPriorities();
			DBObject dbObject = (DBObject) JSON.parse(email);
			applyLowPriorityToEmail(dbObject);
			int latestApplyOrderValue = 10000;
			if (priorityJsonArray != null && priorityJsonArray.length() > 0) {
				for (Object object : priorityJsonArray) {
					JSONObject jsonObject = (JSONObject) object;
					JSONObject conditionJsonObject = jsonObject.has("condition") ? 
							(JSONObject) jsonObject.get("condition") : null;
					if (conditionJsonObject != null) {
						
						String conditionParameter = conditionJsonObject.has("parameter") ? 
								(String) conditionJsonObject.get("parameter") : null;
								
						String conditionParameterValue = conditionJsonObject.has("value") ?
								(String) conditionJsonObject.get("value") : null;
						int applyOrder = (int) conditionJsonObject.get("applyOrder");
						String priorityLabel = (String) jsonObject.get("priorityLabel");

						JSONObject popupJsonObject = jsonObject.has("popUp") ? 
								(JSONObject) jsonObject.get("popUp") : null;
						Calendar calendar = Calendar.getInstance();
						Date timeStamp = calendar.getTime();

						boolean isAutoDelete = jsonObject.has("autoDelete") ? 
								(boolean) jsonObject.get("autoDelete") : false;
						if ("email".equalsIgnoreCase(conditionParameter)) {
							String fromEmailId = (String) dbObject.get("from_emailId");
							if (fromEmailId != null && fromEmailId.equalsIgnoreCase(conditionParameterValue)
									&& latestApplyOrderValue > applyOrder) {
								latestApplyOrderValue = applyOrder;
								dbObject.put("priorityLabel", priorityLabel);
								
								if(popupJsonObject != null){
									int popupTimeInSec = (int) popupJsonObject.get("popUpTime");
									calendar.add(Calendar.SECOND, popupTimeInSec);
									timeStamp = calendar.getTime();
									dbObject.put("read_popup_time", timeStamp);
								}
								
								if (isAutoDelete) {
									Calendar newCalendar = Calendar.getInstance();
									newCalendar.add(Calendar.SECOND, 172800);
									dbObject.put("auto_delete_time", newCalendar.getTime());
								}else {
									dbObject.removeField("auto_delete_time");
								}
								/*
								 * Apply Label to email
								 */
							} 
						} else if ("subject".equalsIgnoreCase(conditionParameter)) {
							String subject = (String) dbObject.get("subject");
							if (subject != null && subject.contains(conditionParameterValue)
									&& latestApplyOrderValue > applyOrder) {
								latestApplyOrderValue = applyOrder;
								dbObject.put("priorityLabel", priorityLabel);

								if(popupJsonObject != null){
									int popupTimeInSec = (int) popupJsonObject.get("popUpTime");
									calendar.add(Calendar.SECOND, popupTimeInSec);
									timeStamp = calendar.getTime();
									dbObject.put("read_popup_time", timeStamp);
								}
								if (isAutoDelete) {
									Calendar newCalendar = Calendar.getInstance();
									newCalendar.add(Calendar.SECOND, 172800);
									dbObject.put("auto_delete_time", newCalendar.getTime());
								}else {
									dbObject.removeField("auto_delete_time");
								}
								/*
								 * Apply Label to email
								 */
							} 
						}
					}

				}
			} 
			emailDao.addNewEmail(dbObject);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			logger.error("Exception Occured in applying priorities due to follwing reasons",exception);
			throw exception;
		}
	}

	/**
	 * The method to apply low priority to emails.
	 * 
	 * @param dbObject The email object
	 */
	private void applyLowPriorityToEmail(DBObject dbObject) {
		dbObject.put("priorityLabel", "Low");
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.add(Calendar.SECOND, 172800);
		dbObject.put("auto_delete_time", newCalendar.getTime());
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
			logger.debug("Exception Occured in mark email as read due to follwing reasons",exception);
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
			logger.error("Exception Occured in mark email as replied due to follwing reasons",exception);
			throw exception;
		}
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
			emailDao.deleteEmails();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			logger.error("Exception Occured in deleting due to follwing reasons",exception);
			throw exception;
		}
	}
	
	/**
	 * The method which returns all emails with expired read popup time from database.
	 * 
	 */
	@Override
	public JSONArray getAllEmailsWithExpiredReadPopupTime() throws Exception {
		JSONArray jsonArray = null;
		try {
			jsonArray = emailDao.getAllEmailsWithExpiredReadPopupTime();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			logger.error("Exception Occured in getting all expired read popu time expired email "
					+ "due to follwing reasons",exception);
			throw exception;
		}
		return jsonArray;
	}
	
	public static void applyPrioritiesToEmailMethod(String email) throws Exception {
		try {
			PriorityDaoImpl priorityDao = new PriorityDaoImpl();
			JSONArray priorityJsonArray = priorityDao.getAllPriorities();
			DBObject dbObject = (DBObject) JSON.parse(email);
			
			dbObject.put("priorityLabel", "Low");
			Calendar neCalendar = Calendar.getInstance();
			neCalendar.add(Calendar.SECOND, 172800);
			dbObject.put("auto_delete_time", neCalendar.getTime());
			
			int latestApplyOrderValue = 10000;
			if (priorityJsonArray != null && priorityJsonArray.length() > 0) {
				for (Object object : priorityJsonArray) {
					JSONObject jsonObject = (JSONObject) object;
					JSONObject conditionJsonObject = (JSONObject) jsonObject.get("condition");
					if (conditionJsonObject != null) {
						String conditionParameter = (String) conditionJsonObject.get("parameter");
						String conditionParameterValue = (String) conditionJsonObject.get("value");
						int applyOrder = (int) conditionJsonObject.get("applyOrder");
						String priorityLabel = (String) jsonObject.get("priorityLabel");

						JSONObject popupJsonObject = (JSONObject) jsonObject.get("popUp");
						Calendar calendar = Calendar.getInstance();
						Date timeStamp = calendar.getTime();

						boolean isAutoDelete = (boolean) jsonObject.get("autoDelete");
						if ("email".equalsIgnoreCase(conditionParameter)) {
							String fromEmailId = (String) dbObject.get("from_emailId");
							if (fromEmailId != null && fromEmailId.equalsIgnoreCase(conditionParameterValue)
									&& latestApplyOrderValue > applyOrder) {
								latestApplyOrderValue = applyOrder;
								dbObject.put("priorityLabel", priorityLabel);

								int popupTimeInSec = (int) popupJsonObject.get("popUpTime");
								calendar.add(Calendar.SECOND, popupTimeInSec);
								timeStamp = calendar.getTime();
								dbObject.put("read_popup_time", timeStamp);
								if (isAutoDelete) {
									Calendar newCalendar = Calendar.getInstance();
									newCalendar.add(Calendar.SECOND, 172800);
									dbObject.put("auto_delete_time", newCalendar.getTime());
								} else {
									dbObject.removeField("auto_delete_time");
								}
								
								 
							} 
						} else if ("subject".equalsIgnoreCase(conditionParameter)) {
							String subject = (String) dbObject.get("subject");
							if (subject != null && subject.contains(conditionParameterValue)
									&& latestApplyOrderValue > applyOrder) {
								latestApplyOrderValue = applyOrder;
								dbObject.put("priorityLabel", priorityLabel);

								int popupTimeInSec = (int) popupJsonObject.get("popUpTime");
								calendar.add(Calendar.SECOND, popupTimeInSec);
								timeStamp = calendar.getTime();
								dbObject.put("read_popup_time", timeStamp);
								if (isAutoDelete) {
									Calendar newCalendar = Calendar.getInstance();
									newCalendar.add(Calendar.SECOND, 172800);
									dbObject.put("auto_delete_time", newCalendar.getTime());
								}else {
									dbObject.removeField("auto_delete_time");
								} 
							} 
						}
					}

				}
			} 
			EmailDao emailDao = new EmailDaoImpl();
			emailDao.addNewEmail(dbObject);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			
			throw exception;
		}
	}
	
	public static void main(String[] args) throws Exception {
		//applyPrioritiesToEmailMethod("{\"from_emailId\": \"ceo@company.com\",\"to_emailId\": \"abcd@gmail.com\",\"subject\": \"TESTING Work Please Reply\",\"body\": \"Main mail body\",\"read\": false,\"replied\": false}");
		applyPrioritiesToEmailMethod("{\"read_popup_time\":{\"$date\":\"2017-10-22T10:39:47.082Z\"},\"priorityLabel\":\"High\",\"to_emailId\":\"abcd@gmail.com\",\"read\":true,\"replied\":true,\"subject\":\"Urgent Work Please Reply\",\"from_emailId\":\"manager@company.com\",\"_id\":{\"$oid\":\"59ec737f8d6ad29939a4be2f\"},\"auto_delete_time\":{\"$date\":\"2017-10-24T10:31:27.082Z\"},\"body\":\"Main mail body\",\"timestamp\":{\"$date\":\"2017-10-22T10:31:27.082Z\"}}");

	}

}
