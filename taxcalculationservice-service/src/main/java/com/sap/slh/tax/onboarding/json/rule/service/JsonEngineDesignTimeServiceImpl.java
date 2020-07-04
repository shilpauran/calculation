package com.sap.slh.tax.onboarding.json.rule.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.sap.slh.tax.calculation.cache.dto.JsonRuleLookupKey;
import com.sap.slh.tax.calculation.cache.lookup.CacheTaxCalculationLookupService;
import com.sap.slh.tax.onboarding.cache.ClearCacheService;
import com.sap.slh.tax.onboarding.json.rule.model.BaseResponse;
import com.sap.slh.tax.onboarding.json.rule.util.RestServiceUtil;

@Service
public class JsonEngineDesignTimeServiceImpl implements JsonEngineDesignTimeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonEngineDesignTimeServiceImpl.class);

	@Value("${tax_calculation_content_url}")
	private String taxCalculationContentUrl;

	@Autowired
	CacheTaxCalculationLookupService cacheLookUp;
	
	@Autowired
	private ClearCacheService clearCache;

	@Autowired
	RestServiceUtil restServiceUtil;

	@Override
	public BaseResponse addCalculationRules(String tenantId) {
		UriComponentsBuilder builder = RestServiceUtil.buildGetCalculationRulesUri(taxCalculationContentUrl);
		Object responseBody = restServiceUtil.getCalculationRules(builder);
		JSONArray calculationRules = new JSONArray(String.valueOf(responseBody));
		for(int i =0 ; i < calculationRules.length();i++ ) {		
			JSONObject calcRule = (JSONObject) calculationRules.get(i);
			LOGGER.info(" Calculation Rule {} added for country {}", String.valueOf(calcRule) , calcRule.getString("country"));
			String country = calcRule.getString("country");
			JsonRuleLookupKey lookupKey = new JsonRuleLookupKey();
			lookupKey.setCountryRegionCode(country);
			lookupKey.setTenantId(tenantId);
			cacheLookUp.put(lookupKey, String.valueOf(calcRule));
		}
		return new BaseResponse();
	}

	@Override
	public BaseResponse updateCalculationRules(String tenantId) {
		clearCache.deleteTenantCacheData(tenantId);
		addCalculationRules(tenantId);
		return new BaseResponse();
	}

	@Override
	public BaseResponse deleteCalculationRules(String tenantId) {
		clearCache.deleteTenantCacheData(tenantId);
		return new BaseResponse();
	}

}
