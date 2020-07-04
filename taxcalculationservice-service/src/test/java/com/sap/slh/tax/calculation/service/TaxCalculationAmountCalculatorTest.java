package com.sap.slh.tax.calculation.service;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TaxCalculationAmountCalculatorTest {

	@InjectMocks
	TaxCalculationAmountCalculator target;
	
	private BigDecimal baseAmount;
	private Double rate;
	@Before
	public void init() {
		baseAmount = BigDecimal.valueOf(1000.00);
		rate = 10.00;
	}
	
	@Test
	public void amountCalculateTest() {
		
		//Execute
		BigDecimal rateBd = target.calculateAmount(baseAmount, rate);
		
		//Test
		assertEquals(new BigDecimal("100.00"),rateBd);
	}
	
	@Test
	public void roundAmountTest() {
		
		BigDecimal amount = target.roundAmount(new BigDecimal(100.99999999),"ROUND_HALF_UP" ,2);
		assertEquals(amount,new BigDecimal("101.00"));
	}

}
