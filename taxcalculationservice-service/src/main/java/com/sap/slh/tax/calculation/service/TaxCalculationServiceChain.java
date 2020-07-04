package com.sap.slh.tax.calculation.service;

import java.util.List;

import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;

public abstract class TaxCalculationServiceChain {

	/** The successor. */
	protected TaxCalculationServiceChain successor;
	
	/**
	 * Sets the successor.
	 *
	 * @param successor the new successor
	 */
	public void setSuccessor(TaxCalculationServiceChain successor) {
		this.successor = successor;
	}

	/**
	 * .
	 *
	 * @param taxAttributesDeterminationInputDto
	 * @return the TaxAttributesDeterminationOutputDto
	 */
	public abstract List<TaxCalculationOutputDTO> calculateTax(List<TaxCalculationInputDTO> taxCalculationInput,
			String tenantId);

	/**
	 * Checks if is valid.
	 *
	 * @param taxCalculationInputDto the tax calculation request
	 * @return true, if is valid
	 */
	protected boolean isValid(List<TaxCalculationInputDTO> taxCalculationInput) {
		
		if (taxCalculationInput != null) {
			return true;
		}
		throw new ApplicationException(TaxCalculationProcessingStatusCode.EMPTY_REQUEST.getValue(),
				TaxCalculationProcessingStatusCode.EMPTY_REQUEST, TaxCalculationStatus.FAILURE);
	}

}