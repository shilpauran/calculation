package com.sap.slh.tax.calculation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "taxBaseResult"
})
public class TaxCalculationOutputDTO implements Serializable
{

    @JsonProperty("taxBaseResult")
    private Taxes taxes;
    

    private static final long serialVersionUID = -1587974533819300926L;
    

    @JsonProperty("taxBaseResult")
    public Taxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxBaseResult")
    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

}
