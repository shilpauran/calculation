package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "taxes" })
public class ItemResult implements Serializable {
	@ApiModelProperty(notes = "The identifier of an item in the business transaction.")
	@JsonProperty("id")
	private String id;

	@ApiModelProperty(notes = "The calculated tax amounts for each tax line.")
	@JsonProperty("taxes")
	private List<TaxResult> taxes = null;

	private final static long serialVersionUID = 385644357493118046L;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("taxes")
	public List<TaxResult> getTaxes() {
		return taxes;
	}

	@JsonProperty("taxes")
	public void setTaxes(List<TaxResult> taxes) {
		this.taxes = taxes;
	}

}
