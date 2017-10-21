package com.prioritization.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.prioritization.service.EmailService;
import com.prioritization.service.EmailServiceImpl;
import com.prioritization.service.PriorityService;
import com.prioritization.service.PriorityServiceImpl;

@Path("/emailService")
public class EmailResource {

	private static EmailService emailService = new EmailServiceImpl();
	
	@GET
	@Path("/getMail/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEmail(@PathParam("id") String emailId) {
		JSONObject emailJson;
		try {
			emailJson = emailService.getEmailById(emailId);
			if (emailJson == null) {
				return Response.status(200).entity("Emails with Higher Priority are unread. read them first.").build();
			} else {
				return Response.status(200).entity(emailJson.toString()).build();
			}
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}
	}
	
	@POST
	@Path("/applyPriorities")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response applyPrioritiesToEmail(String priority) {
		System.out.println("In this method POSt" + priority);
		try {
			emailService.applyPrioritiesToEmail(priority);
			return Response.status(200).entity("priority applied to email successfully !!!!!!").build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}

	}
	
	@POST
	@Path("/markRead/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response markEmailAsRead(@PathParam("id") String emailId) {
		//System.out.println("In this method POSt" + priority);
		try {
			emailService.markEmailAsRead(emailId);
			return Response.status(200).entity("email Marked as read !!!!!!").build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}

	}
	
	@POST
	@Path("/markReplied/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response markEmailAsReplied(@PathParam("id") String emailId) {
		try {
			emailService.markEmailAsReplied(emailId);
			return Response.status(200).entity("email Marked as replied !!!!!!").build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}

	}
	
	@DELETE
	@Path("/autoDelete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autoDeleteEmails() {
		try {
			emailService.deleteEmails();
			return Response.status(200).entity("Email Deleted Message!!!!!!").build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			return Response.status(503).entity("Service Unavailable = " + exception.getMessage()).build();
		}
	}
}
