package com.sap.slh.tax.calculation.exception;

import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;

public class InvalidRequestException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2719071740043310352L;

	public InvalidRequestException(TaxCalculationApplicationError error) {
		super(TaxCalculationProcessingStatusCode.REQUEST_NOT_VALID.getValue(),
				TaxCalculationProcessingStatusCode.REQUEST_NOT_VALID, TaxCalculationStatus.INVALID_REQUEST, error);

	}

}
