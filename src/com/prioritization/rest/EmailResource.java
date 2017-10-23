package com.prioritization.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.util.JSONParseException;
import com.prioritization.service.EmailService;
import com.prioritization.service.EmailServiceImpl;

@Path("/emailService")
public class EmailResource {
	
	final static Logger logger = Logger.getLogger(EmailResource.class);
	private static EmailService emailService = new EmailServiceImpl();

	@GET
	@Path("/getMail/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getEmail(@PathParam("id") String emailId) {
		JSONObject emailJson;
		try {
			logger.debug("Resource method for getting an email.");
			if (emailId == null || emailId.trim().length() == 0){
				return Response.status(Response.Status.OK).entity("email db id cannot be blank").build();
			} else {
			emailJson = emailService.getEmailById(emailId);
			if (emailJson != null) {
				
				return Response.ok(emailJson.toString(), MediaType.APPLICATION_JSON).build();
			}
			}
		} catch (Exception exception) {
			if(exception instanceof IllegalArgumentException){
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException){
				return Response.status(Response.Status.OK).entity("Invalid Json "+exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}
		return Response.status(Response.Status.OK).entity("Emails with Higher Priority are unread. read them first.").build();
	}

	@POST
	@Path("/applyPriorities")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response applyPrioritiesToEmail(String email) {
		try {
			logger.debug("Resource method for applying priorities to an email.");
			if(email == null || email.trim().length() ==0){
				return Response.status(Response.Status.OK).entity("email json cannot be blank").build();
			} else {
				emailService.applyPrioritiesToEmail(email);
				logger.debug("priority applied to email successfully !!!!!!");
				return Response.status(Response.Status.OK).entity("priority applied to email successfully !!!!!!").build();
			}
			
		} catch (Exception exception) {
			if(exception instanceof IllegalArgumentException){
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException){
				return Response.status(Response.Status.OK).entity("Invalid Json "+exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}

	}

	@POST
	@Path("/markRead/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response markEmailAsRead(@PathParam("id") String emailId) {
		try {
			logger.debug("mark email as read for id - "+emailId);
			if(emailId == null || emailId.trim().length() ==0){
				return Response.status(Response.Status.OK).entity("email db id cannot be blank").build();
			} else {
			emailService.markEmailAsRead(emailId);
			logger.debug("email successfully marked as read!!!!!!");
			return Response.status(Response.Status.OK).entity("email Marked as read !!!!!!").build();
			}
		} catch (Exception exception) {
			if(exception instanceof IllegalArgumentException){
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException){
				return Response.status(Response.Status.OK).entity("Invalid Json "+exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}

	}

	@POST
	@Path("/markReplied/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response markEmailAsReplied(@PathParam("id") String emailId) {
		try {
			logger.debug("mark email as replied for id - "+emailId);
			if(emailId == null || emailId.trim().length() ==0){
				return Response.status(Response.Status.OK).entity("email db id cannot be blank").build();
			} else {
			emailService.markEmailAsReplied(emailId);
			logger.debug("email successfully marked as replied!!!!!!");
			return Response.status(200).entity("email Marked as replied !!!!!!").build();
			}
		} catch (Exception exception) {
			if(exception instanceof IllegalArgumentException){
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException){
				return Response.status(Response.Status.OK).entity("Invalid Json "+exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}

	}

	@DELETE
	@Path("/autoDelete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autoDeleteEmails() {
		try {
			logger.debug("resource method for deteling emails");
			emailService.deleteEmails();
			logger.debug("Emails Deleted successfully!!!!!!");
			return Response.status(Response.Status.OK).entity("Email Deleted Message!!!!!!").build();
		} catch (Exception exception) {
			if(exception instanceof IllegalArgumentException){
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException){
				return Response.status(Response.Status.OK).entity("Invalid Json "+exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}
	}

	@GET
	@Path("/getAllReadPopupExpiredEmails")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllEmailsWithExpiredReadPopupTime() {
		JSONArray emailJsonArray;
		try {
			logger.debug("resource method for fetching emails which has read popup time expired.");
			emailJsonArray = emailService.getAllEmailsWithExpiredReadPopupTime();
			if (emailJsonArray != null && emailJsonArray.length() > 0) {
				logger.debug("returning all read popup time expired emails.");
				return Response.ok(emailJsonArray.toString(), MediaType.APPLICATION_JSON).build();
			}
		} catch (Exception exception) {
			if(exception instanceof IllegalArgumentException){
				return Response.status(Response.Status.OK).entity(exception.getMessage()).build();
			} else if (exception instanceof JSONParseException){
				return Response.status(Response.Status.OK).entity("Invalid Json "+exception.getMessage()).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
			}
		}
		return Response.status(Response.Status.OK).entity("Currently There are no Emails to display..").build();
	}
}
