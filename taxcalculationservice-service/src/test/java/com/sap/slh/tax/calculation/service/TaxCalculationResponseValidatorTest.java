package com.sap.slh.tax.calculation.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.dto.TaxLineBRS;
import com.sap.slh.tax.calculation.dto.Taxes;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.ItemResult;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponseLine;
import com.sap.slh.tax.calculation.model.common.TaxLine;
import com.sap.slh.tax.calculation.model.common.TaxResult;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculationResponseValidatorTest {

	private TaxCalculationResponseValidator target = new TaxCalculationResponseValidator();

	private TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();

	private TaxCalculationResponseLine taxCalculationResponse = new TaxCalculationResponseLine();

	List<TaxCalculationInputDTO> taxCalculationRuleEngineRequest = new ArrayList<>();
	List<TaxCalculationOutputDTO> taxCalculationResult = new ArrayList<>();
	TaxCalculationInputDTO calcRequest;
	TaxResult taxLine1 = new TaxResult();
	TaxResult taxLine2 = new TaxResult();

	@Before
	public void init() {

		// No Content Initialization
		TaxCalculationInputDTO calcRequest = new TaxCalculationInputDTO();
		TaxLineBRS taxline = new TaxLineBRS();
		taxline.setId("Item1_1");
		calcRequest.setTaxBaseInput(taxline);
		taxCalculationRuleEngineRequest.add(calcRequest);
		calcRequest = new TaxCalculationInputDTO();
		taxline = new TaxLineBRS();
		taxline.setId("Item1_3");
		calcRequest.setTaxBaseInput(taxline);
		taxCalculationRuleEngineRequest.add(calcRequest);
		Taxes taxes = new Taxes();
		TaxCalculationOutputDTO calcResult = new TaxCalculationOutputDTO();
		calcResult.setTaxes(taxes);
		taxCalculationResult.add(calcResult);
		calcResult = new TaxCalculationOutputDTO();
		taxes = new Taxes();
		calcResult.setTaxes(taxes);
		taxCalculationResult.add(calcResult);

		// Partial Content Initialization
		List<Item> reqItems = new ArrayList<>();
		Item reqItem1 = new Item();
		Item reqItem2 = new Item();
		List<TaxLine> reqTaxes = new ArrayList<>();

		TaxLine reqTax1 = new TaxLine();
		reqTax1.setId("t1");
		reqTax1.setDueCategoryCode(TaxLine.DueCategoryCode.PAYABLE);
		TaxLine reqTax2 = new TaxLine();
		reqTax2.setId("t2");
		reqTax2.setDueCategoryCode(TaxLine.DueCategoryCode.RECEIVABLE);

		reqTaxes.add(reqTax1);
		reqTaxes.add(reqTax2);
		reqItem1.setId("i1");
		reqItem1.setIsTaxEventNonTaxable(false);
		reqItem1.setTaxes(reqTaxes);
		reqItem2.setId("i2");
		reqItem2.setIsTaxEventNonTaxable(false);
		reqItem2.setTaxes(reqTaxes);

		reqItems.add(reqItem1);
		reqItems.add(reqItem2);
		taxCalculationRequest.setId("r1");
		taxCalculationRequest.setItems(reqItems);

		// Response

		taxLine1.setId("t1");
		BigDecimal amount = new BigDecimal(10);
		taxLine1.setTaxableBaseAmount(amount);
		taxLine1.setTaxAmount(amount);
		taxLine1.setDeductibleTaxAmount(amount);
		taxLine1.setNonDeductibleTaxAmount(amount);

		taxLine2.setId("t2");
		BigDecimal amount2 = new BigDecimal(10);
		taxLine2.setTaxableBaseAmount(amount2);
		taxLine2.setTaxAmount(amount2);
		taxLine2.setDeductibleTaxAmount(amount2);
		taxLine2.setNonDeductibleTaxAmount(amount2);

		taxCalculationResponse.setId("r1");

	}

	@Test(expected = ApplicationException.class)
	public void test_noContent() {
		target.validateNoContent(taxCalculationRuleEngineRequest, taxCalculationResult);
	}

	@Test
	public void test_contentFound() {
		taxCalculationResult.clear();
		Taxes taxes = new Taxes();
		taxes.setId("Item1_1");
		taxes.setDecimalPlaces(2);
		taxes.setRoundingMethod("HALF_ROUND_UP");
		taxes.setTaxBaseAmount(new BigDecimal("100"));
		TaxCalculationOutputDTO calcResult = new TaxCalculationOutputDTO();
		calcResult.setTaxes(taxes);
		taxCalculationResult.add(calcResult);
		taxCalculationRuleEngineRequest.remove(1);
		target.validateNoContent(taxCalculationRuleEngineRequest, taxCalculationResult);
	}

	@Test(expected = ApplicationException.class)
	public void test_withBaseAmountMandatory() {
		calcRequest = new TaxCalculationInputDTO();
		TaxLineBRS taxline = new TaxLineBRS();
		taxline.setId("Item1_4");
		calcRequest.setTaxBaseInput(taxline);
		TaxCalculationOutputDTO calcResult = new TaxCalculationOutputDTO();
		Taxes taxes = new Taxes();
		taxes.setId("Item1_4");
		calcResult.setTaxes(taxes);
		taxCalculationRuleEngineRequest.add(calcRequest);
		taxCalculationResult.add(calcResult);
		target.validateNoContent(taxCalculationRuleEngineRequest, taxCalculationResult);
	}

	@Test
	public void test_one_item_missing() {
		ItemResult item1 = new ItemResult();
		ItemResult item2 = new ItemResult();
		List<ItemResult> items = new ArrayList<>();
		List<TaxResult> taxes = new ArrayList<>();
		item1.setId("i1");
		item2.setId("i2");

		taxes.add(taxLine1);
		taxes.add(taxLine2);
		item1.setTaxes(taxes);
		items.add(item1);

		taxCalculationResponse.setItems(items);

		try {
			// Execution
			target.validatePartialContent(taxCalculationRequest, taxCalculationResponse);
			fail();
		} catch (ApplicationException e) {
			// Verify
			assertEquals(TaxCalculationProcessingStatusCode.PARTIAL_CONTENT.getValue(), e.getMessage());
		}

	}

	@Test
	public void test_one_taxline_missing() {
		ItemResult item1 = new ItemResult();
		ItemResult item2 = new ItemResult();
		List<ItemResult> items = new ArrayList<>();
		List<TaxResult> taxes = new ArrayList<>();

		item1.setId("i1");
		item2.setId("i2");

		taxes.add(taxLine1);
		item1.setTaxes(taxes);
		item2.setTaxes(taxes);
		items.add(item1);
		items.add(item2);

		taxCalculationResponse.setItems(items);

		try {
			// Execution
			target.validatePartialContent(taxCalculationRequest, taxCalculationResponse);
			fail();
		} catch (ApplicationException e) {
			// Verify
			assertEquals(TaxCalculationProcessingStatusCode.PARTIAL_CONTENT.getValue(), e.getMessage());
		}

	}

	@Test
	public void test_one_taxline_partial() {
		ItemResult item1 = new ItemResult();
		ItemResult item2 = new ItemResult();
		List<ItemResult> items = new ArrayList<>();
		List<TaxResult> taxes = new ArrayList<>();

		item1.setId("i1");
		item2.setId("i2");

		taxes.add(taxLine1);
		taxLine2.setTaxableBaseAmount(null);
		taxes.add(taxLine2);
		item1.setTaxes(taxes);
		item2.setTaxes(taxes);
		items.add(item1);
		items.add(item2);

		taxCalculationResponse.setItems(items);

		try {
			// Execution
			target.validatePartialContent(taxCalculationRequest, taxCalculationResponse);
			fail();
		} catch (ApplicationException e) {
			// Verify
			assertEquals(TaxCalculationProcessingStatusCode.PARTIAL_CONTENT.getValue(), e.getMessage());
		}
	}

	@Test
	public void test_receivable_taxline_DeductibleTaxAmountisNull() {
		ItemResult item1 = new ItemResult();
		ItemResult item2 = new ItemResult();
		List<ItemResult> items = new ArrayList<>();
		List<TaxResult> taxes = new ArrayList<>();

		item1.setId("i1");
		item2.setId("i2");

		taxes.add(taxLine1);
		taxLine2.setDeductibleTaxAmount(null);
		taxes.add(taxLine2);
		item1.setTaxes(taxes);
		item2.setTaxes(taxes);
		items.add(item1);
		items.add(item2);

		taxCalculationResponse.setItems(items);

		try {
			// Execution
			target.validatePartialContent(taxCalculationRequest, taxCalculationResponse);
			fail();
		} catch (ApplicationException e) {
			// Verify
			assertEquals(TaxCalculationProcessingStatusCode.PARTIAL_CONTENT.getValue(), e.getMessage());
		}
	}

	@Test
	public void test_payable_taxline_DeductibleTaxAmountIsNull() {
		ItemResult item1 = new ItemResult();
		ItemResult item2 = new ItemResult();
		List<ItemResult> items = new ArrayList<>();
		List<TaxResult> taxes = new ArrayList<>();

		item1.setId("i1");
		item2.setId("i2");

		taxes.add(taxLine2);
		taxLine1.setDeductibleTaxAmount(null);
		taxes.add(taxLine1);
		item1.setTaxes(taxes);
		item2.setTaxes(taxes);
		items.add(item1);
		items.add(item2);

		taxCalculationResponse.setItems(items);
		try {
			target.validatePartialContent(taxCalculationRequest, taxCalculationResponse);
		} catch (ApplicationException e) {
			fail();
		}

	}
	
	@Test
	public void test_TaxAmountIsNull() {
		ItemResult item1 = new ItemResult();
		ItemResult item2 = new ItemResult();
		List<ItemResult> items = new ArrayList<>();
		List<TaxResult> taxes = new ArrayList<>();

		item1.setId("i1");
		item2.setId("i2");

		taxes.add(taxLine2);
		taxLine1.setTaxAmount(null);
		taxes.add(taxLine1);
		item1.setTaxes(taxes);
		item2.setTaxes(taxes);
		items.add(item1);
		items.add(item2);
		
		List<Item> reqItems = taxCalculationRequest.getItems();
		Item it = reqItems.get(0);
		it.setIsTaxEventNonTaxable(false);
		reqItems.set(0, it);
		TaxCalculationRequest req = new TaxCalculationRequest();
		req.setId("r1");
		req.setItems(reqItems);

		taxCalculationResponse.setItems(items);
		try {
			target.validatePartialContent(taxCalculationRequest, taxCalculationResponse);
			fail();
		} catch (ApplicationException e) {
			// Verify
			assertEquals(TaxCalculationProcessingStatusCode.PARTIAL_CONTENT.getValue(), e.getMessage());
		}

	}

}
