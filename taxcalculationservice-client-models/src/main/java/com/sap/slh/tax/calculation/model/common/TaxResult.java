package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "taxableBaseAmount", "taxAmount", "deductibleTaxAmount", "nonDeductibleTaxAmount" })
public class TaxResult implements Serializable {

	@ApiModelProperty(notes = "Unique identifier")
	@JsonProperty("id")
	private String id;

	@ApiModelProperty(notes = "The base amount that the tax service used to calculate the tax amount.")
	@JsonProperty("taxableBaseAmount")
	private BigDecimal taxableBaseAmount;

	@ApiModelProperty(notes = "The amount of tax that the tax service calculates, referring to a tax type. This amount includes any nondeductible amounts.")
	@JsonProperty("taxAmount")
	private BigDecimal taxAmount;

	@ApiModelProperty(notes = "The amount of tax that is deductible.")
	@JsonProperty("deductibleTaxAmount")
	private BigDecimal deductibleTaxAmount;

	@ApiModelProperty(notes = "The amount of tax that is not deductible.")
	@JsonProperty("nonDeductibleTaxAmount")
	private BigDecimal nonDeductibleTaxAmount;

	private final static long serialVersionUID = 7600228441877734811L;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("taxableBaseAmount")
	public BigDecimal getTaxableBaseAmount() {
		return taxableBaseAmount;
	}

	@JsonProperty("taxableBaseAmount")
	public void setTaxableBaseAmount(BigDecimal taxableBaseAmount) {
		this.taxableBaseAmount = taxableBaseAmount;
	}

	@JsonProperty("taxAmount")
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	@JsonProperty("taxAmount")
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	@JsonProperty("deductibleTaxAmount")
	public BigDecimal getDeductibleTaxAmount() {
		return deductibleTaxAmount;
	}

	@JsonProperty("deductibleTaxAmount")
	public void setDeductibleTaxAmount(BigDecimal deductibleTaxAmount) {
		this.deductibleTaxAmount = deductibleTaxAmount;
	}

	@JsonProperty("nonDeductibleTaxAmount")
	public BigDecimal getNonDeductibleTaxAmount() {
		return nonDeductibleTaxAmount;
	}

	@JsonProperty("nonDeductibleTaxAmount")
	public void setNonDeductibleTaxAmount(BigDecimal nonDeductibleTaxAmount) {
		this.nonDeductibleTaxAmount = nonDeductibleTaxAmount;
	}

}
