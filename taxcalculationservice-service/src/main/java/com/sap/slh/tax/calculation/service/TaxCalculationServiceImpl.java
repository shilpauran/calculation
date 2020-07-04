package com.sap.slh.tax.calculation.service;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.exception.InvalidRequestException;
import com.sap.slh.tax.calculation.model.TaxCalculationValidationResult;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationWarning;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponseLine;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.calculation.service.builder.TaxCalculationResponseBuilder;
import com.sap.slh.tax.calculation.service.mapper.impl.TaxCalculationRuleEngineRequestMapper;
import com.sap.slh.tax.calculation.utility.JsonUtil;
import com.sap.slh.tax.calculation.validator.StructuralValidator;

@Service
public class TaxCalculationServiceImpl implements TaxCalculationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaxCalculationServiceImpl.class);

	@Autowired
	private GenericTaxCalculationService genericTaxCalculationService;

	@Autowired
	TaxCalculationRequestValidation requestValidator;

	@Autowired
	private StructuralValidator structuralValidator;

	@Autowired
	TaxCalculationResponseValidator responseValidator;

	@Autowired
	private TaxCalculationRuleEngineRequestMapper taxCalculationRuleEngineRequestMapper;

	@Override
	public TaxCalculationResponse calculateTax(final TaxCalculationRequest taxCalculationRequest, Locale locale,
			final String tenantId) {

		TaxCalculationResponse response = new TaxCalculationResponse();
		TaxCalculationApplicationWarning warnings = new TaxCalculationApplicationWarning();

		LOGGER.debug("Service implementation is invoked");

		if (taxCalculationRequest == null) {
			throw new ApplicationException(TaxCalculationProcessingStatusCode.EMPTY_REQUEST.getValue(),
					TaxCalculationProcessingStatusCode.EMPTY_REQUEST);
		}
		TaxCalculationValidationResult validationResult = null;

		if (structuralValidator.isValid(taxCalculationRequest, locale)) {
			validationResult = requestValidator.validate(taxCalculationRequest, locale);
		}

		Gson gson = new Gson();
		LOGGER.debug("validation result {}", gson.toJson(validationResult));

		if (validationResult != null) {
			warnings = validationResult.getTaxCalculationWarnings();
		}

		if (validationResult != null && !validationResult.isValid()) {
			throw new InvalidRequestException(validationResult.getTaxCalculationErrors());
		}

		List<TaxCalculationInputDTO> taxCalculationRuleEngineRequest = taxCalculationRuleEngineRequestMapper
				.convertTo(taxCalculationRequest).getRuleEngineRequest();

		List<TaxCalculationOutputDTO> taxCalculationOutputDtos = genericTaxCalculationService
				.calculateTax(taxCalculationRuleEngineRequest, tenantId);

		if (CollectionUtils.isEmpty(taxCalculationOutputDtos)) {
			LOGGER.error(" Items and tax line with no content are {}",
					JsonUtil.toJsonString(taxCalculationRuleEngineRequest));
			LOGGER.error(TaxCalculationProcessingStatusCode.CONTENT_NOT_FOUND.getValue());
			throw new ApplicationException(TaxCalculationProcessingStatusCode.CONTENT_NOT_FOUND.getValue(),
					TaxCalculationProcessingStatusCode.CONTENT_NOT_FOUND, TaxCalculationStatus.NO_CONTENT);

		}

		TaxCalculationResponseBuilder responseBuilder = new TaxCalculationResponseBuilder()
				.from(taxCalculationOutputDtos, taxCalculationRequest);
		TaxCalculationResponseLine result = responseBuilder.build();
		LOGGER.debug("Response builder result {} ", JsonUtil.toJsonString(result));
		responseValidator.validatePartialContent(taxCalculationRequest, result);
		LOGGER.info("Tax Calculation Response {}", JsonUtil.toJsonString(result));
		response.setResult(result);
		response.setStatus(TaxCalculationStatus.SUCCESS);
		if (!CollectionUtils.isEmpty(warnings.getDetails())) {
			response.setProcessingStatusCode(
					TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY_WITH_WARNINGS);
			response.setStatusMessage(
					TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY_WITH_WARNINGS.getValue());
			response.setWarning(warnings);
			return response;
		}
		response.setProcessingStatusCode(TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY);
		response.setStatusMessage(TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY.getValue());

		return response;
	}
}
