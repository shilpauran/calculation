package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxCalculationErrorDetail implements Serializable {

	private static final long serialVersionUID = 4628171657114847851L;
	private String field;
	private String message;
	private TaxCalculationErrorMessage errorCode;

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

	public void setErrorCode(TaxCalculationErrorMessage valueOf) {
		// TODO Auto-generated method stub
		this.errorCode = valueOf;
	}

	public TaxCalculationErrorMessage getErrorCode() {
		return errorCode;
	}

}
