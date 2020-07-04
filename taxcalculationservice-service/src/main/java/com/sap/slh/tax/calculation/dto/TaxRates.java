package com.sap.slh.tax.calculation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rateForTaxType1",
    "rateForTaxType2",
    "rateForTaxType3",
    "rateForTaxType4"
})
public class TaxRates implements Serializable
{

    @JsonProperty("rateForTaxType1") //rateForTaxType1
    private Double rateForTaxType1;
    @JsonProperty("rateForTaxType2") //rateForTaxType2
    private Double rateForTaxType2;
    @JsonProperty("rateForTaxType3") //rateForTaxType3
    private Double rateForTaxType3;
    @JsonProperty("rateForTaxType4") //rateForTaxType4
    private Double rateForTaxType4;
    
    private static final long serialVersionUID = -8490176927767429488L;
    
    @JsonProperty("rateForTaxType1")
    public Double getRateForTaxType1() {
		return rateForTaxType1;
	}
    @JsonProperty("rateForTaxType1")
	public void setRateForTaxType1(Double rateForTaxType1) {
		this.rateForTaxType1 = rateForTaxType1;
	}
    @JsonProperty("rateForTaxType2")
    public Double getRateForTaxType2() {
		return rateForTaxType2;
	}
    @JsonProperty("rateForTaxType2")
	public void setRateForTaxType2(Double rateForTaxType2) {
		this.rateForTaxType2 = rateForTaxType2;
	}
    @JsonProperty("rateForTaxType3")
	public Double getRateForTaxType3() {
		return rateForTaxType3;
	}
    @JsonProperty("rateForTaxType3")
	public void setRateForTaxType3(Double rateForTaxType3) {
		this.rateForTaxType3 = rateForTaxType3;
	}
    @JsonProperty("rateForTaxType4")
	public Double getRateForTaxType4() {
		return rateForTaxType4;
	}
    @JsonProperty("rateForTaxType4")
	public void setRateForTaxType4(Double rateForTaxType4) {
		this.rateForTaxType4 = rateForTaxType4;
	}
}
	

    