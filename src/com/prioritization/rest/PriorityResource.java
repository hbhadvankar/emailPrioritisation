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

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.util.JSONParseException;
import com.prioritization.service.PriorityService;
import com.prioritization.service.PriorityServiceImpl;

@Path("/priority")
public class PriorityResource {

	final static Logger logger = Logger.getLogger(PriorityResource.class);
	private static PriorityService priorityService = new PriorityServiceImpl();

	@GET
	@Path("/getAllPriorities")
	public Response getAllPriorities() {

		JSONArray prioritiesJsonArray;
		try {
			logger.debug("in getAllPriorities.");
			prioritiesJsonArray = priorityService.getAllPriorities();
			if (prioritiesJsonArray != null && prioritiesJsonArray.length() > 0) {
				logger.debug("Total Prioritites Fetched = " + prioritiesJsonArray.length());
				return Response.ok(prioritiesJsonArray.toString(), MediaType.APPLICATION_JSON).build();
			}
		} catch (Exception exception) {
			if (exception instanceof JSONParseException) {
				return Response.status(Response.Status.OK).entity("invalid data " + exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}
		return Response.status(Response.Status.OK).entity("Currently There are no priorities to display..").build();
	}

	@POST
	@Path("/createPriority")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewPriority(String priority) {
		try {
			logger.debug("in createNewPriority.");
			if (priority == null || priority.trim().length() == 0) {
				return Response.status(Response.Status.OK).entity("priority json cannot be blank").build();
			} else {
				priorityService.createPriority(priority);
				return Response.status(Response.Status.OK).entity("priority created successfully !!!!!!").build();
			}
		} catch (Exception exception) {
			if (exception instanceof IllegalArgumentException) {
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException) {
				return Response.status(Response.Status.OK).entity("Invalid Json " + exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}
	}

	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getPriorityById(@PathParam("id") String priorityId) {
		JSONObject priorityJson;
		try {
			logger.debug("in getPriorityById.");
			if (priorityId == null || priorityId.trim().length() == 0) {
				return Response.status(Response.Status.OK).entity(" priorityId cannot be blank").build();
			} else {
				priorityJson = priorityService.getPriorityById(priorityId);
				if (priorityJson != null) {
					return Response.ok(priorityJson.toString(), MediaType.APPLICATION_JSON).build();
				}
			}
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			if (exception instanceof IllegalArgumentException) {
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException) {
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}
		return Response.status(Response.Status.OK).entity("Priority not found..").build();
	}

	@POST
	@Path("/updatePriority")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePriority(String priority) {
		try {
			logger.debug("in updatePriority.");
			if (priority == null || priority.trim().length() == 0) {
				return Response.status(Response.Status.OK).entity("priority json cannot be blank").build();
			} else {
				priorityService.updatePriority(priority);
				return Response.status(200).entity("priority updated successfully !!!!!!").build();
			}
		} catch (Exception exception) {
			if (exception instanceof IllegalArgumentException) {
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException) {
				return Response.status(Response.Status.OK).entity("Invalid Json " + exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}

	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePriorityById(@PathParam("id") String priorityId) {
		try {
			logger.debug("in deletePriorityById.");
			if (priorityId == null || priorityId.trim().length() == 0) {
				return Response.status(Response.Status.OK).entity("priority id cannot be blank").build();
			} else {
				priorityService.deletePriorityById(priorityId);
				return Response.status(200).entity("priority deleted successfully !!!!!!").build();
			}
		} catch (Exception exception) {
			if (exception instanceof IllegalArgumentException) {
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException) {
				return Response.status(Response.Status.OK).entity("Invalid Json " + exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}
	}

	public static Response getPriorityByIdMethod(String priorityId) {
		JSONObject priorityJson;
		try {
			logger.debug("in getPriorityById.");
			if (priorityId == null || priorityId.trim().length() == 0) {
				return Response.status(Response.Status.OK).entity(" priorityId cannot be blank").build();
			} else {
				priorityJson = priorityService.getPriorityById(priorityId);
				if (priorityJson != null) {
					return Response.status(Response.Status.OK).entity(priorityJson.toString()).build();
				}
			}
		} catch (Exception exception) {
			if (exception instanceof IllegalArgumentException) {
				Response response = Response.status(Response.Status.OK).entity(exception.getMessage()).build();
				return response;
			}
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}
		return Response.status(Response.Status.OK).entity("Priority not found..").build();
	}

	public static void main(String[] args) {
		getPriorityByIdMethod("adafascasda");
	}
}