package com.sap.slh.tax.calculation.exception;

import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

public class DependencyVcapRetrievalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1403467824590391475L;
    private final String vcapEntry;

    public DependencyVcapRetrievalException(String vcapEntry) {
        super(String.format(TaxCalculationConstants.VCAPENTRY_MSG, vcapEntry));
        this.vcapEntry = vcapEntry;
    }

    public String getVcapEntry() {
        return this.vcapEntry;
    }

}
