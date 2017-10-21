package com.prioritization.dataObject;

import com.owlike.genson.annotation.JsonProperty;

/**
 * The class for Priority.
 * 
 * @author Harish
 *
 */

public class Priority {

	/**
	 * Example Data given below.
	 * ### Priority - 1
	 * "{"
   		 + "	\"userId\" : \"harish\","
   		 + "	\"priorityLabel\" : \"myLabel\","
   		 + "	\"priorityValue\" : 1,"
   		 + "	\"condition\" : {"
   		 + "		\"parameter\" : \"email\","
   		 + "		\"value\" : \"abcd@gmail.com\","
   		 + "		\"applyOrder\" : 2"
   		 + "	},"
   		 + "	\"readPopupTime\" : 600,"
   		 + "	\"mustReply\" : true,"
   		 + "	\"autoDelete\" : false,"
   		 + "}";
   		 
   		 ### Priority - 2
   		 "{"
   		 + "	\"userId\" : \"harish\","
   		 + "	\"priorityLabel\" : \"myLabel1\","
   		 + "	\"priorityValue\" : 2,"
   		 + "	\"condition\" : {"
   		 + "		\"parameter\" : \"subject\","
   		 + "		\"value\" : \"limited stock\","
   		 + "		\"applyOrder\" : 1"
   		 + "	},"
   		 + "	\"readPopupTime\" : 1200,"
   		 + "	\"mustReply\" : false,"
   		 + "	\"autoDelete\" : true,"
   		 + "	\"autoDeleteTime\" : 0"
   		 + "}";
	 */
	private String id;
	//@JsonProperty
	private String prioritylabel;
	//@JsonProperty
	private int priorityValue;
	//@JsonProperty
	private Condition condition;
	//@JsonProperty
	private Long readPopupTime;
	//@JsonProperty
	private Boolean mustReply;
	//@JsonProperty
	private Boolean autoDelete;
	//@JsonProperty
	private Long autoDeleteTime;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrioritylabel() {
		return prioritylabel;
	}
	public void setPrioritylabel(String prioritylabel) {
		this.prioritylabel = prioritylabel;
	}
	public int getPriorityValue() {
		return priorityValue;
	}
	public void setPriorityValue(int priorityValue) {
		this.priorityValue = priorityValue;
	}
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	public Long getReadPopupTime() {
		return readPopupTime;
	}
	public void setReadPopupTime(Long readPopupTime) {
		this.readPopupTime = readPopupTime;
	}
	public Boolean getMustReply() {
		return mustReply;
	}
	public void setMustReply(Boolean mustReply) {
		this.mustReply = mustReply;
	}
	public Boolean getAutoDelete() {
		return autoDelete;
	}
	public void setAutoDelete(Boolean autoDelete) {
		this.autoDelete = autoDelete;
	}
	public Long getAutoDeleteTime() {
		return autoDeleteTime;
	}
	public void setAutoDeleteTime(Long autoDeleteTime) {
		this.autoDeleteTime = autoDeleteTime;
	}
	
	
	
}
