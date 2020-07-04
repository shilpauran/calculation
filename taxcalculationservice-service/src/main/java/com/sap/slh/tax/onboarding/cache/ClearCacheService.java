package com.sap.slh.tax.onboarding.cache;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClearCacheService {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	public void deleteTenantCacheData(String tenantId) {
		Set<Object> keys = redisTemplate.opsForSet().members(tenantId + CacheConstant.CACHE_SET_SUFFIX);
		redisTemplate.delete(keys);
		redisTemplate.delete(tenantId + CacheConstant.CACHE_SET_SUFFIX);
	}

}
