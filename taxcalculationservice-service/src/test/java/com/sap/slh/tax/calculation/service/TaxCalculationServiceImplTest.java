package com.sap.slh.tax.calculation.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationRuleEngineRequest;
import com.sap.slh.tax.calculation.dto.Taxes;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.TaxCalculationValidationResult;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationWarning;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponseLine;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.calculation.model.common.TaxCalculationWarningDetail;
import com.sap.slh.tax.calculation.service.builder.TaxCalculationResponseBuilder;
import com.sap.slh.tax.calculation.service.mapper.impl.TaxCalculationRequestCheckFieldMapper;
import com.sap.slh.tax.calculation.service.mapper.impl.TaxCalculationRuleEngineRequestMapper;
import com.sap.slh.tax.calculation.validator.StructuralValidator;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculationServiceImplTest {

	@Mock
	private TaxCalculationResponseBuilder mockResponseBuilder;

	@Mock
	private TaxCalculationRequestValidation mockRequestValidator;

	@Mock
	private TaxCalculationResponseValidator mockResponseValidator;

	@Mock
	private Jackson2JsonMessageConverter mockConverter;

	@Mock
	private GenericTaxCalculationService mockGenericTaxCalculationService;

	@Mock
	private ObjectMapper mockMapper;

	@Mock
	private TaxCalculationRequestCheckFieldMapper mockCheckFieldMapper;

	@Mock
	private TaxCalculationRuleEngineRequestMapper taxCalculationRuleEngineRequestMapper;

	@Mock
	private StructuralValidator structuralValidator;

	@InjectMocks
	TaxCalculationServiceImpl target;

	private TaxCalculationRequest calculationRequest;
	MessageProperties msg;

	Jackson2JsonMessageConverter msgConverter;

	List<TaxCalculationResponseLine> results = new ArrayList<>();

	Message ruleServiceCalcReqs = null;

	Message ruleServiceCalcResp = null;

	TaxCalculationRuleEngineRequest taxCalculationRuleEngineRequest = new TaxCalculationRuleEngineRequest();

	@Before
	public void init() {
		calculationRequest = new TaxCalculationRequest();
	}

	@Test
	public void test_calculateTax_success() throws JsonParseException, JsonMappingException, IOException {

		TaxCalculationRequest calculationRequest = new TaxCalculationRequest();
		List<Item> items = new ArrayList<>();
		calculationRequest.setItems(items);
		List<TaxCalculationOutputDTO> taxCalcOutputDto = new ArrayList<>();
		TaxCalculationOutputDTO output = new TaxCalculationOutputDTO();
		Taxes taxes = new Taxes();
		taxes.setTaxBaseAmount(new BigDecimal(100));
		taxes.setRuleId("test");
		output.setTaxes(taxes);
		taxCalcOutputDto.add(output);
		TaxCalculationResponseLine resp = new TaxCalculationResponseLine();

		TaxCalculationApplicationWarning warnings = new TaxCalculationApplicationWarning();
		TaxCalculationValidationResult validationResult = new TaxCalculationValidationResult();
		validationResult.setTaxCalculationWarnings(warnings);
		validationResult.setValid(true);

		// Mock
		Mockito.when(mockRequestValidator.validate(calculationRequest, Locale.ENGLISH)).thenReturn(validationResult);
		Mockito.when(taxCalculationRuleEngineRequestMapper.convertTo(calculationRequest))
				.thenReturn(taxCalculationRuleEngineRequest);
		Mockito.when(mockGenericTaxCalculationService
				.calculateTax(taxCalculationRuleEngineRequest.getRuleEngineRequest(), "")).thenReturn(taxCalcOutputDto);
		Mockito.when(structuralValidator.isValid(calculationRequest, Locale.ENGLISH)).thenReturn(true);
		// Execution
		TaxCalculationResponse response = target.calculateTax(calculationRequest, Locale.ENGLISH, "");

		// Verify
		assertNotNull(response);
		assertEquals(TaxCalculationStatus.SUCCESS, response.getStatus());
		assertEquals(TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY,
				response.getProcessingStatusCode());
	}

	@Test
	public void test_calculateTax_successWithWarning() throws JsonParseException, JsonMappingException, IOException {

		TaxCalculationRequest calculationRequest = new TaxCalculationRequest();
		List<Item> items = new ArrayList<>();
		calculationRequest.setItems(items);
		List<TaxCalculationOutputDTO> taxCalcOutputDto = new ArrayList<>();
		TaxCalculationOutputDTO output = new TaxCalculationOutputDTO();
		Taxes taxes = new Taxes();
		taxes.setTaxBaseAmount(new BigDecimal(100));
		taxes.setRuleId("test");
		output.setTaxes(taxes);
		taxCalcOutputDto.add(output);
		TaxCalculationApplicationWarning warnings = new TaxCalculationApplicationWarning();
		List<TaxCalculationWarningDetail> details = new ArrayList<>();
		TaxCalculationWarningDetail detail = new TaxCalculationWarningDetail();
		TaxCalculationResponseLine responseLine = new TaxCalculationResponseLine();
		details.add(detail);
		warnings.setDetails(details);
		TaxCalculationValidationResult validationResult = new TaxCalculationValidationResult();
		validationResult.setTaxCalculationWarnings(warnings);
		validationResult.setValid(true);

		// Mock
		Mockito.when(mockRequestValidator.validate(calculationRequest, Locale.ENGLISH)).thenReturn(validationResult);
		Mockito.when(taxCalculationRuleEngineRequestMapper.convertTo(calculationRequest))
				.thenReturn(taxCalculationRuleEngineRequest);
		Mockito.when(mockGenericTaxCalculationService
				.calculateTax(taxCalculationRuleEngineRequest.getRuleEngineRequest(), "")).thenReturn(taxCalcOutputDto);
		Mockito.when(structuralValidator.isValid(calculationRequest, Locale.ENGLISH)).thenReturn(true);

		// Execution
		TaxCalculationResponse response = target.calculateTax(calculationRequest, Locale.ENGLISH, "");

		// Verify
		assertNotNull(response);
		assertEquals(TaxCalculationStatus.SUCCESS, response.getStatus());
		assertEquals(TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY_WITH_WARNINGS,
				response.getProcessingStatusCode());

	}

	@Test
	public void test_calculateTax_invalidRequest() throws IOException {
		TaxCalculationRequest calculationRequest = new TaxCalculationRequest();
		TaxCalculationApplicationWarning warnings = new TaxCalculationApplicationWarning();
		List<TaxCalculationOutputDTO> taxCalcOutputDto = new ArrayList<>();
		TaxCalculationValidationResult validationResult = new TaxCalculationValidationResult();
		validationResult.setTaxCalculationWarnings(warnings);
		validationResult.setValid(false);

		// Mock
		Mockito.when(mockRequestValidator.validate(calculationRequest, Locale.ENGLISH)).thenReturn(validationResult);
		// Mockito.when(mockGenericTaxCalculationService.calculateTax(calculationRequest)).thenReturn(taxCalcOutputDto);
		Mockito.when(structuralValidator.isValid(calculationRequest, Locale.ENGLISH)).thenReturn(true);

		// Execution
		try {
			target.calculateTax(calculationRequest, Locale.ENGLISH, "");
			fail();
		} catch (ApplicationException e) {
			// Verify
			assertEquals(TaxCalculationProcessingStatusCode.REQUEST_NOT_VALID.getValue(), e.getMessage());
		}

	}

	@Test(expected = ApplicationException.class)
	public void test_calculate_tax_emptyResult() {
		TaxCalculationRequest calculationRequest = new TaxCalculationRequest();
		List<Item> items = new ArrayList<>();
		calculationRequest.setItems(items);
		List<TaxCalculationOutputDTO> taxCalcOutputDto = new ArrayList<>();
		TaxCalculationApplicationWarning warnings = new TaxCalculationApplicationWarning();
		TaxCalculationValidationResult validationResult = new TaxCalculationValidationResult();
		validationResult.setTaxCalculationWarnings(warnings);
		validationResult.setValid(true);
		// Mock
		Mockito.when(mockRequestValidator.validate(calculationRequest, Locale.ENGLISH)).thenReturn(validationResult);
		Mockito.when(taxCalculationRuleEngineRequestMapper.convertTo(calculationRequest))
				.thenReturn(taxCalculationRuleEngineRequest);
		Mockito.when(mockGenericTaxCalculationService
				.calculateTax(taxCalculationRuleEngineRequest.getRuleEngineRequest(), "")).thenReturn(taxCalcOutputDto);
		Mockito.when(structuralValidator.isValid(calculationRequest, Locale.ENGLISH)).thenReturn(true);

		// Execution
		TaxCalculationResponse response = target.calculateTax(calculationRequest, Locale.ENGLISH, "");
	}

	@Test
	public void test_calculateTax_emptyrequest() {
		TaxCalculationRequest emptyCalculationRequest = null;
		try {
			target.calculateTax(emptyCalculationRequest, Locale.ENGLISH, "");
			fail();
		} catch (ApplicationException e) {
			assertEquals(TaxCalculationProcessingStatusCode.EMPTY_REQUEST.getValue(), e.getMessage());
		}

	}

}