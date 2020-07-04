package com.sap.slh.tax.calculation.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.utility.JsonUtil;

@Service("genericTaxCalculationService")
public class GenericTaxCalculationService extends TaxCalculationServiceChain {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericTaxCalculationService.class);

	@Autowired
	@Qualifier("cacheTaxCalculationService")
	private TaxCalculationServiceChain cacheTaxCalculationService;

	@Autowired
	@Qualifier("ruleEngineTaxCalculationService")
	private TaxCalculationServiceChain ruleEngineTaxCalculationService;

	/**
	 * Initialize service chain.
	 */
	@PostConstruct
	public void initialiseTaxCalculationServiceChain() {
		this.setSuccessor(cacheTaxCalculationService);
		cacheTaxCalculationService.setSuccessor(ruleEngineTaxCalculationService);
	}

	@Override
	public List<TaxCalculationOutputDTO> calculateTax(
			List<TaxCalculationInputDTO> taxCalculationInput, String tenantId) {
		
		LOGGER.debug("calculateTaxRequest : {}", JsonUtil.toJsonString(taxCalculationInput));
		List<TaxCalculationOutputDTO> outputList = Collections.emptyList();
		
		if (isValid(taxCalculationInput)) {
			outputList = successor.calculateTax(taxCalculationInput,tenantId);
		}
		return outputList;
	}
}
