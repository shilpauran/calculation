package com.sap.slh.tax.calculation.service.builder;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.dto.Taxes;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponseLine;
import com.sap.slh.tax.calculation.service.TaxCalculationAmountCalculator;


@RunWith(MockitoJUnitRunner.class)
public class TaxCalculationResponseBuilderTest {

	private TaxCalculationRequest taxCalculationRequest;
	private List<TaxCalculationOutputDTO> taxCalculationResults;
	private BigDecimal baseAmount = new BigDecimal("1000.00");
	private Double rate = 10.00;
	private TaxCalculationRequest requestForResponseBuilder;
	
	@Mock
	SimpleDateFormat dateFormat;
	
	@Mock
	private TaxCalculationAmountCalculator mockCalculator;
	
	@InjectMocks
	TaxCalculationResponseBuilder target;
	
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
			taxLine.setTaxTypeCode(taxTypeCode);
			taxLine.setDueCategoryCode(TaxLine.DueCategoryCode.RECEIVABLE);
			taxLine.setIsReverseChargeRelevant(true);
			taxLine.setNonDeductibleTaxRate(100.00);
			taxLine.setTaxRate(rate);
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
	
		//response init
		taxCalculationResults = new ArrayList<>();
		TaxCalculationOutputDTO result1 = new TaxCalculationOutputDTO();
		Taxes taxLine1 = new Taxes();
	
		taxLine1.setId("Item1_1");
		taxLine1.setTaxBaseAmount(baseAmount);
		taxLine1.setRoundingMethod("half_up");
		taxLine1.setDecimalPlaces(2);
		result1.setTaxes(taxLine1);
		
		TaxCalculationOutputDTO result2 = new TaxCalculationOutputDTO();
		Taxes taxLine2 = new Taxes();
		taxLine2.setId("Item1_2");
		taxLine2.setTaxBaseAmount(baseAmount);
		taxLine2.setRoundingMethod("half_up");
		taxLine2.setDecimalPlaces(2);
		result2.setTaxes(taxLine2);
		
		TaxCalculationOutputDTO result3 = new TaxCalculationOutputDTO();
		Taxes taxLine3 = new Taxes();
		taxLine3.setId("Item2_1");
		taxLine3.setTaxBaseAmount(baseAmount);
		taxLine3.setRoundingMethod("half_up");
		taxLine3.setDecimalPlaces(2);
		result3.setTaxes(taxLine3);
		
		taxCalculationResults.add(result1);
		taxCalculationResults.add(result2);
		taxCalculationResults.add(result3);
		
		
		requestForResponseBuilder = new TaxCalculationRequest();
		List<Item> items2 = new ArrayList<>();
		Item item2 = new Item();
		Item item3 = new Item();
		List<TaxLine> taxes2 = new ArrayList<>();
		List<TaxLine> taxes3 = new ArrayList<>();
		TaxLine tax1 = new TaxLine();
		tax1.setId("1");
		tax1.setTaxTypeCode("1");
		tax1.setDueCategoryCode(TaxLine.DueCategoryCode.RECEIVABLE);
		tax1.setIsReverseChargeRelevant(true);
		tax1.setNonDeductibleTaxRate(100.00);
		tax1.setTaxRate(rate);
		tax1.setIsReverseChargeRelevant(true);
		
		TaxLine tax2 = new TaxLine();
		tax2.setId("2");
		tax2.setTaxTypeCode("2");
		tax2.setDueCategoryCode(TaxLine.DueCategoryCode.RECEIVABLE);
		tax2.setIsReverseChargeRelevant(true);
		tax2.setNonDeductibleTaxRate(100.00);
		tax2.setTaxRate(rate);
		tax2.setIsReverseChargeRelevant(true);
		
		taxes2.add(tax1);
		taxes2.add(tax2);
		item2.setTaxes(taxes2);
		item2.setId("Item1");
		item2.setCountryRegionCode(Item.CountryRegionCode.CA);
		item2.setIsTaxEventNonTaxable(false);
		item2.setQuantity(new BigDecimal("100"));
		item2.setTaxEventCode("11");
		item2.setUnitPrice(new BigDecimal("10"));
		
		TaxLine tax3 = new TaxLine();
		tax3.setId("1");
		tax3.setTaxTypeCode("1");
		tax3.setDueCategoryCode(TaxLine.DueCategoryCode.RECEIVABLE);
		tax3.setIsReverseChargeRelevant(true);
		tax3.setNonDeductibleTaxRate(100.00);
		tax3.setTaxRate(rate);
		tax3.setIsReverseChargeRelevant(true);
		taxes3.add(tax3);
		
		item3.setTaxes(taxes3);
		item3.setId("Item2");
		item3.setCountryRegionCode(Item.CountryRegionCode.CA);
		item3.setIsTaxEventNonTaxable(true);
		item3.setQuantity(new BigDecimal("100"));
		item3.setTaxEventCode("11");
		item3.setUnitPrice(new BigDecimal("10"));
		
		items2.add(item2);
		items2.add(item3);
		requestForResponseBuilder.setId("SO_1");
		requestForResponseBuilder.setItems(items);
		requestForResponseBuilder.setAmountTypeCode(TaxCalculationRequest.AmountTypeCode.NET);
		requestForResponseBuilder.setCurrencyCode(TaxCalculationRequest.CurrencyCode.CAD);
		requestForResponseBuilder.setDate(new Date(20190727));
		requestForResponseBuilder.setItems(items2);

	}
	
	@Test
	public void fromAndBuildTest() {
		
		//Mock
		Mockito.when(mockCalculator.calculateAmount(any(BigDecimal.class), any(Double.class)))
		.thenReturn(new BigDecimal("100.00"));
		
		//Execute
		TaxCalculationResponseLine calculationResponseLine = target.from(taxCalculationResults,requestForResponseBuilder).build();
		
		//Test
		assertNotNull(calculationResponseLine);
		
	}

}

