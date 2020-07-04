package com.sap.slh.tax.calculation.exception;

import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;

public class TenantNotFoundException extends ApplicationException {

	
	private static final long serialVersionUID = 5357230313159427036L;
	
	public TenantNotFoundException()
	{
		super(TaxCalculationProcessingStatusCode.TENANT_ID_NOT_FOUND.getValue(),
					TaxCalculationProcessingStatusCode.TENANT_ID_NOT_FOUND, TaxCalculationStatus.FAILURE);
	}
	
	

}
