package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxCalculationApplicationWarning implements Serializable {

	private static final long serialVersionUID = -6682351420214005671L;
	private List<TaxCalculationWarningDetail> details;

	public List<TaxCalculationWarningDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TaxCalculationWarningDetail> details) {
		this.details = details;
	}

}
