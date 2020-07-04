package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxCalculationResponse implements Serializable{
	

	private static final long serialVersionUID = 6469639433737806274L;

	private TaxCalculationStatus status;

    private TaxCalculationProcessingStatusCode processingStatusCode;

    private String statusMessage;
  
    private TaxCalculationResponseLine result;

    private TaxCalculationApplicationError error;
    
    private TaxCalculationApplicationWarning warning;

	public TaxCalculationStatus getStatus() {
		return status;
	}

	public void setStatus(TaxCalculationStatus status) {
		this.status = status;
	}

	public TaxCalculationProcessingStatusCode getProcessingStatusCode() {
		return processingStatusCode;
	}

	public void setProcessingStatusCode(TaxCalculationProcessingStatusCode processingStatusCode) {
		this.processingStatusCode = processingStatusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public TaxCalculationResponseLine getResult() {
		return result;
	}

	public void setResult(TaxCalculationResponseLine result) {
		this.result = result;
	}

	public TaxCalculationApplicationError getError() {
		return error;
	}

	public void setError(TaxCalculationApplicationError error) {
		this.error = error;
	}

	public TaxCalculationApplicationWarning getWarning() {
		return warning;
	}

	public void setWarning(TaxCalculationApplicationWarning warning) {
		this.warning = warning;
	}

}
