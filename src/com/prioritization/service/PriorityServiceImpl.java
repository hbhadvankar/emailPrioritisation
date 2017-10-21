package com.prioritization.service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.prioritization.dao.PriorityDaoImpl;

public class PriorityServiceImpl implements PriorityService {

	private PriorityDaoImpl priorityDao;

	public PriorityServiceImpl() {
		priorityDao = new PriorityDaoImpl();
	}

	@Override
	public void updatePriority(String priority) throws Exception{
		try {
			priorityDao.updatePriority(priority);
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
	 * The method to insert a new priority in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public void createPriority(String priority) throws Exception {
		// TODO Auto-generated method stub
		try {
			priorityDao.createPriority(priority);
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
	 * The method that returns all priorities.
	 * 
	 * @return The JSONArray Object which contains all priorities.
	 */
	@Override
	public JSONArray getAllPriorities() throws Exception {
		JSONArray jsonArray = null;
		try {
			jsonArray = priorityDao.getAllPriorities();
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
		return jsonArray;
	}

	/**
	 * The method that returns a priority json object based on priority id.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	@Override
	public JSONObject getPriorityById(String priorityId) throws Exception {
		JSONObject jsonObject = null;
		try {
			jsonObject = priorityDao.getPriorityById(priorityId);
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
	 * The method that deletes a priority json object based on priority id.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	@Override
	public void deletePriorityById(String priorityId) throws Exception {
		try {
			priorityDao.deletePriorityById(priorityId);
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

}
