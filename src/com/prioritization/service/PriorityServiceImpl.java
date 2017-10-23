package com.prioritization.service;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.prioritization.dao.PriorityDaoImpl;

public class PriorityServiceImpl implements PriorityService {
	final static Logger logger = Logger.getLogger(PriorityServiceImpl.class);
	private PriorityDaoImpl priorityDao;

	public PriorityServiceImpl() {
		priorityDao = new PriorityDaoImpl();
	}

	@Override
	public void updatePriority(String priority) throws Exception {
		try {
			logger.debug("in updatePriority.");
			priorityDao.updatePriority(priority);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
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
			logger.debug("in createPriority service method.");
			priorityDao.createPriority(priority);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
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
			logger.debug("in getAllPriorities service method.");
			jsonArray = priorityDao.getAllPriorities();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
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
			logger.debug("in getPriorityById service method.");
			jsonObject = priorityDao.getPriorityById(priorityId);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
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
			logger.debug("in deletePriorityById service method.");
			priorityDao.deletePriorityById(priorityId);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			throw exception;
		}
	}

}
