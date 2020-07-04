package com.sap.slh.tax.calculation.model.api.design.time;

import java.io.Serializable;

public class ApplicationError implements Serializable {

	private static final long serialVersionUID = -5458197266537466174L;

	private String errorId;
	
	private String errorDetails;
	
	public String getErrorId() {
		return errorId;
	}
	
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	
	public String getErrorDetails() {
		return errorDetails;
	}
	
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	
	

}
