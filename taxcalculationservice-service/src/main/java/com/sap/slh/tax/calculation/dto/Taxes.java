package com.sap.slh.tax.calculation.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"id",
	"ruleId",
    "taxBaseAmount",
    "decimalPlaces",
    "roundingMethod",
    "factor"
})
public class Taxes implements Serializable
{

	@JsonProperty("id")
	private String id;
	@JsonProperty("ruleId")
	private String ruleId;
	@JsonProperty("taxBaseAmount") 
    private BigDecimal taxBaseAmount;
    @JsonProperty("decimalPlaces")
    private Integer decimalPlaces;
    @JsonProperty("roundingMethod")
    private String roundingMethod;
    @JsonProperty("factor")
    private String factor;
	private static final long serialVersionUID = -362990924916085068L;
    
    @JsonProperty("id")
    public String getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("ruleId")
	public String getRuleId() {
		return ruleId;
	}
	@JsonProperty("ruleId")
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
    @JsonProperty("taxBaseAmount")
    public BigDecimal getTaxBaseAmount() {
        return taxBaseAmount;
    }

    @JsonProperty("taxBaseAmount")
    public void setTaxBaseAmount(BigDecimal taxBaseAmount) {
        this.taxBaseAmount = taxBaseAmount;
    }

    @JsonProperty("decimalPlaces")
    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    @JsonProperty("decimalPlaces")
    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @JsonProperty("roundingMethod")
    public String getRoundingMethod() {
        return roundingMethod;
    }

    @JsonProperty("roundingMethod")
    public void setRoundingMethod(String roundingMethod) {
        this.roundingMethod = roundingMethod;
    }
    
    @JsonProperty("factor")
    public String getFactor() {
		return factor;
	}
    
    @JsonProperty("factor")
	public void setFactor(String factor) {
		this.factor = factor;
	}

}
