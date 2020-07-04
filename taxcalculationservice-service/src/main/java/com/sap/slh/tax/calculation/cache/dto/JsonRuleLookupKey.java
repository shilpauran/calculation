package com.sap.slh.tax.calculation.cache.dto;

import java.io.Serializable;

public class JsonRuleLookupKey implements Serializable {

	private static final long serialVersionUID = 2095161806667401952L;

	private String tenantId;
	private String countryRegionCode;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getCountryRegionCode() {
		return countryRegionCode;
	}

	public void setCountryRegionCode(String countryRegionCode) {
		this.countryRegionCode = countryRegionCode;
	}

	@Override
	public String toString() {
		return "tenantId:" + tenantId + ":countryRegionCode:" + countryRegionCode;
	}

}
