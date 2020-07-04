package com.sap.slh.tax.calculation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "isTaxType1Relevant",
    "isTaxType2Relevant",
    "isTaxType3Relevant",
    "isTaxType4Relevant"
})
public class ReverseCharge implements Serializable 
{

    @JsonProperty("isTaxType1Relevant") 
    private String isTaxType1Relevant;
	@JsonProperty("isTaxType2Relevant") 
    private String isTaxType2Relevant;
	@JsonProperty("isTaxType3Relevant") 
    private String isTaxType3Relevant;
    @JsonProperty("isTaxType4Relevant") 
    private String isTaxType4Relevant;
    
    private static final long serialVersionUID = -8050989934107520639L;
    
    @JsonProperty("isTaxType1Relevant")
    public String getIsTaxType1Relevant() {
		return isTaxType1Relevant;
	}
    
    @JsonProperty("isTaxType1Relevant")
	public void setIsTaxType1Relevant(String isTaxType1Relevant) {
		this.isTaxType1Relevant = isTaxType1Relevant;
	}
    
    @JsonProperty("isTaxType2Relevant")
    public String getIsTaxType2Relevant() {
		return isTaxType2Relevant;
	}
    
    @JsonProperty("isTaxType2Relevant")
	public void setIsTaxType2Relevant(String isTaxType2Relevant) {
		this.isTaxType2Relevant = isTaxType2Relevant;
	}
    
    @JsonProperty("isTaxType3Relevant")
	public String getIsTaxType3Relevant() {
		return isTaxType3Relevant;
	}
    
    @JsonProperty("isTaxType3Relevant")
	public void setIsTaxType3Relevant(String isTaxType3Relevant) {
		this.isTaxType3Relevant = isTaxType3Relevant;
	}
    
    @JsonProperty("isTaxType4Relevant")
	public String getIsTaxType4Relevant() {
		return isTaxType4Relevant;
	}
    
    @JsonProperty("isTaxType4Relevant")
	public void setIsTaxType4Relevant(String isTaxType4Relevant) {
		this.isTaxType4Relevant = isTaxType4Relevant;
	}
    
}
