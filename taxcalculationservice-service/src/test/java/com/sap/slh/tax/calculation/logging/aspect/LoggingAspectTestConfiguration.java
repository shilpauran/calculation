package com.sap.slh.tax.calculation.logging.aspect;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;

import com.sap.slh.tax.calculation.exception.GlobalExceptionHandler;
import com.sap.slh.tax.calculation.external.service.TaxCalculationListener;
import com.sap.slh.tax.calculation.external.service.TaxCalculationTenantValidator;
import com.sap.slh.tax.calculation.service.TaxCalculationService;
import com.sap.slh.tax.calculation.utility.JWTUtil;

@Configuration
public class LoggingAspectTestConfiguration {
	
	@Bean
	JWTUtil jwtUtil()
	{
		return mock(JWTUtil.class);
	}
	
	@Bean
	GlobalExceptionHandler globalExceptionHandler()
	{
		return mock(GlobalExceptionHandler.class);
	}
	
	@Bean
	TaxCalculationService taxCalculationService()
	{
		return mock(TaxCalculationService.class);
	}
	
	@Bean
	RedisTemplate<Object, Object> redisTemplate()
	{
		return mock(RedisTemplate.class);
	}
	
	@Bean
	TaxCalculationTenantValidator tenantvalidator()
	{
		return mock(TaxCalculationTenantValidator.class);
	}
	@Bean
	@DependsOn({"jwtUtil","globalExceptionHandler"})
	LoggingAspect aspect()
	{   
		return new LoggingAspect();
	}
	
	@Bean
	@DependsOn({"jwtUtil","globalExceptionHandler","taxCalculationService","redisTemplate","aspect","tenantvalidator"})
	TaxCalculationListener listener() {
	    return new TaxCalculationListener();
	}
		
	

}
