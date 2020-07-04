package com.sap.slh.tax.calculation.model;

import java.io.Serializable;

import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationWarning;

public class TaxCalculationValidationResult implements Serializable {

	private static final long serialVersionUID = 1602782579598786214L;

	private boolean isValid;

	private TaxCalculationApplicationError taxCalculationErrors;

	private TaxCalculationApplicationWarning taxCalculationWarnings;

	public TaxCalculationApplicationWarning getTaxCalculationWarnings() {
		return taxCalculationWarnings;
	}

	public void setTaxCalculationWarnings(TaxCalculationApplicationWarning taxCalculationWarnings) {
		this.taxCalculationWarnings = taxCalculationWarnings;
	}

	public TaxCalculationApplicationError getTaxCalculationErrors() {
		return taxCalculationErrors;
	}

	public void setTaxCalculationErrors(TaxCalculationApplicationError taxCalculationErrors) {
		this.taxCalculationErrors = taxCalculationErrors;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
