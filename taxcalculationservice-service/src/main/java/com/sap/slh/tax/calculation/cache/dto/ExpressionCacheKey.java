package com.sap.slh.tax.calculation.cache.dto;

public class ExpressionCacheKey {

	private String country;
	private String amountTypeCode;
	private String isReverseChargeRelevant;
	private String isTaxEventNonTaxable;
	private String taxTypeCode;
	private String isTaxType1ReverseChargeRelevant;
	private String isTaxType2ReverseChargeRelevant;
	private String isTaxType3ReverseChargeRelevant;
	private String isTaxType4ReverseChargeRelevant;
	private String isTaxType1Relevant;
	private String isTaxType2Relevant;
	private String isTaxType3Relevant;
	private String isTaxType4Relevant;
	private String tenantId;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAmountTypeCode() {
		return amountTypeCode;
	}

	public void setAmountTypeCode(String amountTypeCode) {
		this.amountTypeCode = amountTypeCode;
	}

	public String getIsReverseChargeRelevant() {
		return isReverseChargeRelevant;
	}

	public void setIsReverseChargeRelevant(String isReverseChargeRelevant) {
		this.isReverseChargeRelevant = isReverseChargeRelevant;
	}

	public String getIsTaxEventNonTaxable() {
		return isTaxEventNonTaxable;
	}

	public void setIsTaxEventNonTaxable(String isTaxEventNonTaxable) {
		this.isTaxEventNonTaxable = isTaxEventNonTaxable;
	}

