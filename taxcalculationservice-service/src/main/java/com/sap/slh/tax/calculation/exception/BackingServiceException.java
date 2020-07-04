package com.sap.slh.tax.calculation.exception;
import java.util.Map;

import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;

/**
 * This class is used to handle backing services exceptions and extends
 * <code>RuntimeException</code>.
 * 
 */

public class BackingServiceException extends CustomException {
	
	/** serialVersionUID. */
	private static final long serialVersionUID = 2837508888552870730L;
	
	public BackingServiceException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode) {
		super(message,processingStatusCode);	
	}

	public BackingServiceException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			Map<String, Object> debugInfo) {
		super(message,processingStatusCode,debugInfo);		
	}

	public BackingServiceException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			TaxCalculationApplicationError error) {
		super(message,processingStatusCode,error);
	}

	public BackingServiceException(final String message, final Throwable exception,
			final TaxCalculationProcessingStatusCode processingStatusCode) {
		super(message, exception,processingStatusCode);	
	}

	public BackingServiceException(final String message, final Throwable exception,
			final TaxCalculationProcessingStatusCode processingStatusCode, final TaxCalculationStatus status) {
		super(message, exception,processingStatusCode,status);	
	}

	public BackingServiceException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			final TaxCalculationStatus status) {
		super(message,processingStatusCode,status);	
	}

	public BackingServiceException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			final TaxCalculationStatus status, final TaxCalculationApplicationError error) {
		super(message,processingStatusCode,status,error);	
	}
	
	public BackingServiceException(final String message, final Throwable exception) {
		super(message, exception);
	}
}
