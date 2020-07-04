package com.sap.slh.tax.calculation.service.mapper.impl;

import java.util.HashMap;

import org.springframework.stereotype.Component;
import com.sap.slh.tax.calculation.dto.TaxCalculationRequestCheckFieldsDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationRequestCheckFieldsMap;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.service.mapper.AbstractMapper;

@Component
public class TaxCalculationRequestCheckFieldMapper extends AbstractMapper <TaxCalculationRequest,TaxCalculationRequestCheckFieldsMap>{

	public TaxCalculationRequestCheckFieldMapper() {
		super(TaxCalculationRequest.class,TaxCalculationRequestCheckFieldsMap.class);
		
	}

	@Override
	public TaxCalculationRequestCheckFieldsMap convertTo(TaxCalculationRequest source,
			TaxCalculationRequestCheckFieldsMap destination) {
		if(source == null)
			return null;
		if(destination == null)
			destination = new TaxCalculationRequestCheckFieldsMap();
		prepareHashMap(source,destination);
		return destination;
		
	}
	
	private void prepareHashMap(
			TaxCalculationRequest taxCalculationRequest, TaxCalculationRequestCheckFieldsMap checkFieldsMap ) {

		String id = "";
		HashMap<String, TaxCalculationRequestCheckFieldsDTO> idMap = new HashMap<>();
		
		for (Item item : taxCalculationRequest.getItems()) {
			String itemId = item.getId();
			for (TaxLine tax : item.getTaxes()) {
				String taxId = tax.getId();
				id = id.concat(itemId).concat("_").concat(taxId);
				TaxCalculationRequestCheckFieldsDTO checkFields = new TaxCalculationRequestCheckFieldsDTO();
				checkFields.setDueCategoryCode(tax.getDueCategoryCode());
				checkFields.setIsTaxEventNonTaxable(item.getIsTaxEventNonTaxable());
				checkFields.setTaxRate(tax.getTaxRate());
				checkFields.setNonDeductibilityTaxRate(tax.getNonDeductibleTaxRate());
				idMap.put(id, checkFields);
				id = "";

			}
		}
		checkFieldsMap.setIdmap(idMap);
	}

	@Override
	public TaxCalculationRequest convertFrom(TaxCalculationRequestCheckFieldsMap source,
			TaxCalculationRequest destination) {
		throw new ApplicationException(TaxCalculationProcessingStatusCode.OPERATION_UNSUPPORTED_ERROR.getValue(),
				TaxCalculationProcessingStatusCode.OPERATION_UNSUPPORTED_ERROR);
	}

}
