package com.sap.slh.tax.calculation.dto;

import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.model.common.TaxLine.DueCategoryCode;

public class TaxCalculationRequestCheckFieldsDTO {
	 
	 private Boolean isTaxEventNonTaxable;
	 private TaxLine.DueCategoryCode dueCategoryCode;
	 private Double taxRate;
	 private Double nonDeductibilityTaxRate;
	 
	public TaxLine.DueCategoryCode getDueCategoryCode() {
		return dueCategoryCode;
	}
	
	public void setDueCategoryCode(DueCategoryCode dueCategoryCode2) {
		this.dueCategoryCode = dueCategoryCode2;
	}
	
	public Boolean getIsTaxEventNonTaxable() {
		return isTaxEventNonTaxable;
	}
	
	public void setIsTaxEventNonTaxable(Boolean isTaxEventNonTaxable) {
		this.isTaxEventNonTaxable = isTaxEventNonTaxable;
	}
	
	public Double getNonDeductibilityTaxRate() {
		return nonDeductibilityTaxRate;
	}
	
	public void setNonDeductibilityTaxRate(Double nonDeductibilityTaxRate) {
		this.nonDeductibilityTaxRate = nonDeductibilityTaxRate;
	}
	
	public Double getTaxRate() {
		return taxRate;
	}
	
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
}