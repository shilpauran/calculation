package com.sap.slh.tax.onboarding.json.rule.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.slh.tax.onboarding.json.rule.model.BaseResponse;
import com.sap.slh.tax.onboarding.json.rule.service.JsonEngineDesignTimeServiceImpl;
import com.sap.slh.tax.onboarding.json.rule.util.JsonEngineUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Calculation Rules operations")
public class JsonEngineRulesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonEngineRulesController.class);
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	private JsonEngineDesignTimeServiceImpl jsonEngineDesignTimeImpl;
	
	@ApiOperation(value = "add calculation rules in tenant's cache", notes = "add calculation rules in tenant's cache", nickname = "add calculation rules in tenant's cache")
	@RequestMapping(value = "/calculation/v1/onboarding", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse addJsonCalculationRules(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
		
		BaseResponse response = null;
		String tenantId = JsonEngineUtil.getTenantId(authToken);
		LOGGER.info("Adding Json Rules for tenantId : {} ", tenantId);
		response = jsonEngineDesignTimeImpl.addCalculationRules(tenantId);
		return response;
	}

	@ApiOperation(value = "delete add calculation rules in tenant's cache", notes = "delete add calculation rules in tenant's cache", nickname = "delete calculation rules in tenant's cache")
	@RequestMapping(value = "/calculation/v1/offboarding", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse deleteJsonCalculationRules(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
		BaseResponse response = null;
		String tenantId = JsonEngineUtil.getTenantId(authToken);
		LOGGER.info("Deleting Json Rules for tenantId : {} ", tenantId);
		response = jsonEngineDesignTimeImpl.deleteCalculationRules(tenantId);
		return response;
		
	}
	
	@ApiOperation(value = "update add calculation rules in tenant's cache", notes = "update calculation rules in tenant's cache", nickname = "update calculation rules in tenant's cache")
	@RequestMapping(value = "/calculation/api/v1/calculation/rules", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse updateJsonCalculationRules(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
		BaseResponse response = null;
		String tenantId = JsonEngineUtil.getTenantId(authToken);
		LOGGER.info("Updating Json Rules for tenantId : {} ", tenantId);
		response = jsonEngineDesignTimeImpl.updateCalculationRules(tenantId);
		return response;
	}
	
	@RequestMapping(value = "/calculation/clear/cache", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse clearCache() {
		redisTemplate.getConnectionFactory().getConnection().flushAll();
		return new BaseResponse();
	}

}
