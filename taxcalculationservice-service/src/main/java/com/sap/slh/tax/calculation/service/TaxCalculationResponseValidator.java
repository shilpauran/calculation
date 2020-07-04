package com.sap.slh.tax.calculation.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.ItemResult;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponseLine;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.model.common.TaxResult;
import com.sap.slh.tax.calculation.utility.JsonUtil;

@Component
public class TaxCalculationResponseValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaxCalculationResponseValidator.class);

	public void validateNoContent(List<TaxCalculationInputDTO> taxCalculationRequest,
			List<TaxCalculationOutputDTO> taxCalculationResult) {

		LOGGER.error("Rule Engine response is  {}", JsonUtil.toJsonString(taxCalculationResult));
		List<TaxCalculationInputDTO> calculationRequestwithNoResponse = taxCalculationRequest.stream()
				.filter(calcRequest -> taxCalculationResult.stream()
						.noneMatch(calcResult -> calcResult != null && calcResult.getTaxes() != null
								&& calcResult.getTaxes().getId() != null
								&& calcRequest.getTaxBaseInput().getId().equals(calcResult.getTaxes().getId())
								&& calcResult.getTaxes().getTaxBaseAmount() != null ))
				.collect(Collectors.toList());

		// Rule Engine responses without id field value but have other values for other
		// fields are considered No Content
		if (calculationRequestwithNoResponse.size() == taxCalculationResult.size()) {
			LOGGER.error(" Items and tax line with no content {}",
					JsonUtil.toJsonString(calculationRequestwithNoResponse));
			LOGGER.error(TaxCalculationProcessingStatusCode.CONTENT_NOT_FOUND.getValue());
			throw new ApplicationException(TaxCalculationProcessingStatusCode.CONTENT_NOT_FOUND.getValue(),
					TaxCalculationProcessingStatusCode.CONTENT_NOT_FOUND, TaxCalculationStatus.NO_CONTENT);

		}

	}

	public void validatePartialContent(TaxCalculationRequest taxCalculationRequest,
			TaxCalculationResponseLine taxCalculationResponse) {

		if (taxCalculationRequest != null && taxCalculationResponse != null) {
			boolean isItemPartial = false;
			if (taxCalculationRequest.getItems().size() != taxCalculationResponse.getItems().size()) {
				isItemPartial = true;
			} else {
				Map<String, Item> reqItemMap = taxCalculationRequest.getItems().stream()
						.collect(Collectors.toMap(Item::getId, item -> item));
				isItemPartial = checkItemsPartial(taxCalculationResponse.getItems(), reqItemMap);
			}
			if (isItemPartial) {
				LOGGER.error("Partial Content Error occured. Request : {} and Response : : {} ",
						JsonUtil.toJsonString(taxCalculationRequest), JsonUtil.toJsonString(taxCalculationResponse));
				throw new ApplicationException(TaxCalculationProcessingStatusCode.PARTIAL_CONTENT.getValue(),
						TaxCalculationProcessingStatusCode.PARTIAL_CONTENT, TaxCalculationStatus.PARTIAL_CONTENT);

			}
		}

	}

	private Boolean checkItemsPartial(List<ItemResult> items, Map<String, Item> requestItemMap) {

		for (ItemResult respItem : items) {
			Item reqItem = requestItemMap.get(respItem.getId());
			List<TaxLine> reqTaxes = reqItem.getTaxes();
			Boolean isTaxEventNonTaxable = reqItem.getIsTaxEventNonTaxable();
			if (respItem.getTaxes().size() != reqTaxes.size()) {
				return true;
			} else {
				Map<String, TaxLine.DueCategoryCode> reqTaxLineMap = reqTaxes.stream()
						.collect(Collectors.toMap(TaxLine::getId, TaxLine::getDueCategoryCode));

				if (checkTaxlinesPartial(respItem.getTaxes(), reqTaxLineMap, isTaxEventNonTaxable))
					return true;

			}
		}
		return false;

	}

	private Boolean checkTaxlinesPartial(List<TaxResult> taxes, Map<String, TaxLine.DueCategoryCode> requestTaxLineMap,
			Boolean isTaxEventNonTaxable) {

		for (TaxResult respTax : taxes) {
			if (respTax.getTaxableBaseAmount() == null) {
				return true;
			}
			if (!isTaxEventNonTaxable && respTax.getTaxAmount() == null) {
				return true;
			}
			if ((!isTaxEventNonTaxable
					&& requestTaxLineMap.get(respTax.getId()).equals(TaxLine.DueCategoryCode.RECEIVABLE))
					&& (respTax.getDeductibleTaxAmount() == null || respTax.getNonDeductibleTaxAmount() == null)) {
				return true;
			}
		}
		return false;

	}

}