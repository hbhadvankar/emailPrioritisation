package com.prioritization.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.prioritization.service.PriorityService;
import com.prioritization.service.PriorityServiceImpl;

@Path("/priority")
public class PriorityResource {

	private static PriorityService priorityService = new PriorityServiceImpl();

	@GET
	@Path("/getAllPriorities")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPriorities() {

		JSONArray prioritiesJsonArray;
		try {
			prioritiesJsonArray = priorityService.getAllPriorities();
			if (prioritiesJsonArray != null && prioritiesJsonArray.length() > 0) {
				return Response.status(200).entity(prioritiesJsonArray.toString()).build();
			}
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}
		return Response.status(204).entity("Currently There are no priorities to display..").build();
	}

	@POST
	@Path("/createPriority")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewPriority(String priority) {
		System.out.println("In this method POSt" + priority);
		try {
			priorityService.createPriority(priority);
			return Response.status(200).entity("priority created successfully !!!!!!").build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}

	}

	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getPriorityById(@PathParam("id") String priorityId) {
		JSONObject priorityJson;
		try {
			priorityJson = priorityService.getPriorityById(priorityId);
			if (priorityJson != null) {
				return Response.status(200).entity(priorityJson.toString()).build();
			}
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}
		return Response.status(204).entity("Prioritynot found..").build();
	}

	@POST
	@Path("/updatePriority")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePriority(String priority) {
		System.out.println("In this method POSt" + priority);
		try {
			priorityService.updatePriority(priority);
			return Response.status(200).entity("priority updated successfully !!!!!!").build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePriorityById(@PathParam("id") String priorityId) {
		try {
			priorityService.deletePriorityById(priorityId);
			return Response.status(200).entity("priority deleted successfully !!!!!!").build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}
	}

	/**
	 * Have Commented the method below just in case for reference.
	 * 
	 */
	/*
	 * @POST
	 * 
	 * @Path("/createPriority")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON) public Response
	 * createNewPriority(String priority) {
	 * System.out.println("In this method POSt"+priority);
	 * priorityService.createPriority(priority);
	 * System.out.println("PriorityLabel  == "+priority.getPrioritylabel());
	 * System.out.println("PriorityValue  == "+priority.getPriorityValue());
	 * System.out.println("ReadPopupTime  == "+priority.getReadPopupTime());
	 * 
	 * System.out.println("MustReply  == "+priority.getMustReply());
	 * System.out.println("AutoDelete  == "+priority.getAutoDelete());
	 * System.out.println("Condition->Parameter  == "+priority.getCondition().
	 * getParameter());
	 * System.out.println("Condition->ParameterValue  == "+priority.getCondition
	 * ().getParameterValue());
	 * System.out.println("Condition->ApplyOrder  == "+priority.getCondition().
	 * getApplyOrder());
	 * 
	 * ///priority.setCondition(new Condition("Subject", "abcd@gmail.com", 1));
	 * 
	 * priority.setMustReply(Boolean.TRUE);
	 * priority.setAutoDelete(Boolean.FALSE);
	 * 
	 * return Response.status(200).entity("We got your priority!!!!!!").build();
	 * }
	 */
}