package com.sap.slh.tax.calculation.service.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.dto.ReverseCharge;
import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationRuleEngineRequest;
import com.sap.slh.tax.calculation.dto.TaxDetails;
import com.sap.slh.tax.calculation.dto.TaxLineBRS;
import com.sap.slh.tax.calculation.dto.TaxRates;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.service.mapper.AbstractMapper;
import com.sap.slh.tax.calculation.utility.DateUtility;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

@Component
public class TaxCalculationRuleEngineRequestMapper
		extends AbstractMapper<TaxCalculationRequest, TaxCalculationRuleEngineRequest> {

	public TaxCalculationRuleEngineRequestMapper() {
		super(TaxCalculationRequest.class, TaxCalculationRuleEngineRequest.class);
	}

	@Override
	public TaxCalculationRuleEngineRequest convertTo(TaxCalculationRequest source,
			TaxCalculationRuleEngineRequest destination) {
		if (source == null)
			return null;
		if (destination == null) {
			destination = new TaxCalculationRuleEngineRequest();
		}
		createRuleEngineRequest(source, destination);
		return destination;
	}

	private void createRuleEngineRequest(TaxCalculationRequest source, TaxCalculationRuleEngineRequest destination) {

		List<TaxCalculationInputDTO> taxCalculationRuleEngineRequest = new ArrayList<>();
		
		for (Item item : source.getItems()) {

			for (TaxLine tax : item.getTaxes()) {
				TaxCalculationInputDTO taxCalculationInputDTO = new TaxCalculationInputDTO();
				TaxLineBRS taxLineBRS = buildTaxLine(tax, item, source);
				TaxRates taxRates = new TaxRates();
				TaxDetails taxTypeRel = new TaxDetails();
				ReverseCharge revRelevance = new ReverseCharge();
				for (TaxLine taxLine : item.getTaxes()) {
					switch (taxLine.getTaxTypeCode()) {
					case TaxCalculationConstants.TAX_TYPECODE_1:
						taxRates.setRateForTaxType1(taxLine.getTaxRate());
						taxTypeRel.setIsTaxType1Relevant(TaxCalculationConstants.TRUE);
						revRelevance.setIsTaxType1Relevant(
								taxLine.getIsReverseChargeRelevant().toString().toUpperCase());
						break;
					case TaxCalculationConstants.TAX_TYPECODE_2:
						taxRates.setRateForTaxType2(taxLine.getTaxRate());
						taxTypeRel.setIsTaxType2Relevant(TaxCalculationConstants.TRUE);
						revRelevance.setIsTaxType2Relevant(
								taxLine.getIsReverseChargeRelevant().toString().toUpperCase());
						break;
					case TaxCalculationConstants.TAX_TYPECODE_3:
						taxRates.setRateForTaxType3(taxLine.getTaxRate());
						taxTypeRel.setIsTaxType3Relevant(TaxCalculationConstants.TRUE);
						revRelevance.setIsTaxType3Relevant(
								taxLine.getIsReverseChargeRelevant().toString().toUpperCase());
						break;
					case TaxCalculationConstants.TAX_TYPECODE_4:
						taxRates.setRateForTaxType4(taxLine.getTaxRate());
						taxTypeRel.setIsTaxType4Relevant(TaxCalculationConstants.TRUE);
						revRelevance.setIsTaxType4Relevant(
								taxLine.getIsReverseChargeRelevant().toString().toUpperCase());
						break;
					default: break;

					}
				}
				taxCalculationInputDTO.setReverseCharge(revRelevance);
				taxCalculationInputDTO.setTaxBaseInput(taxLineBRS);
				taxCalculationInputDTO.setTaxRates(taxRates);
				taxCalculationInputDTO.setTaxDetails(taxTypeRel);
				taxCalculationRuleEngineRequest.add(taxCalculationInputDTO);
			}
		}
		destination.setRuleEngineRequest(taxCalculationRuleEngineRequest);
	}

	private TaxLineBRS buildTaxLine(TaxLine tax, Item item, TaxCalculationRequest source) {
		TaxLineBRS taxLineBRS = new TaxLineBRS();
		taxLineBRS.setId(item.getId() + "_" + tax.getId());
		taxLineBRS.setCountryCode(item.getCountryRegionCode());
		taxLineBRS.setCurrencyCode(source.getCurrencyCode());
		taxLineBRS.setDate(DateUtility.toStringFromDate(source.getDate()));
		taxLineBRS.setAmountTypeCode(source.getAmountTypeCode());
		taxLineBRS.setQuantity(item.getQuantity());
		taxLineBRS.setTaxTypeCode(tax.getTaxTypeCode());
		taxLineBRS.setUnitPrice(item.getUnitPrice());
		taxLineBRS.setIsTaxEventNonTaxable(item.getIsTaxEventNonTaxable().toString().toUpperCase());
		taxLineBRS.setTaxEventCode(item.getTaxEventCode());
		taxLineBRS.setDueCategoryCode(tax.getDueCategoryCode().value());
		taxLineBRS.setIsReverseChargeRelevant(tax.getIsReverseChargeRelevant().toString().toUpperCase());
		taxLineBRS.setTaxRate(tax.getTaxRate());
		return taxLineBRS;

	}

	@Override
	public TaxCalculationRequest convertFrom(TaxCalculationRuleEngineRequest source,
			TaxCalculationRequest destination) {
		throw new ApplicationException(TaxCalculationProcessingStatusCode.OPERATION_UNSUPPORTED_ERROR.getValue(),
				TaxCalculationProcessingStatusCode.OPERATION_UNSUPPORTED_ERROR);
	}

}
