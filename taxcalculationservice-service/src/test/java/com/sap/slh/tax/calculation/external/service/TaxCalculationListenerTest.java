
package com.sap.slh.tax.calculation.external.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.exception.GlobalExceptionHandler;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.service.TaxCalculationService;
import com.sap.slh.tax.calculation.utility.JWTUtil;
import com.sap.slh.tax.calculation.utility.JsonUtil;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

@RunWith(PowerMockRunner.class)

@PrepareForTest({ JsonUtil.class, ObjectMapper.class })
public class TaxCalculationListenerTest {

	private Message taxCalculationRequest;

	@Mock
	TaxCalculationService mockTaxCalculationService;

	@Mock
	GlobalExceptionHandler mockGlobalExceptionHandler;

	@Mock
	Locale localeObject;

	@Mock
	List<String> supportedLanguages;

	@Mock
	JWTUtil jwtUtil;
	
	@Mock
	TaxCalculationTenantValidator tenantvalidator;

	@InjectMocks
	TaxCalculationListener target;

	@Before
	public void init() {

		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("Accept-Language", "en");
		messageProperties.setHeader(TaxCalculationConstants.TENANT_ID, "1234");
		String string = new String();
		byte[] body = string.getBytes();
		taxCalculationRequest = new Message(body, messageProperties);

	}

	@Test
	public void test_calculateTaxes_success() {

		PowerMockito.mockStatic(JsonUtil.class);
		PowerMockito.mockStatic(ObjectMapper.class);
		String tenantId = "1234";
		TaxCalculationResponse resp = new TaxCalculationResponse();
		TaxCalculationRequest taxCalculationReq = new TaxCalculationRequest();
		Locale locale = new Locale("en"); // HashOperations<String, Object, Object>
		Mockito.when(mockTaxCalculationService.calculateTax(taxCalculationReq, locale, tenantId)).thenReturn(resp);
		when(JsonUtil.toObjectFromByte(any(byte[].class), ArgumentMatchers.<Class<TaxCalculationRequest>>any()))
				.thenReturn(taxCalculationReq);
		when(supportedLanguages.contains(any())).thenReturn(false);

		Mockito.when(tenantvalidator.validateTenant(any(String.class))).thenReturn(true);

		TaxCalculationResponse response = target.calculateTaxes(taxCalculationRequest);

		assertNotNull(response);
	}

	@Test
	public void missingAcceptHeader() {

		PowerMockito.mockStatic(JsonUtil.class);
		PowerMockito.mockStatic(ObjectMapper.class);
		String tenantId = "1234";
		TaxCalculationResponse resp = new TaxCalculationResponse();
		TaxCalculationRequest taxCalculationReq = new TaxCalculationRequest();
		Locale locale = new Locale("en"); //
		//Mockito.when(localeObject.forLanguageTag("en")).thenReturn(locale);
		Mockito.when(mockTaxCalculationService.calculateTax(taxCalculationReq, locale, tenantId)).thenReturn(resp);
		when(JsonUtil.toObjectFromByte(any(byte[].class), ArgumentMatchers.<Class<TaxCalculationRequest>>any()))
				.thenReturn(taxCalculationReq);
		when(supportedLanguages.contains(any())).thenReturn(false);

		Mockito.when(tenantvalidator.validateTenant(any(String.class))).thenReturn(true);

		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader(TaxCalculationConstants.TENANT_ID, "1234");
		String string = new String();
		byte[] body = string.getBytes();
		taxCalculationRequest = new Message(body, messageProperties);
		TaxCalculationResponse response = target.calculateTaxes(taxCalculationRequest);

		assertNotNull(response);
	}

	@Test
	public void unsupportedLanguage() {

		PowerMockito.mockStatic(JsonUtil.class);
		PowerMockito.mockStatic(ObjectMapper.class);
		String tenantId = "1234";
		TaxCalculationResponse resp = new TaxCalculationResponse();
		TaxCalculationRequest taxCalculationReq = new TaxCalculationRequest();
		Locale locale = new Locale("en"); //
		//Mockito.when(localeObject.forLanguageTag("en")).thenReturn(locale);
		Mockito.when(mockTaxCalculationService.calculateTax(taxCalculationReq, locale, tenantId)).thenReturn(resp);
		when(JsonUtil.toObjectFromByte(any(byte[].class), ArgumentMatchers.<Class<TaxCalculationRequest>>any()))
				.thenReturn(taxCalculationReq);
		when(supportedLanguages.contains(any())).thenReturn(true);

		Mockito.when(tenantvalidator.validateTenant(any(String.class))).thenReturn(true);

		TaxCalculationResponse response = target.calculateTaxes(taxCalculationRequest);

		assertNotNull(response);
	}

	@Test
	public void test_calculateTaxes_failure() {

		ApplicationException exception = new ApplicationException(
				TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR.getValue(),
				TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR);

		TaxCalculationResponse resp = new TaxCalculationResponse();
		resp.setStatusMessage(TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR.getValue());
		resp.setProcessingStatusCode(TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR);

		TaxCalculationRequest taxCalculationReq = new TaxCalculationRequest();
		Locale locale = new Locale("en");
		Mockito.doThrow(exception).when(mockTaxCalculationService).calculateTax(taxCalculationReq, locale, "");

		Mockito.when(mockGlobalExceptionHandler.handleGlobalCustomException(any(ApplicationException.class)))
				.thenReturn(resp);
		Mockito.when(tenantvalidator.validateTenant(any(String.class))).thenReturn(true);
		TaxCalculationResponse response = target.calculateTaxes(taxCalculationRequest);

		// verify
		Mockito.verify(mockGlobalExceptionHandler).handleGlobalCustomException(any(ApplicationException.class));
		assertEquals(TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR.getValue(),
				response.getProcessingStatusCode().getValue());

	}
	
	@Test
	public void test_tenantID_not_found() {
		PowerMockito.mockStatic(JsonUtil.class);
		PowerMockito.mockStatic(ObjectMapper.class);
		TaxCalculationResponse resp = new TaxCalculationResponse();
		TaxCalculationRequest taxCalculationReq = new TaxCalculationRequest();
		String tenantId = "1234";
		Mockito.when(mockTaxCalculationService.calculateTax(taxCalculationReq, Locale.ENGLISH, tenantId))
				.thenReturn(resp);
		when(JsonUtil.toObjectFromByte(any(byte[].class), ArgumentMatchers.<Class<TaxCalculationRequest>>any()))
				.thenReturn(taxCalculationReq);
		Mockito.when(jwtUtil.getTenantId(any(String.class))).thenReturn(tenantId);
		Mockito.when(tenantvalidator.validateTenant(any(String.class))).thenReturn(false);
		// Execution //
		TaxCalculationResponse response = target.calculateTaxes(taxCalculationRequest);
		assertNull(response);
	}

}