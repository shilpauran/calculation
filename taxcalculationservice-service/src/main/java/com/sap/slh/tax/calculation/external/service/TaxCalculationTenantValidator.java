package com.sap.slh.tax.calculation.external.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.sap.slh.tax.calculation.exception.BackingServiceException;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.onboarding.cache.CacheConstant;

import io.github.resilience4j.retry.annotation.Retry;

@Repository
public class TaxCalculationTenantValidator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaxCalculationTenantValidator.class);
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Retry(name = "redisRetryer", fallbackMethod = "fallbackValidateTenant")
	public Boolean validateTenant(String tenantId) {
		return redisTemplate.hasKey(tenantId + CacheConstant.CACHE_SET_SUFFIX);
	}
	
	public Boolean fallbackValidateTenant(String tenantId, Exception e) {
		LOGGER.error("Executing fallback method . An error occured when verifying tenantId: {}", tenantId, e);
		throw new BackingServiceException(TaxCalculationProcessingStatusCode.SERVICE_UNAVAILABLE.getValue(),
				TaxCalculationProcessingStatusCode.SERVICE_UNAVAILABLE, TaxCalculationStatus.SERVICE_UNAVAILABLE);
	}
}
