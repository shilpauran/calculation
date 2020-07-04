package com.sap.slh.tax.calculation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "taxBaseInput", "reverseCharge", "taxRates", "taxDetails" })
public class TaxCalculationInputDTO implements Serializable {

	@JsonProperty("taxBaseInput")
	private TaxLineBRS taxBaseInput;
	@JsonProperty("reverseCharge")
	private ReverseCharge reverseCharge;
	@JsonProperty("taxRates")
	private TaxRates taxRates;
	@JsonProperty("taxDetails")
	private TaxDetails taxDetails;

	private static final long serialVersionUID = -7846673262092132817L;

	@JsonProperty("taxBaseInput")
	public TaxLineBRS getTaxBaseInput() {
		return taxBaseInput;
	}

	@JsonProperty("taxBaseInput")
	public void setTaxBaseInput(TaxLineBRS taxBaseInput) {
		this.taxBaseInput = taxBaseInput;
	}

	@JsonProperty("reverseCharge")
	public ReverseCharge getReverseCharge() {
		return reverseCharge;
	}

	@JsonProperty("reverseCharge")
	public void setReverseCharge(ReverseCharge reverseCharge) {
		this.reverseCharge = reverseCharge;
	}

	@JsonProperty("taxRates")
	public TaxRates getTaxRates() {
		return taxRates;
	}

	@JsonProperty("taxRates")
	public void setTaxRates(TaxRates taxRates) {
		this.taxRates = taxRates;
	}

	@JsonProperty("taxDetails")
	public TaxDetails getTaxDetails() {
		return taxDetails;
	}

	@JsonProperty("taxDetails")
	public void setTaxDetails(TaxDetails taxDetails) {
		this.taxDetails = taxDetails;
	}

}
