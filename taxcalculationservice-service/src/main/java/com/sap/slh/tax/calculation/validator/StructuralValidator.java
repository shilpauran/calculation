package com.sap.slh.tax.calculation.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.exception.InvalidRequestException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationErrorDetail;
import com.sap.slh.tax.calculation.model.common.TaxCalculationErrorMessage;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.utility.ErrorTokenGenerator;

import io.micrometer.core.instrument.util.StringUtils;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StructuralValidator {
	
	@Autowired
	private MessageSource messageSource;
	Locale locale;

	TaxCalculationApplicationError error = new TaxCalculationApplicationError();
	List<TaxCalculationErrorDetail> details = new ArrayList<>();
	TaxCalculationErrorDetail detail;

	public boolean isValid(TaxCalculationRequest request, Locale locale) {
		this.locale = locale;

		requestValidate(request);

		error.setDetails(details);
		error.setErrorId(ErrorTokenGenerator.getErrorId());

		if (!CollectionUtils.isEmpty(details)) {
			throw new InvalidRequestException(error);
		}

		return true;
	}

	// header validation

	private void requestValidate(TaxCalculationRequest request) {

		// id - blank
		if (StringUtils.isBlank(request.getId())) {
			addErrorDetail("error.document.id.required", "id");
		}

		if (request.getDate() == null) {
			addErrorDetail("error.document.date.required", "date");
		}

		if (request.getAmountTypeCode() == null) {
			addErrorDetail("error.document.amountTypeCode.required", "amountTypeCode");
		}

		if (request.getCurrencyCode() == null) {
			addErrorDetail("error.document.currencyCode.required", "currencyCode");
		}

		List<Item> items = request.getItems();
		
		if(CollectionUtils.isEmpty(items)) {
			addErrorDetail("error.document.items.required", "items");
			return;
		}
		itemValidate(items);

	}

	// items validation
	private void itemValidate(List<Item> items) {

		for (Item item : items) {
			if (StringUtils.isBlank(item.getId())) {
				addErrorDetail("error.item.id.required", "id");
			}

			if (item.getQuantity() == null) {
				addErrorDetail("error.item.quantity.required", "quantity");
			}

			if (item.getUnitPrice() == null) {
				addErrorDetail("error.item.unitPrice.required", "unitPrice");
			}
			if (item.getCountryRegionCode() == null) {
				addErrorDetail("error.item.countryRegionCode.required", "countryRegionCode");
			}
			if (StringUtils.isBlank(item.getTaxEventCode())) {
				addErrorDetail("error.item.taxEventCode.required", "taxEventCode");
			}
			List<TaxLine> taxes = item.getTaxes();
			
			if(CollectionUtils.isEmpty(taxes)) {
				addErrorDetail("error.item.taxes.required", "taxes");
				continue;
			}
			taxLineValidate(taxes);

		}

	}

	// taxline validation
	private void taxLineValidate(List<TaxLine> taxes) {
		for (TaxLine taxLine : taxes) {
			if (StringUtils.isBlank(taxLine.getId())) {
				addErrorDetail("error.tax.id.required", "id");
			}
			if (StringUtils.isBlank(taxLine.getTaxTypeCode())) {
				addErrorDetail("error.tax.taxTypeCode.required", "taxTypeCode");
			}
			if (taxLine.getDueCategoryCode() == null) {
				addErrorDetail("error.tax.dueCategoryCode.required", "dueCategoryCode");
			}
		}

	}

	private void addErrorDetail(String messageKey, String field) {
		detail = new TaxCalculationErrorDetail();

		String message = messageSource.getMessage(messageKey, null, locale);
		
		detail.setField(field);
		detail.setMessage(message);
		detail.setErrorCode(TaxCalculationErrorMessage.fromValue(messageKey));
		details.add(detail);
	}

}
