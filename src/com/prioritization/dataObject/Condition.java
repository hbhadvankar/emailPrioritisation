package com.prioritization.dataObject;

import com.owlike.genson.annotation.JsonProperty;

/**
 * The class for priority Condition.
 * 
 * @author Harish
 *
 */
public class Condition {
	//@JsonProperty
	private String parameter;
	///@JsonProperty
	private String parameterValue;
	//@JsonProperty
	private int applyOrder;
	
	
	
	public Condition(String parameter, String parameterValue, int applyOrder) {
		super();
		this.parameter = parameter;
		this.parameterValue = parameterValue;
		this.applyOrder = applyOrder;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	public int getApplyOrder() {
		return applyOrder;
	}
	public void setApplyOrder(int applyOrder) {
		this.applyOrder = applyOrder;
	}
	
	
}
