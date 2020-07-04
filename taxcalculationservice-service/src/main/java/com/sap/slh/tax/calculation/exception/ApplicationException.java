package com.sap.slh.tax.calculation.exception;

import java.util.Map;

import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;

/**
 * This class is used to handle application level exceptions and extends
 * <code>RuntimeException</code>.
 * 
 * @author
 */
public class ApplicationException extends CustomException {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2837508888552870730L;

	
	public ApplicationException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode) {
		super(message,processingStatusCode);	
	}

	public ApplicationException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			Map<String, Object> debugInfo) {
		super(message,processingStatusCode,debugInfo);		
	}

	public ApplicationException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			TaxCalculationApplicationError error) {
		super(message,processingStatusCode,error);
	}

	public ApplicationException(final String message, final Throwable exception,
			final TaxCalculationProcessingStatusCode processingStatusCode) {
		super(message, exception,processingStatusCode);	
	}

	public ApplicationException(final String message, final Throwable exception,
			final TaxCalculationProcessingStatusCode processingStatusCode, final TaxCalculationStatus status) {
		super(message, exception,processingStatusCode,status);	
	}

	public ApplicationException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			final TaxCalculationStatus status) {
		super(message,processingStatusCode,status);	
	}

	public ApplicationException(final String message, final TaxCalculationProcessingStatusCode processingStatusCode,
			final TaxCalculationStatus status, final TaxCalculationApplicationError error) {
		super(message,processingStatusCode,status,error);	
	}
	
	public ApplicationException(final String message, final Throwable exception) {
		super(message, exception);
	}
}
