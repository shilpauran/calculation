package com.sap.slh.tax.calculation.exception.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.exception.CustomException;
import com.sap.slh.tax.calculation.jmx.ServerExceptionMBean;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;

@Aspect
@Component
public class ServerExceptionCheckAspect {
	
	@Autowired
	ServerExceptionMBean serverExceptionMBean;
	
	@Around(value = "execution(* com.sap.slh.tax.calculation.exception.GlobalExceptionHandler.handleGlobalCustomException(..)) && args(exception)")
	public TaxCalculationResponse around(ProceedingJoinPoint joinPoint, CustomException exception) throws Throwable
	{
		if(exception.getProcessingStatusCode().equals(TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR))
			serverExceptionMBean.incrementCount();
		return (TaxCalculationResponse) joinPoint.proceed(joinPoint.getArgs());
	}

}
