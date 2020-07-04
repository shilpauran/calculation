package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"taxCalculationResponses"
})
public class TaxCalculationResult implements Serializable{

	private static final long serialVersionUID = 2912124485938823162L;
	
	@JsonProperty("taxCalculationResponses")
	private List<TaxCalculationResponseLine> taxCalculationResponses;
	
	@JsonProperty("taxCalculationResponses")
	public List<TaxCalculationResponseLine> getTaxCalculationResponses() {
		return taxCalculationResponses;
	}

	@JsonProperty("taxCalculationResponses")
	public void setTaxCalculationResponses(List<TaxCalculationResponseLine> taxCalculationResponses) {
		this.taxCalculationResponses = taxCalculationResponses;
	}

	
}
