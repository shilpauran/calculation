package com.sap.slh.tax.calculation.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenericTaxCalculationServiceTest {
	
	List<TaxCalculationInputDTO> taxcalculationRequest;
	List<TaxCalculationOutputDTO> outputList;

	@InjectMocks 
	GenericTaxCalculationService target;
	@Mock 
	TaxCalculationServiceChain mockSuccessor;

	@Before
	public void init() {
		

		taxcalculationRequest = new ArrayList<>();
		outputList = new ArrayList<>();
		target.initialiseTaxCalculationServiceChain();
		
	}
	
	@Test
	public void test_calculateTax_success() {
		
		//Mock
		Mockito.when(mockSuccessor.calculateTax(taxcalculationRequest,"")).thenReturn(outputList);
		
		//Execution
		List<TaxCalculationOutputDTO> outputList = target.calculateTax(taxcalculationRequest,"");
		
		//Verify
		Mockito.verify(mockSuccessor).calculateTax(taxcalculationRequest,"");
		assertNotNull(outputList);
		
	}
	
	@Test 
	public void test_calculateTax_nullRequest() {
		
		List<TaxCalculationInputDTO> nullReq = null;
		
		//Verify
		try {
			target.calculateTax(nullReq,"");
			fail();
		}catch(ApplicationException e) {
			assertEquals("The request body is empty. Please check the request and try again.", e.getMessage());
		}
	}

}