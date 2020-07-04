package com.sap.slh.tax.calculation.cache.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.sap.slh.tax.calculation.cache.dto.JsonRuleLookupKey;
import com.sap.slh.tax.calculation.exception.BackingServiceException;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.onboarding.cache.CacheConstant;

import io.github.resilience4j.retry.annotation.Retry;

@Repository
public class CacheTaxCalculationLookupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheTaxCalculationLookupService.class);

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	public void put(JsonRuleLookupKey key, String value) {
		redisTemplate.opsForSet().add(key.getTenantId() + CacheConstant.CACHE_SET_SUFFIX, key.toString());
		redisTemplate.opsForValue().set(key.toString(), value);
	}

	@Retry(name = "redisRetryer", fallbackMethod = "fallBackGetJsonRule")
	public String get(JsonRuleLookupKey key) {
		return (String) redisTemplate.opsForValue().get(key.toString());
	}

	public String fallBackGetJsonRule(JsonRuleLookupKey key, Exception e) {
		LOGGER.error("Fallback method executed while fetching json rule for key: {}", key, e);
		throw new BackingServiceException(TaxCalculationProcessingStatusCode.SERVICE_UNAVAILABLE.getValue(),
				TaxCalculationProcessingStatusCode.SERVICE_UNAVAILABLE, TaxCalculationStatus.SERVICE_UNAVAILABLE);
	}

}
