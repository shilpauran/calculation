package com.sap.slh.tax.calculation.external.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.redis.core.RedisTemplate;

import com.sap.slh.tax.calculation.exception.BackingServiceException;

@RunWith(PowerMockRunner.class)
public class TaxCalculationTenantValidatorTest {

	@Mock
	RedisTemplate<Object, Object> redisTemplate;
	
	@InjectMocks
	TaxCalculationTenantValidator target;
	
	@Test
	public void validateTenantSuccess() {
		
		Mockito.when(redisTemplate.hasKey(any(String.class))).thenReturn(true);
		Boolean response = target.validateTenant("tenantId");
		assertEquals(response,true);
	}
	
	@Test(expected = BackingServiceException.class)
	public void validateTenantFailure() {
		Mockito.when(redisTemplate.hasKey(any(String.class))).thenReturn(false);
		Exception e = new Exception();
		
		for (int i = 0;i <=2;i++)
			target.validateTenant("tenantId");
		target.fallbackValidateTenant("tenantId", e);
		
	}
}
