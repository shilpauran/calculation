package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;

public class TaxCalculationWarningDetail implements Serializable{
	
	private static final long serialVersionUID = -2108817461660743313L;
	private String field;
	private String message;
	
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}