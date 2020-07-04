package com.sap.slh.tax.calculation.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;
import com.sap.slh.tax.calculation.model.TaxCalculationValidationResult;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationWarning;
import com.sap.slh.tax.calculation.model.common.TaxCalculationErrorDetail;
import com.sap.slh.tax.calculation.model.common.TaxCalculationErrorMessage;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationWarningDetail;
import com.sap.slh.tax.calculation.utility.ErrorTokenGenerator;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

@Component

@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaxCalculationRequestValidation {

	private TaxCalculationValidationResult validationResult;

	private TaxCalculationApplicationError applicationError;

	private TaxCalculationApplicationWarning applicationWarning;

	private List<TaxCalculationErrorDetail> errorDetails;

	@Autowired
	private Gson gson;

	@Autowired
	private MessageSource messageSource;

	private static final Logger logger = LoggerFactory.getLogger(TaxCalculationRequestValidation.class);

	public TaxCalculationValidationResult validate(@RequestBody TaxCalculationRequest taxCalculationDocument,
			Locale locale) {
		String rootPath = "";
		String message = gson.toJson(taxCalculationDocument);

		init();

		logger.debug("Content successfully loaded");

		// Payload validation
		JSONObject documents = null;
		try {
			documents = new JSONObject(message);
		} catch (JSONException e1) {
			addError(rootPath, messageSource.getMessage(TaxCalculationErrorMessage.REQUEST_STRUCTURE_INVALID.getValue(),
					null, locale), TaxCalculationErrorMessage.REQUEST_STRUCTURE_INVALID);
			logger.error("Root Path : {} , {} , Exception : {}", rootPath,
					TaxCalculationErrorMessage.REQUEST_STRUCTURE_INVALID.getValue(), e1);
			return getValidationResult();
		}
		if (documents.length() == 0) {
			addError(rootPath,
					messageSource.getMessage(TaxCalculationErrorMessage.REQUEST_PAYLOAD_EMPTY.getValue(), null, locale),
					TaxCalculationErrorMessage.REQUEST_PAYLOAD_EMPTY);
			return getValidationResult();
		}

		if (getValidationResult().isValid()) {
			// Business Validations
			validateDocument(documents, locale);
		}

		return getValidationResult();
	}

	public void init() {

		validationResult = new TaxCalculationValidationResult();
		applicationError = new TaxCalculationApplicationError();
		applicationWarning = new TaxCalculationApplicationWarning();
		errorDetails = new ArrayList<>();
		List<TaxCalculationWarningDetail> warningDetails = new ArrayList<>();

		applicationWarning.setDetails(warningDetails);
		applicationError.setDetails(errorDetails);
		validationResult.setTaxCalculationWarnings(applicationWarning);
		validationResult.setTaxCalculationErrors(applicationError);
	}

	private void validateDocument(JSONObject document, Locale locale) {

		// Date validation
		String date = document.optString(TaxCalculationConstants.DATE);

		if (!isValidDate(date)) {
			addError(getPath(TaxCalculationConstants.DATE),
					messageSource.getMessage(TaxCalculationErrorMessage.DATE_INVALID.getValue(), null, locale),
					TaxCalculationErrorMessage.DATE_INVALID);
		}

		JSONArray items = document.optJSONArray(TaxCalculationConstants.ITEMS);
		validateItems(items, getPath(TaxCalculationConstants.ITEMS), locale);
	}

	private void validateItems(JSONArray items, String path, Locale locale) {
		Set<String> itemIds = new HashSet<>();

		for (int i = 0; i < items.length(); i++) {
			String itemPath = getPath(path + "[" + Integer.toString(i) + "]");

			JSONObject item = items.getJSONObject(i);
			String itemId = item.optString(TaxCalculationConstants.ID);
			logger.debug("validating Item {}", itemId);
			if (itemIds.contains(itemId)) {
				addError(getPath(itemPath, TaxCalculationConstants.ID),
						messageSource.getMessage(TaxCalculationErrorMessage.ITEM_ID_EXISTS.getValue(), null, locale),
						TaxCalculationErrorMessage.ITEM_ID_EXISTS);
			} else {
				itemIds.add(itemId);
			}

			boolean taxable = !item.optBoolean(TaxCalculationConstants.IS_TAX_EVENT_NON_TAXABLE);

			JSONArray taxes = item.optJSONArray(TaxCalculationConstants.TAXES);
			validateTaxes(taxes, taxable, getPath(itemPath, TaxCalculationConstants.TAXES), locale);

		}

	}

	private static boolean isTaxRateInvalid(String rate) {

		if (StringUtils.isBlank(rate)) {
			return true;
		}

		BigDecimal value = new BigDecimal(rate);
		if (value.compareTo(BigDecimal.valueOf(0.0)) >= 0 && value.compareTo(BigDecimal.valueOf(100.0)) <= 0) {
			return false;
		}

		return true;
	}

	private void validateTaxes(JSONArray taxes, boolean taxable, String taxesPath, Locale locale) {
		Set<String> taxIds = new HashSet<>();

		for (int i = 0; i < taxes.length(); i++) {
			String taxPath = getPath(taxesPath + "[" + Integer.toString(i) + "]");

			JSONObject tax = taxes.getJSONObject(i);
			String taxId = tax.optString(TaxCalculationConstants.ID);
			if (taxIds.contains(taxId)) {
				addError(taxesPath,
						messageSource.getMessage(TaxCalculationErrorMessage.TAX_ID_EXISTS.getValue(), null, locale),
						TaxCalculationErrorMessage.TAX_ID_EXISTS);
			} else {
				taxIds.add(taxId);
			}

			if (taxable) {
				if (!tax.has(TaxCalculationConstants.TAX_RATE)) {
					addError(
							getPath(taxPath, TaxCalculationConstants.TAX_RATE), messageSource
									.getMessage(TaxCalculationErrorMessage.TAX_RATE_MISSING.getValue(), null, locale),
							TaxCalculationErrorMessage.TAX_RATE_MISSING);
				} else if (isTaxRateInvalid(tax.optString(TaxCalculationConstants.TAX_RATE))) {
					addError(
							getPath(taxPath, TaxCalculationConstants.TAX_RATE), messageSource
									.getMessage(TaxCalculationErrorMessage.TAX_RATE_INVALID.getValue(), null, locale),
							TaxCalculationErrorMessage.TAX_RATE_INVALID);

				}
			}

			if (StringUtils.isBlank(tax.optString(TaxCalculationConstants.TAX_TYPE_CODE))) {
				addError(
						getPath(taxPath, TaxCalculationConstants.TAX_TYPE_CODE), messageSource
								.getMessage(TaxCalculationErrorMessage.TAX_TYPE_CODE_INVALID.getValue(), null, locale),
						TaxCalculationErrorMessage.TAX_TYPE_CODE_INVALID);
			}

		}

	}

	private static boolean isValidDate(String date) {
		String pattern = TaxCalculationConstants.DATE_PATTERN;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setLenient(false);
		try {
			format.parse(date.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	private void addError(String path, String message, TaxCalculationErrorMessage errorCode) {
		TaxCalculationErrorDetail errorDetail = new TaxCalculationErrorDetail();
		errorDetail.setField(path);
		errorDetail.setMessage(message);
		errorDetail.setErrorCode(errorCode);
		errorDetails.add(errorDetail);
		applicationError.setDetails(errorDetails);
	}

	private TaxCalculationValidationResult getValidationResult() {
		if (StringUtils.isBlank(applicationError.getErrorId())) {
			applicationError.setErrorId(ErrorTokenGenerator.getErrorId());
		}

		validationResult.setValid(false);
		if (applicationError.getDetails() == null || applicationError.getDetails().isEmpty()) {
			validationResult.setValid(true);
		}

		validationResult.setTaxCalculationWarnings(applicationWarning);
		validationResult.setTaxCalculationErrors(applicationError);
		return validationResult;
	}

	private String getPath(String... fields) {
		return String.join(TaxCalculationConstants.DOT, fields);
	}

}