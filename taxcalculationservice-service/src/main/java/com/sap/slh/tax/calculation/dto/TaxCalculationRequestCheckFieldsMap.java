package com.sap.slh.tax.calculation.dto;

import java.util.HashMap;
import java.util.Map;

public class TaxCalculationRequestCheckFieldsMap {
	
	private HashMap<String, TaxCalculationRequestCheckFieldsDTO> idmap;

	public Map<String, TaxCalculationRequestCheckFieldsDTO> getIdmap() {
		return idmap;
	}

	public void setIdmap(Map<String, TaxCalculationRequestCheckFieldsDTO> idmap) {
		this.idmap = (HashMap<String, TaxCalculationRequestCheckFieldsDTO>) idmap;
	}

}
