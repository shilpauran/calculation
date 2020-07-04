package com.sap.slh.tax.calculation.service.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationRequestCheckFieldsDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationRequestCheckFieldsMap;
import com.sap.slh.tax.calculation.model.common.ItemResult;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponseLine;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.model.common.TaxResult;
import com.sap.slh.tax.calculation.service.TaxCalculationAmountCalculator;
import com.sap.slh.tax.calculation.service.mapper.impl.TaxCalculationRequestCheckFieldMapper;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

public class TaxCalculationResponseBuilder {

	private TaxCalculationAmountCalculator calculator;
	private TaxCalculationRequestCheckFieldMapper checkFieldMapper;

	private TaxCalculationResponseLine responseLine;

	public TaxCalculationResponseBuilder() {
		responseLine = new TaxCalculationResponseLine();
		checkFieldMapper = new TaxCalculationRequestCheckFieldMapper();
		calculator = new TaxCalculationAmountCalculator();
	}

	public TaxCalculationResponseBuilder setId(String id) {
		this.responseLine.setId(id);
		return this;
	}

	public TaxCalculationResponseBuilder setItems(List<ItemResult> items) {
		this.responseLine.setItems(items);
		return this;
	}

	public TaxCalculationResponseBuilder from(List<TaxCalculationOutputDTO> ruleEngineOutputs,
			TaxCalculationRequest taxCalculationRequest) {

		ItemResult itemExistsLine = null;

		TaxCalculationRequestCheckFieldsMap idMap = checkFieldMapper.convertTo(taxCalculationRequest);
		this.setId(taxCalculationRequest.getId());

		for (TaxCalculationOutputDTO ruleEngineResult : ruleEngineOutputs) {

			if (ruleEngineResult != null) {
				TaxCalculationRequestCheckFieldsDTO checkFields = idMap.getIdmap()
						.get(ruleEngineResult.getTaxes().getId());
				if (ruleEngineResult.getTaxes().getId() != null) {
					String[] idSplit = ruleEngineResult.getTaxes().getId().split("_");
					String itemId = idSplit[0];
					String taxId = idSplit[1];

					TaxResult result = prepareResult(ruleEngineResult, checkFields, taxId);
					itemExistsLine = findEntryLine(responseLine, itemId);

					if (itemExistsLine == null) {
						ItemResult item = new ItemResult();
						item.setId(itemId);
						List<TaxResult> taxes = new ArrayList<>();
						taxes.add(result);
						item.setTaxes(taxes);
						List<ItemResult> items = new ArrayList<>();
						items.add(item);
						if (CollectionUtils.isEmpty(responseLine.getItems())) {
							this.setItems(items);
						} else {
							responseLine.getItems().add(item);
						}

					} else {
						List<TaxResult> taxes = new ArrayList<>();
						taxes.add(result);
						itemExistsLine.getTaxes().add(result);
					}
				}
			}
		}

		return this;
	}

	private TaxResult prepareResult(TaxCalculationOutputDTO ruleEngineResult,
			TaxCalculationRequestCheckFieldsDTO checkFields, String taxId) {

		TaxResult result = new TaxResult();
		result.setId(taxId);
		int decimalPlaces = ruleEngineResult.getTaxes().getDecimalPlaces() == null ? TaxCalculationConstants.DEFAULT_DECIMAL_PLACES:ruleEngineResult.getTaxes().getDecimalPlaces();
		String roundingMethod = ruleEngineResult.getTaxes().getRoundingMethod() == null ? TaxCalculationConstants.DEFAULT_ROUND_HALF_UP:ruleEngineResult.getTaxes().getRoundingMethod();
		if (ruleEngineResult.getTaxes().getTaxBaseAmount() != null) {	
			BigDecimal baseAmount = ruleEngineResult.getTaxes().getTaxBaseAmount();
			if (!checkFields.getIsTaxEventNonTaxable()) {

				BigDecimal taxAmount = calculator.calculateAmount(baseAmount, checkFields.getTaxRate());
				result.setTaxAmount(calculator.roundAmount(taxAmount, roundingMethod, decimalPlaces));

				if (TaxLine.DueCategoryCode.RECEIVABLE == checkFields.getDueCategoryCode()) {
					BigDecimal nonDeductibleTaxAmount = calculator.calculateAmount(taxAmount,
							checkFields.getNonDeductibilityTaxRate());
					BigDecimal deductibleTaxAmount = taxAmount.subtract(nonDeductibleTaxAmount);

					result.setNonDeductibleTaxAmount(
							calculator.roundAmount(nonDeductibleTaxAmount, roundingMethod, decimalPlaces));

					result.setDeductibleTaxAmount(
							calculator.roundAmount(deductibleTaxAmount, roundingMethod, decimalPlaces));
				}
			}
			result.setTaxableBaseAmount(calculator.roundAmount(baseAmount, roundingMethod, decimalPlaces));
		}

		return result;
	}

	private ItemResult findEntryLine(TaxCalculationResponseLine taxCalculationResponse, String itemId) {
		ItemResult item = null;
		if (!CollectionUtils.isEmpty(taxCalculationResponse.getItems())) {
			for (ItemResult itemResult : taxCalculationResponse.getItems()) {
				if (itemResult.getId().equals(itemId)) {
					item = itemResult;
					break;
				}
			}
		}
		return item;
	}

	public TaxCalculationResponseLine build() {
		return this.responseLine;
	}
}
