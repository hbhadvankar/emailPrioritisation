package com.prioritization.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface PriorityService {

	/**
	 * The method to update existing priority in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public void updatePriority(String priority) throws Exception;

	/**
	 * The method to insert a new priority in database.
	 * 
	 * @param priority
	 *            The json which is represented in string format.
	 */
	public void createPriority(String priority) throws Exception;

	/**
	 * The method that returns all priorities.
	 * 
	 * @return The JSONArray Object which contains all priorities.
	 */
	public JSONArray getAllPriorities() throws Exception;

	/**
	 * The method that returns a priority json object based on priority id.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	public JSONObject getPriorityById(String priorityId) throws Exception;

	/**
	 * The method that deletes a priority json object based on priority id.
	 * 
	 * @param priorityId
	 *            The priority id.
	 * @return The priority Json Object.
	 */
	public void deletePriorityById(String priorityId) throws Exception;
}
