package com.sap.slh.tax.calculation.logging.aspect;

import java.util.Map;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.exception.GlobalExceptionHandler;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.utility.JWTUtil;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

@Aspect
@Component
public class LoggingAspect {

	@Autowired
	private GlobalExceptionHandler globalExceptionHandler;

	@Autowired
	private JWTUtil jwtUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	@Around(value = "execution(* com.sap.slh.tax.calculation.external.service.TaxCalculationListener.calculateTaxes(..)) && args(taxCalculationRequest)")
	public TaxCalculationResponse around(ProceedingJoinPoint joinPoint, Message taxCalculationRequest)
			throws Throwable {

		String correlationId = null;
		TaxCalculationResponse response;

		try {

			MessageProperties messageProperties = taxCalculationRequest.getMessageProperties();
			Map<String, Object> messageHeaders = messageProperties.getHeaders();

			if (messageHeaders.containsKey(TaxCalculationConstants.X_CORRELATION_ID)) {
				correlationId = messageHeaders.get(TaxCalculationConstants.X_CORRELATION_ID).toString();
			} else {
				correlationId = UUID.randomUUID().toString();
				LOGGER.warn(" Correlation id is not passed for request {}, so generated a new correlation id {}",
						taxCalculationRequest, correlationId);
			}

			String authorizationToken = messageHeaders.get(HttpHeaders.AUTHORIZATION).toString();
			String tenantId = jwtUtil.getTenantId(authorizationToken);
			messageProperties.setHeader(TaxCalculationConstants.TENANT_ID, tenantId);

			MDC.put(TaxCalculationConstants.CORRELATION_ID, correlationId);
			MDC.put(TaxCalculationConstants.TENANT_ID, tenantId);
			response = (TaxCalculationResponse) joinPoint.proceed(joinPoint.getArgs());

		} catch (ApplicationException e) {
			response = globalExceptionHandler.handleGlobalCustomException(e);
		} finally {
			MDC.remove(TaxCalculationConstants.CORRELATION_ID);
			MDC.remove(TaxCalculationConstants.TENANT_ID);
		}
		return response;
	}

}