	public String getTaxTypeCode() {
		return taxTypeCode;
	}

	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}

	public String getIsTaxType1ReverseChargeRelevant() {
		return isTaxType1ReverseChargeRelevant;
	}

	public void setIsTaxType1ReverseChargeRelevant(String isTaxType1ReverseChargeRelevant) {
		this.isTaxType1ReverseChargeRelevant = isTaxType1ReverseChargeRelevant;
	}

	public String getIsTaxType2ReverseChargeRelevant() {
		return isTaxType2ReverseChargeRelevant;
	}

	public void setIsTaxType2ReverseChargeRelevant(String isTaxType2ReverseChargeRelevant) {
		this.isTaxType2ReverseChargeRelevant = isTaxType2ReverseChargeRelevant;
	}

	public String getIsTaxType3ReverseChargeRelevant() {
		return isTaxType3ReverseChargeRelevant;
	}

	public void setIsTaxType3ReverseChargeRelevant(String isTaxType3ReverseChargeRelevant) {
		this.isTaxType3ReverseChargeRelevant = isTaxType3ReverseChargeRelevant;
	}

	public String getIsTaxType4ReverseChargeRelevant() {
		return isTaxType4ReverseChargeRelevant;
	}

	public void setIsTaxType4ReverseChargeRelevant(String isTaxType4ReverseChargeRelevant) {
		this.isTaxType4ReverseChargeRelevant = isTaxType4ReverseChargeRelevant;
	}

	public String getIsTaxType1Relevant() {
		return isTaxType1Relevant;
	}

	public void setIsTaxType1Relevant(String isTaxType1Relevant) {
		this.isTaxType1Relevant = isTaxType1Relevant;
	}

	public String getIsTaxType2Relevant() {
		return isTaxType2Relevant;
	}

	public void setIsTaxType2Relevant(String isTaxType2Relevant) {
		this.isTaxType2Relevant = isTaxType2Relevant;
	}

	public String getIsTaxType3Relevant() {
		return isTaxType3Relevant;
	}

	public void setIsTaxType3Relevant(String isTaxType3Relevant) {
		this.isTaxType3Relevant = isTaxType3Relevant;
	}

	public String getIsTaxType4Relevant() {
		return isTaxType4Relevant;
	}

	public void setIsTaxType4Relevant(String isTaxType4Relevant) {
		this.isTaxType4Relevant = isTaxType4Relevant;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountTypeCode == null) ? 0 : amountTypeCode.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((isReverseChargeRelevant == null) ? 0 : isReverseChargeRelevant.hashCode());
		result = prime * result + ((isTaxEventNonTaxable == null) ? 0 : isTaxEventNonTaxable.hashCode());
		result = prime * result + ((isTaxType1Relevant == null) ? 0 : isTaxType1Relevant.hashCode());
		result = prime * result
				+ ((isTaxType1ReverseChargeRelevant == null) ? 0 : isTaxType1ReverseChargeRelevant.hashCode());
		result = prime * result + ((isTaxType2Relevant == null) ? 0 : isTaxType2Relevant.hashCode());
		result = prime * result
				+ ((isTaxType2ReverseChargeRelevant == null) ? 0 : isTaxType2ReverseChargeRelevant.hashCode());
		result = prime * result + ((isTaxType3Relevant == null) ? 0 : isTaxType3Relevant.hashCode());
		result = prime * result
				+ ((isTaxType3ReverseChargeRelevant == null) ? 0 : isTaxType3ReverseChargeRelevant.hashCode());
		result = prime * result + ((isTaxType4Relevant == null) ? 0 : isTaxType4Relevant.hashCode());
		result = prime * result
				+ ((isTaxType4ReverseChargeRelevant == null) ? 0 : isTaxType4ReverseChargeRelevant.hashCode());
		result = prime * result + ((taxTypeCode == null) ? 0 : taxTypeCode.hashCode());
		result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpressionCacheKey other = (ExpressionCacheKey) obj;
		if (amountTypeCode == null) {
			if (other.amountTypeCode != null)
				return false;
		} else if (!amountTypeCode.equals(other.amountTypeCode))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (isReverseChargeRelevant == null) {
			if (other.isReverseChargeRelevant != null)
				return false;
		} else if (!isReverseChargeRelevant.equals(other.isReverseChargeRelevant))
			return false;
		if (isTaxEventNonTaxable == null) {
			if (other.isTaxEventNonTaxable != null)
				return false;
		} else if (!isTaxEventNonTaxable.equals(other.isTaxEventNonTaxable))
			return false;
		if (isTaxType1Relevant == null) {
			if (other.isTaxType1Relevant != null)
				return false;
		} else if (!isTaxType1Relevant.equals(other.isTaxType1Relevant))
			return false;
		if (isTaxType1ReverseChargeRelevant == null) {
			if (other.isTaxType1ReverseChargeRelevant != null)
				return false;
		} else if (!isTaxType1ReverseChargeRelevant.equals(other.isTaxType1ReverseChargeRelevant))
			return false;
		if (isTaxType2Relevant == null) {
			if (other.isTaxType2Relevant != null)
				return false;
		} else if (!isTaxType2Relevant.equals(other.isTaxType2Relevant))
			return false;
		if (isTaxType2ReverseChargeRelevant == null) {
			if (other.isTaxType2ReverseChargeRelevant != null)
				return false;
		} else if (!isTaxType2ReverseChargeRelevant.equals(other.isTaxType2ReverseChargeRelevant))
			return false;
		if (isTaxType3Relevant == null) {
			if (other.isTaxType3Relevant != null)
				return false;
		} else if (!isTaxType3Relevant.equals(other.isTaxType3Relevant))
			return false;
		if (isTaxType3ReverseChargeRelevant == null) {
			if (other.isTaxType3ReverseChargeRelevant != null)
				return false;
		} else if (!isTaxType3ReverseChargeRelevant.equals(other.isTaxType3ReverseChargeRelevant))
			return false;
		if (isTaxType4Relevant == null) {
			if (other.isTaxType4Relevant != null)
				return false;
		} else if (!isTaxType4Relevant.equals(other.isTaxType4Relevant))
			return false;
		if (isTaxType4ReverseChargeRelevant == null) {
			if (other.isTaxType4ReverseChargeRelevant != null)
				return false;
		} else if (!isTaxType4ReverseChargeRelevant.equals(other.isTaxType4ReverseChargeRelevant))
			return false;
		if (taxTypeCode == null) {
			if (other.taxTypeCode != null)
				return false;
		} else if (!taxTypeCode.equals(other.taxTypeCode))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		return true;
	}

}
