package com.sap.slh.tax.calculation.service.mapper.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sap.slh.tax.calculation.dto.TaxCalculationRequestCheckFieldsMap;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculationRequestCheckFieldMapperTest {
	
	@InjectMocks
	TaxCalculationRequestCheckFieldMapper target;
	
	private TaxCalculationRequest taxCalculationRequest;
	
	@Before
	public void init() {
		taxCalculationRequest = new TaxCalculationRequest();
		List<Item> items = new ArrayList<>();
		Item item = new Item();
		List<TaxLine> taxes = new ArrayList<>();
		String[] taxTypeCodes = {"1","2","3","4","5"};
		
		for(String taxTypeCode: taxTypeCodes)
		{
			TaxLine taxLine = new TaxLine();
			taxLine.setId("1");
			taxLine.setTaxTypeCode(taxTypeCode);
			taxLine.setDueCategoryCode(TaxLine.DueCategoryCode.RECEIVABLE);
			taxLine.setIsReverseChargeRelevant(true);
			taxLine.setNonDeductibleTaxRate(100.00);
			taxLine.setTaxTypeCode(taxTypeCode);
			taxLine.setIsReverseChargeRelevant(true);
			taxes.add(taxLine);
		}

		item.setTaxes(taxes);
		item.setId("Item2");
		item.setCountryRegionCode(Item.CountryRegionCode.CA);
		item.setIsTaxEventNonTaxable(false);
		item.setQuantity(new BigDecimal("100"));
		item.setTaxEventCode("11");
		item.setUnitPrice(new BigDecimal("10"));
		items.add(item);
		
		taxCalculationRequest.setId("SO_1");
		taxCalculationRequest.setItems(items);
		taxCalculationRequest.setAmountTypeCode(TaxCalculationRequest.AmountTypeCode.NET);
		taxCalculationRequest.setCurrencyCode(TaxCalculationRequest.CurrencyCode.CAD);
		taxCalculationRequest.setDate(new Date(20190727));
	}
	
	@Test
	public void testConvertTo() {
		TaxCalculationRequestCheckFieldsMap idMap = target.convertTo(taxCalculationRequest,null);
		assertNotNull(idMap);
	}
	
	@Test
	public void testConvertToNull() {
		TaxCalculationRequestCheckFieldsMap idMap = target.convertTo(null,null);
		assertNull(idMap);
	}

	@Test(expected = ApplicationException.class )
	public void testConvertFrom() {
		TaxCalculationRequestCheckFieldsMap idMap = null;
		target.convertFrom(idMap,taxCalculationRequest);
	}
}
