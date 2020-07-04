package com.sap.slh.tax.calculation.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "currencyCode", "countryCode", "subdivisionCode", "date", "amountTypeCode", "quantity",
		"taxTypeCode", "unitPrice", "isTaxEventNonTaxable", "taxEventCode", "dueCategoryCode",
		"isReverseChargeRelevant", "taxRate" })
public class TaxLineBRS implements Serializable {

	@JsonProperty("id")
	private String id;
	@JsonProperty("currencyCode")
	private TaxCalculationRequest.CurrencyCode currencyCode;
	@JsonProperty("countryCode")
	private Item.CountryRegionCode countryCode;
	@JsonProperty("subdivisionCode")
	private String subdivisionCode;
	@JsonProperty("date")
	private String date;
	@JsonProperty("amountTypeCode")
	private TaxCalculationRequest.AmountTypeCode amountTypeCode;
	@JsonProperty("quantity")
	private BigDecimal quantity;
	@JsonProperty("taxTypeCode")
	private String taxTypeCode;
	@JsonProperty("unitPrice")
	private BigDecimal unitPrice;
	@JsonProperty("isTaxEventNonTaxable")
	private String isTaxEventNonTaxable;
	@JsonProperty("taxEventCode")
	private String taxEventCode;
	@JsonProperty("dueCategoryCode")
	private String dueCategoryCode;
	@JsonProperty("isReverseChargeRelevant")
	private String isReverseChargeRelevant;
	@JsonProperty("taxRate")
	private Double taxRate;
	private static final long serialVersionUID = -2099862799517163287L;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("currencyCode")
	public TaxCalculationRequest.CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

	@JsonProperty("currencyCode")
	public void setCurrencyCode(TaxCalculationRequest.CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}

	@JsonProperty("countryCode")
	public Item.CountryRegionCode getCountryCode() {
		return countryCode;
	}

	@JsonProperty("countryCode")
	public void setCountryCode(Item.CountryRegionCode countryCode) {
		this.countryCode = countryCode;
	}

	@JsonProperty("subdivisionCode")
	public String getSubdivisionCode() {
		return subdivisionCode;
	}

	@JsonProperty("subdivisionCode")
	public void setSubdivisionCode(String subdivisionCode) {
		this.subdivisionCode = subdivisionCode;
	}

	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	@JsonProperty("amountTypeCode")
	public TaxCalculationRequest.AmountTypeCode getAmountTypeCode() {
		return amountTypeCode;
	}

	@JsonProperty("amountTypeCode")
	public void setAmountTypeCode(TaxCalculationRequest.AmountTypeCode amountTypeCode) {
		this.amountTypeCode = amountTypeCode;
	}

	@JsonProperty("quantity")
	public BigDecimal getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("taxTypeCode")
	public String getTaxTypeCode() {
		return taxTypeCode;
	}

	@JsonProperty("taxTypeCode")
	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}

	@JsonProperty("unitPrice")
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	@JsonProperty("unitPrice")
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@JsonProperty("isTaxEventNonTaxable")
	public String getIsTaxEventNonTaxable() {
		return isTaxEventNonTaxable;
	}

	@JsonProperty("isTaxEventNonTaxable")
	public void setIsTaxEventNonTaxable(String isTaxEventNonTaxable) {
		this.isTaxEventNonTaxable = isTaxEventNonTaxable;
	}

	@JsonProperty("taxEventCode")
	public String getTaxEventCode() {
		return taxEventCode;
	}

	@JsonProperty("taxEventCode")
	public void setTaxEventCode(String taxEventCode) {
		this.taxEventCode = taxEventCode;
	}

	@JsonProperty("dueCategoryCode")
	public String getDueCategoryCode() {
		return dueCategoryCode;
	}

	@JsonProperty("dueCategoryCode")
	public void setDueCategoryCode(String dueCategoryCode) {
		this.dueCategoryCode = dueCategoryCode;
	}

	@JsonProperty("isReverseChargeRelevant")
	public String getIsReverseChargeRelevant() {
		return isReverseChargeRelevant;
	}

	@JsonProperty("isReverseChargeRelevant")
	public void setIsReverseChargeRelevant(String isReverseChargeRelevant) {
		this.isReverseChargeRelevant = isReverseChargeRelevant;
	}

	@JsonProperty("taxRate")
	public Double getTaxRate() {
		return taxRate;
	}

	@JsonProperty("taxRate")
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

}
