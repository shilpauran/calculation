package com.sap.slh.tax.calculation.external.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.exception.BackingServiceException;
import com.sap.slh.tax.calculation.exception.GlobalExceptionHandler;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.calculation.service.TaxCalculationService;
import com.sap.slh.tax.calculation.utility.JsonUtil;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;
import com.sap.slh.tax.onboarding.cache.CacheConstant;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class TaxCalculationListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaxCalculationListener.class);

	@Autowired
	private TaxCalculationService taxCalculationService;

	@Autowired
	private GlobalExceptionHandler globalExceptionHandler;
	
	@Autowired
	private TaxCalculationTenantValidator tenantvalidator;


	@Value("${tax.calculation.service.supported.languages}")
	private String supportedLanguage;

	private List<String> supportedLanguages;

	@PostConstruct
	public void initSupportedLanguages() {
		supportedLanguages = Arrays.asList(supportedLanguage.split(","));

	}

	@CircuitBreaker(name = "taxCalculationCircuitBreaker", fallbackMethod = "fallbackCalculateTaxes")
	@RabbitListener(queues = "${rabbitmq.calc.queue}", returnExceptions = "true")
	public TaxCalculationResponse calculateTaxes(Message taxCalculationRequest) {

		LOGGER.debug("Message recieved: {}", taxCalculationRequest);
		TaxCalculationResponse response = null;
		try {
			
			Map<String, Object> messageHeaders = taxCalculationRequest.getMessageProperties().getHeaders();
			String tenantId = messageHeaders.get(TaxCalculationConstants.TENANT_ID).toString();


			if(!tenantvalidator.validateTenant(tenantId)) {
				throw new ApplicationException(TaxCalculationProcessingStatusCode.TENANT_ID_NOT_FOUND.getValue(),
						TaxCalculationProcessingStatusCode.INVALID_PARAMETER, TaxCalculationStatus.INVALID_REQUEST);
			}
			
			Locale locale = null;
			if (messageHeaders.containsKey(HttpHeaders.ACCEPT_LANGUAGE)) {
				locale = supportedLanguages.contains(messageHeaders.get(HttpHeaders.ACCEPT_LANGUAGE))
						? Locale.forLanguageTag((String) messageHeaders.get(HttpHeaders.ACCEPT_LANGUAGE))
						: Locale.ENGLISH;
			} else {
				locale = Locale.ENGLISH;
			}

			TaxCalculationRequest taxCalculationReq = JsonUtil.toObjectFromByte(taxCalculationRequest.getBody(),
					TaxCalculationRequest.class);

			LOGGER.debug("Request for tax calculation {}", JsonUtil.toJsonString(taxCalculationReq));
			response = taxCalculationService.calculateTax(taxCalculationReq, locale, tenantId);

			LOGGER.debug(" Calculation tax response : {}", JsonUtil.toJsonString(response));
		} catch (ApplicationException e) {
			response = globalExceptionHandler.handleGlobalCustomException(e);
			LOGGER.debug(" Calculation tax response : {}", JsonUtil.toJsonString(response));
		}
		return response;
	}

	public TaxCalculationResponse fallbackCalculateTaxes(Message taxCalculationRequest, BackingServiceException e) {
		LOGGER.error("status of the circuit is: {}, Backing service error occured while processing the request: ",
				TaxCalculationConstants.CB_STATUS_CLOSED, e);
		return globalExceptionHandler.handleGlobalCustomException(e);
	}

	public TaxCalculationResponse fallbackCalculateTaxes(Message taxCalculationRequest, CallNotPermittedException e) {
		LOGGER.error("status of the circuit is: {}, Limt of permitted failed calls has been reached : ",
				TaxCalculationConstants.CB_STATUS_OPEN, e);
		throw e;
	}
}
