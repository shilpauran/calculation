package com.sap.slh.tax.calculation.model.common;

public enum TaxCalculationProcessingStatusCode {
	
	   TAX_CALCULATED_SUCCESSFULLY("Tax calculated successfully"),
	   TAX_CALCULATED_SUCCESSFULLY_WITH_WARNINGS("Tax calculated successfully, but there are some warnings. Please check the log for details."),
	   TAX_CALCULATION_FAILURE("Unable to calculate tax; check the log for details."),
	   REQUEST_NOT_VALID("Bad request"),       
	   EMPTY_REQUEST("The request body is empty. Please check the request and try again."),
	   AUTHENTICATION_FAILURE("There was an authentication issue; please check the log."),
	   TAX_RULE_SERVICE_FAILURE("The Tax Rule service failed."),
	   OPERATION_UNSUPPORTED_ERROR("Operation unsupported error occurred"),
	   CONTENT_NOT_FOUND("Content not found; no suitable rules are available to calculate the tax amounts in line with the request."),
	   PARTIAL_CONTENT("Not all content found; not all the rules required to calculate the tax amounts in line with the request are available."),
	   TENANT_ID_NOT_FOUND("Tenant ID not found"),
	   INTERNAL_SERVER_ERROR("Due to an internal service error, the request was not processed. Please check the log and try again later."), 
	   INVALID_PARAMETER("Invalid parameter."),
	   SERVICE_UNAVAILABLE("service is unavailable at this moment. please try again later");
	
	 private String value;

	   /**
	    * Instantiates a new response value.
	    *
	    * @param value
	    *            the value
	    */
	   private TaxCalculationProcessingStatusCode(final String value) {
	       this.value = value;
	   }

	   /**
	    * Gets the value.
	    *
	    * @return the value
	    */
	   public final String getValue() {
	       return value;
	   }

}
