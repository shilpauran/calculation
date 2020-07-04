package com.sap.slh.tax.calculation.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;

@Service("cacheTaxCalculationService")
public class CacheTaxCalculationService extends TaxCalculationServiceChain {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheTaxCalculationService.class);

	@Override
	public List<TaxCalculationOutputDTO> calculateTax(List<TaxCalculationInputDTO> taxCalculationInput,
			String tenantId) {

		LOGGER.info("Fetching form JSON Rule Engine");
		List<TaxCalculationOutputDTO> outputs = successor.calculateTax(taxCalculationInput, tenantId);
		return outputs;

	}
}