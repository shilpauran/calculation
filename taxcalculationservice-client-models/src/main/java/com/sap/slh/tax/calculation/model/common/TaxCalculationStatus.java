package com.sap.slh.tax.calculation.model.common;

public enum TaxCalculationStatus {

	SUCCESS(200, "Operation is successful."),

	FAILURE(500, "Operation Failed."),
	
	SERVICE_UNAVAILABLE(503,"Service Unavailable"),

	PARTIAL_CONTENT(206, "Partial Content"),
	
	NO_CONTENT(204, "No Content"),

	REQUEST_DENIED(403, "Request was denied."),

	INVALID_REQUEST(400, "The request is invalid.");

	/** The string value of given enum status. */
	private String value;
	
	private int code;

	/**
	 * Instantiates a new response value.
	 *
	 * @param value the value
	 */
	TaxCalculationStatus(final int code ,final String value) {
		this.value = value;
		this.code = code;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}
	/**
	 * 
	 * @return code
	 */
	public int getCode() {
		return code;
	}

}
