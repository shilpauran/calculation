package com.sap.slh.tax.calculation.service;

import java.util.Locale;

import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;

public interface TaxCalculationService {
	
	public TaxCalculationResponse calculateTax(
			final TaxCalculationRequest taxCalculationRequest,Locale locale,final String tenantId);

}
