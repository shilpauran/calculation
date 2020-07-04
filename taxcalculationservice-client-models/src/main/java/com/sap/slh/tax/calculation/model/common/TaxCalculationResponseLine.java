package com.sap.slh.tax.calculation.model.common;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "items" })
public class TaxCalculationResponseLine implements Serializable {

	@ApiModelProperty(notes = "The identifier of the business transaction for which the consuming application needs taxes to be calculated.")
	@JsonProperty("id")
	private String id;
	@JsonProperty("items")
	private List<ItemResult> items = null;

	private final static long serialVersionUID = -3083005872390576204L;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("items")
	public List<ItemResult> getItems() {
		return items;
	}

	@JsonProperty("items")
	public void setItems(List<ItemResult> items) {
		this.items = items;
	}

}
