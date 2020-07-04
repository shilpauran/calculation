package com.sap.slh.tax.onboarding.json.rule.service;

import com.sap.slh.tax.onboarding.json.rule.model.BaseResponse;

public interface JsonEngineDesignTimeService {
	
	public BaseResponse addCalculationRules(String tenantId);
	
	public BaseResponse updateCalculationRules(String tenantId);
	
	public BaseResponse deleteCalculationRules(String tenantId);
	

}
