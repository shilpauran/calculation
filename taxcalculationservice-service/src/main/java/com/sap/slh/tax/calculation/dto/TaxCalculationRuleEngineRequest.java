package com.sap.slh.tax.calculation.dto;

import java.util.List;

public class TaxCalculationRuleEngineRequest {
	
	private List<TaxCalculationInputDTO> ruleEngineRequest;

	public List<TaxCalculationInputDTO> getRuleEngineRequest() {
		return ruleEngineRequest;
	}

	public void setRuleEngineRequest(List<TaxCalculationInputDTO> ruleEngineRequest) {
		this.ruleEngineRequest = ruleEngineRequest;
	}

}
