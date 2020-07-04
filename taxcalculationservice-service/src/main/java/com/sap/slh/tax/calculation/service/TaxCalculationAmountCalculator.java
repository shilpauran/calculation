package com.sap.slh.tax.calculation.service;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

@Component
public class TaxCalculationAmountCalculator {
	
    public BigDecimal calculateAmount(BigDecimal baseAmount, Double rate) {
        BigDecimal rateBd = BigDecimal.valueOf(rate).divide(new BigDecimal("100"));
        return baseAmount.multiply(rateBd);
        
    }
    
    public BigDecimal roundAmount(BigDecimal amount, String roundingMethod, Integer decimalPlaces) {
		BigDecimal roundedAmount = null;
		switch (roundingMethod) {
		case TaxCalculationConstants.ROUND_HALF_UP:
			roundedAmount = amount.setScale(decimalPlaces, RoundingMode.HALF_UP);
			break;

		default:
			break;
		}
		return roundedAmount;
	}
    
}