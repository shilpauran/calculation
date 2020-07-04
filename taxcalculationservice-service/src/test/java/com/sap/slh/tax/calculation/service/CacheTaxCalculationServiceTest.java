package com.sap.slh.tax.calculation.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;

@RunWith(MockitoJUnitRunner.class)
public class CacheTaxCalculationServiceTest {

	@Mock
	TaxCalculationServiceChain mockSuccessor;

	@InjectMocks
	CacheTaxCalculationService target;

	@Test
	public void test() {
		List<TaxCalculationInputDTO> taxCalculationInput = new ArrayList<>();
		String tenantId = "123";
		List<TaxCalculationOutputDTO> outputs = new ArrayList<>();
		// Mock
		Mockito.when(mockSuccessor.calculateTax(taxCalculationInput, tenantId)).thenReturn(outputs);

		// Execute
		List<TaxCalculationOutputDTO> result = target.calculateTax(taxCalculationInput, tenantId);

		// Verify
		assertNotNull(result);
	}

}
