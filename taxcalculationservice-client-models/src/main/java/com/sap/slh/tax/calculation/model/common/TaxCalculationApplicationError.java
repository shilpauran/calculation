package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxCalculationApplicationError implements Serializable {

	private static final long serialVersionUID = 1602782579598786214L;

	private String errorId;
	
	private List<TaxCalculationErrorDetail> details;

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	public List<TaxCalculationErrorDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TaxCalculationErrorDetail> details) {
		this.details = details;
	}
      
    
    

  

    
}
