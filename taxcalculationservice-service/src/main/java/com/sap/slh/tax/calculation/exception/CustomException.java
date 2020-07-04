package com.sap.slh.tax.calculation.exception;

import java.util.Map;

import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;

public class CustomException extends RuntimeException{

	/** serialVersionUID. */
	private static final long serialVersionUID = 2837508888552870730L;

	/** The status code. */
	private TaxCalculationProcessingStatusCode processingStatusCode;
	private TaxCalculationApplicationError error;
	private transient Map<String, Object> debugInfo;
	private TaxCalculationStatus status;

	public CustomException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode) {
		super(message);
		this.processingStatusCode = processingStatusCode;
	}

	public CustomException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			Map<String, Object> debugInfo) {
		super(message);
		this.processingStatusCode = processingStatusCode;
		this.debugInfo = debugInfo;
	}

	public CustomException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			TaxCalculationApplicationError error) {
		super(message);
		this.processingStatusCode = processingStatusCode;
		this.setError(error);

	}

	public CustomException(final String message, final Throwable exception,
			final TaxCalculationProcessingStatusCode processingStatusCode) {
		super(message, exception);
		this.processingStatusCode = processingStatusCode;
	}

	public CustomException(final String message, final Throwable exception,
			final TaxCalculationProcessingStatusCode processingStatusCode, final TaxCalculationStatus status) {
		super(message, exception);
		this.processingStatusCode = processingStatusCode;
		this.setStatus(status);
	}

	public CustomException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			final TaxCalculationStatus status) {
		super(message);
		this.processingStatusCode = processingStatusCode;
		this.setStatus(status);
	}

	public CustomException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			final TaxCalculationStatus status, final TaxCalculationApplicationError error) {
		super(message);
		this.processingStatusCode = processingStatusCode;
		this.setStatus(status);
		this.error = error;
	}
	
	public CustomException(final String message, final Throwable exception) {
		super(message, exception);
	}

	public TaxCalculationProcessingStatusCode getProcessingStatusCode() {
		return processingStatusCode;
	}

	public Map<String, Object> getDebugInfo() {
		return debugInfo;
	}

	public TaxCalculationApplicationError getError() {
		return error;
	}

	public void setError(TaxCalculationApplicationError error) {
		this.error = error;
	}

	public TaxCalculationStatus getStatus() {
		return status;
	}

	public void setStatus(TaxCalculationStatus status) {
		this.status = status;
	}

}
