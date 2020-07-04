package com.sap.slh.tax.calculation.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sap.slh.tax.calculation.cache.dto.ExpressionCacheKey;
import com.sap.slh.tax.calculation.cache.dto.ExpressionCacheValue;
import com.sap.slh.tax.calculation.cache.lookup.CacheTaxCalculationLookupService;
import com.sap.slh.tax.calculation.dto.ReverseCharge;
import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.dto.TaxDetails;
import com.sap.slh.tax.calculation.dto.TaxLineBRS;
import com.sap.slh.tax.calculation.dto.TaxRates;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.utility.DateUtility;
import com.sap.slh.tax.calculation.utility.ExpressionHelper;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ExpressionHelper.class)
public class RuleEngineTaxCalculationServiceTest {

	@Mock
	private CacheTaxCalculationLookupService mockCacheTaxCalculationLookupService;

	@Mock
	private List<String> supportedCountries;
	
	@Mock
	private Map<ExpressionCacheKey, Set<ExpressionCacheValue>> expressionMap;
	
	@InjectMocks
	RuleEngineTaxCalculationService ruleEngine;

	private List<TaxCalculationInputDTO> taxCalculationInputDTOlist = new ArrayList<>();;
	private TaxCalculationInputDTO taxCalculationInputDTO = new TaxCalculationInputDTO();
	private TaxLineBRS taxLineBRS = new TaxLineBRS();
	private ReverseCharge reverseCharge = new ReverseCharge();
	private TaxRates taxRates = new TaxRates();
	private TaxDetails taxDetails = new TaxDetails();
	
	

	private String jsonRuleString;
	private String jsonRuleStringDefault;

	@Before
	public void init() throws CompileException, ClassNotFoundException {
				
		taxLineBRS.setId("SO_1");
		taxLineBRS.setDate("2019-08-27");
		taxLineBRS.setAmountTypeCode(TaxCalculationRequest.AmountTypeCode.GROSS);
		taxLineBRS.setCurrencyCode(TaxCalculationRequest.CurrencyCode.CAD);

		taxLineBRS.setQuantity(new BigDecimal(10));
		taxLineBRS.setUnitPrice(new BigDecimal(10));
		taxLineBRS.setCountryCode(Item.CountryRegionCode.CA);
		taxLineBRS.setTaxEventCode("11");
		taxLineBRS.setIsTaxEventNonTaxable("false");

		taxLineBRS.setTaxTypeCode(TaxCalculationConstants.TAX_TYPECODE_1);
		taxLineBRS.setTaxRate(new Double(5));
		taxLineBRS.setDueCategoryCode("RECEIVABLE");
		taxLineBRS.setIsReverseChargeRelevant("false");

		reverseCharge.setIsTaxType1Relevant(taxLineBRS.getIsReverseChargeRelevant().toString().toUpperCase());
		taxRates.setRateForTaxType1(taxLineBRS.getTaxRate());
		taxRates.setRateForTaxType2(taxLineBRS.getTaxRate());
		taxRates.setRateForTaxType3(taxLineBRS.getTaxRate());
		taxRates.setRateForTaxType4(taxLineBRS.getTaxRate());
		taxDetails.setIsTaxType1Relevant(TaxCalculationConstants.TRUE);

		taxCalculationInputDTO.setTaxBaseInput(taxLineBRS);
		taxCalculationInputDTO.setReverseCharge(reverseCharge);
		taxCalculationInputDTO.setTaxRates(taxRates);
		taxCalculationInputDTO.setTaxDetails(taxDetails);

		taxCalculationInputDTOlist.add(taxCalculationInputDTO);

		jsonRuleString = "{\r\n" + "    \"country\": \"CA\",\r\n" + "    \"rules\": [\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxEventNonTaxable == true && ( taxTypeCode.equalsIgnoreCase(\\\"1\\\") || taxTypeCode.equalsIgnoreCase(\\\"2\\\") || taxTypeCode.equalsIgnoreCase(\\\"3\\\") || taxTypeCode.equalsIgnoreCase(\\\"4\\\"))\",\r\n"
				+ "            \"parameter_names\": \"isTaxEventNonTaxable,taxTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,String\",\r\n" + "            \"results\": [\r\n"
				+ "                {\r\n" + "                    \"ruleName\": \"NonTaxableScenario\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "               {\r\n"
				+ "           \"expression\": \"(( isTaxType1Relevant == true && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"1\\\")) || ( isTaxType1Relevant == false && isTaxType2Relevant == true && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"2\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == true && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"3\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == true && taxTypeCode.equalsIgnoreCase(\\\"4\\\"))) && ( amountTypeCode.equalsIgnoreCase(\\\"Net\\\") || ( isReverseChargeRelevant == true && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")) ) \",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType2Relevant,isTaxType3Relevant,isTaxType4Relevant,taxTypeCode,amountTypeCode,isReverseChargeRelevant\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String,String,boolean\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"SingleTaxType01\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"9999-12-31\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"(( isTaxType1Relevant == true && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"1\\\")) || ( isTaxType1Relevant == false && isTaxType2Relevant == true && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"2\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == true && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"3\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == true && taxTypeCode.equalsIgnoreCase(\\\"4\\\"))) && ( isReverseChargeRelevant == false && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")) \",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType2Relevant,isTaxType3Relevant,isTaxType4Relevant,taxTypeCode,amountTypeCode,isReverseChargeRelevant\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String,String,boolean\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"SingleTaxType02\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf(1 + (taxRate / 100)), 15, java.math.RoundingMode.HALF_UP)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,taxRate\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"9999-12-31\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        \r\n" + "         {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType3Relevant == true  && amountTypeCode.equalsIgnoreCase(\\\"Net\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType3Relevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,String\",\r\n" + "            \"results\": [\r\n"
				+ "                {\r\n" + "                    \"ruleName\": \"GSTPST_01\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "\r\n" + "         {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType3Relevant == true && isTaxType1ReverseChargeRelevant == false && isTaxType3ReverseChargeRelevant == false && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType3Relevant,isTaxType1ReverseChargeRelevant,isTaxType3ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTPST_02\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf((1 + (rateForTaxType1 / 100) + (rateForTaxType3 / 100))), 15, java.math.RoundingMode.HALF_UP)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,rateForTaxType1,rateForTaxType3\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType3Relevant == true && isTaxType1ReverseChargeRelevant == true && isTaxType3ReverseChargeRelevant == false && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType3Relevant,isTaxType1ReverseChargeRelevant,isTaxType3ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTPST_03\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf((1 + (rateForTaxType3 / 100))), 15, java.math.RoundingMode.HALF_UP)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,rateForTaxType3\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType3Relevant == true && isTaxType1ReverseChargeRelevant == false && isTaxType3ReverseChargeRelevant == true && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType3Relevant,isTaxType1ReverseChargeRelevant,isTaxType3ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTPST_04\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf((1 + (rateForTaxType1 / 100))), 15, java.math.RoundingMode.HALF_UP)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,rateForTaxType1\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType3Relevant == true && isTaxType1ReverseChargeRelevant == true && isTaxType3ReverseChargeRelevant == true && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType3Relevant,isTaxType1ReverseChargeRelevant,isTaxType3ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTPST_05\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType4Relevant == true && amountTypeCode.equalsIgnoreCase(\\\"Net\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType4Relevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,String\",\r\n" + "            \"results\": [\r\n"
				+ "                {\r\n" + "                    \"ruleName\": \"GSTQST_01\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "    	{\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType4Relevant == true && isTaxType1ReverseChargeRelevant == false && isTaxType4ReverseChargeRelevant == false && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType4Relevant,isTaxType1ReverseChargeRelevant,isTaxType4ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTQST_02\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf((1 + (rateForTaxType1 / 100) + (rateForTaxType4 / 100))), 15, java.math.RoundingMode.HALF_UP)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,rateForTaxType1,rateForTaxType4\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType4Relevant == true && isTaxType1ReverseChargeRelevant == true && isTaxType4ReverseChargeRelevant == false && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType4Relevant,isTaxType1ReverseChargeRelevant,isTaxType4ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTQST_03\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf((1 + (rateForTaxType4 / 100))), 15, java.math.RoundingMode.HALF_UP)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,rateForTaxType4\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType4Relevant == true && isTaxType1ReverseChargeRelevant == false && isTaxType4ReverseChargeRelevant == true && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType4Relevant,isTaxType1ReverseChargeRelevant,isTaxType4ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTQST_04\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf((1 + (rateForTaxType1 / 100) )), 15, java.math.RoundingMode.HALF_UP)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,rateForTaxType1\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        },\r\n" + "        {\r\n"
				+ "            \"expression\": \"isTaxType1Relevant == true && isTaxType4Relevant == true && isTaxType1ReverseChargeRelevant == true && isTaxType4ReverseChargeRelevant == true && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")\",\r\n"
				+ "            \"parameter_names\": \"isTaxType1Relevant,isTaxType4Relevant,isTaxType1ReverseChargeRelevant,isTaxType4ReverseChargeRelevant,amountTypeCode\",\r\n"
				+ "            \"parameter_types\": \"boolean,boolean,boolean,boolean,String\",\r\n"
				+ "            \"results\": [\r\n" + "                {\r\n"
				+ "                    \"ruleName\": \"GSTQST_05\",\r\n"
				+ "                    \"baseAmount\": \"quantity.multiply(unitPrice)\",\r\n"
				+ "                    \"parameter_names\": \"quantity,unitPrice,rateForTaxType1,rateForTaxType4\",\r\n"
				+ "                    \"parameter_types\": \"BigDecimal,BigDecimal,double,double\",\r\n"
				+ "                    \"valid_from\" : \"2000-01-01\",\r\n"
				+ "                    \"valid_to\" : \"2099-01-01\"\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        }  \r\n" + "\r\n" + "    ]\r\n" + "}";
		
		jsonRuleStringDefault = "{\r\n" + 
				"   \"country\":\"default\",\r\n" + 
				"   \"rules\":[\r\n" + 
				"      {\r\n" + 
				"         \"expression\":\"isTaxEventNonTaxable == true && ( taxTypeCode.equalsIgnoreCase(\\\"1\\\") || taxTypeCode.equalsIgnoreCase(\\\"2\\\") || taxTypeCode.equalsIgnoreCase(\\\"3\\\") || taxTypeCode.equalsIgnoreCase(\\\"4\\\"))\",\r\n" + 
				"         \"parameter_names\":\"isTaxEventNonTaxable,taxTypeCode\",\r\n" + 
				"         \"parameter_types\":\"boolean,String\",\r\n" + 
				"         \"results\":[\r\n" + 
				"            {\r\n" + 
				"               \"ruleName\":\"NonTaxableScenario\",\r\n" + 
				"               \"baseAmount\":\"quantity.multiply(unitPrice)\",\r\n" + 
				"               \"parameter_names\":\"quantity,unitPrice\",\r\n" + 
				"               \"parameter_types\":\"BigDecimal,BigDecimal\",\r\n" + 
				"               \"valid_from\":\"2000-01-01\",\r\n" + 
				"               \"valid_to\":\"9999-12-31\"\r\n" + 
				"            }\r\n" + 
				"         ]\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"         \"expression\":\"(( isTaxType1Relevant == true && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"1\\\")) || ( isTaxType1Relevant == false && isTaxType2Relevant == true && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"2\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == true && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"3\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == true && taxTypeCode.equalsIgnoreCase(\\\"4\\\"))) && ( amountTypeCode.equalsIgnoreCase(\\\"Net\\\") || ( isReverseChargeRelevant == true && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")) ) \",\r\n" + 
				"         \"parameter_names\":\"isTaxType1Relevant,isTaxType2Relevant,isTaxType3Relevant,isTaxType4Relevant,taxTypeCode,amountTypeCode,isReverseChargeRelevant\",\r\n" + 
				"         \"parameter_types\":\"boolean,boolean,boolean,boolean,String,String,boolean\",\r\n" + 
				"         \"results\":[\r\n" + 
				"            {\r\n" + 
				"               \"ruleName\":\"SingleTaxType01\",\r\n" + 
				"               \"baseAmount\":\"quantity.multiply(unitPrice)\",\r\n" + 
				"               \"parameter_names\":\"quantity,unitPrice\",\r\n" + 
				"               \"parameter_types\":\"BigDecimal,BigDecimal\",\r\n" + 
				"               \"valid_from\":\"2000-01-01\",\r\n" + 
				"               \"valid_to\":\"9999-12-31\"\r\n" + 
				"            }\r\n" + 
				"         ]\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"         \"expression\":\"(( isTaxType1Relevant == true && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"1\\\")) || ( isTaxType1Relevant == false && isTaxType2Relevant == true && isTaxType3Relevant == false && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"2\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == true && isTaxType4Relevant == false && taxTypeCode.equalsIgnoreCase(\\\"3\\\")) ||( isTaxType1Relevant == false && isTaxType2Relevant == false && isTaxType3Relevant == false && isTaxType4Relevant == true && taxTypeCode.equalsIgnoreCase(\\\"4\\\"))) && ( isReverseChargeRelevant == false && amountTypeCode.equalsIgnoreCase(\\\"Gross\\\")) \",\r\n" + 
				"         \"parameter_names\":\"isTaxType1Relevant,isTaxType2Relevant,isTaxType3Relevant,isTaxType4Relevant,taxTypeCode,amountTypeCode,isReverseChargeRelevant\",\r\n" + 
				"         \"parameter_types\":\"boolean,boolean,boolean,boolean,String,String,boolean\",\r\n" + 
				"         \"results\":[\r\n" + 
				"            {\r\n" + 
				"               \"ruleName\":\"SingleTaxType02\",\r\n" + 
				"               \"baseAmount\":\"quantity.multiply(unitPrice).divide(java.math.BigDecimal.valueOf(1 + (taxRate / 100)), 15, java.math.RoundingMode.HALF_UP)\",\r\n" + 
				"               \"parameter_names\":\"quantity,unitPrice,taxRate\",\r\n" + 
				"               \"parameter_types\":\"BigDecimal,BigDecimal,double\",\r\n" + 
				"               \"valid_from\":\"2000-01-01\",\r\n" + 
				"               \"valid_to\":\"9999-12-31\"\r\n" + 
				"            }\r\n" + 
				"         ]\r\n" + 
				"      }\r\n" + 
				"   ]\r\n" + 
				"}";
		
	}

	@Test 
	public void test_calculateTax() {
		Mockito.when(mockCacheTaxCalculationLookupService.get(Mockito.any())).thenReturn(jsonRuleString);
		Mockito.when(supportedCountries.contains("CA")).thenReturn(true);
		
		List<TaxCalculationOutputDTO> actual = ruleEngine.calculateTax(taxCalculationInputDTOlist, "123");

		assertEquals("SingleTaxType02", actual.get(0).getTaxes().getRuleId());
		assertEquals(new BigDecimal("95.238095238095238"), actual.get(0).getTaxes().getTaxBaseAmount());
		assertNull(actual.get(0).getTaxes().getDecimalPlaces());
		assertNull(actual.get(0).getTaxes().getRoundingMethod());
		assertNull(actual.get(0).getTaxes().getFactor());
	}
	
	@Test 
	public void test_calculateTax_withCacheExpression() throws ClassNotFoundException, CompileException {
		
		Set<ExpressionCacheValue> expressionCacheValueSet = new HashSet<>();
		ExpressionCacheValue expressionCacheValue = new ExpressionCacheValue();
		ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
		expressionCacheValue.setExpressionEvaluator(new ExpressionEvaluator());
		expressionEvaluator.setExpressionType(BigDecimal.class);
		String[] parameterNamesArray = ExpressionHelper.explode(("quantity,unitPrice"));
		Class<?>[] parameterTypesArray = ExpressionHelper.stringToTypes("BigDecimal,BigDecimal");
		expressionEvaluator.setParameters(parameterNamesArray, parameterTypesArray);
		expressionEvaluator.cook("quantity.multiply(unitPrice)");
		expressionCacheValue.setExpressionEvaluator(expressionEvaluator);
		expressionCacheValue.setParameterTypes(parameterTypesArray);
		expressionCacheValue.setParameterNames(parameterNamesArray);
		expressionCacheValue.setRuleName("SingleTaxType02");
		expressionCacheValue.setValidFrom(DateUtility.toDateFromString("2001-01-01").getTime());
		expressionCacheValue.setValidTo(DateUtility.toDateFromString("9999-12-31").getTime());
		
		expressionCacheValueSet.add(expressionCacheValue);
		Mockito.lenient().when(mockCacheTaxCalculationLookupService.get(Mockito.any())).thenReturn(jsonRuleString);
		Mockito.lenient().when(supportedCountries.contains("CA")).thenReturn(true);
		Mockito.when(expressionMap.get(any(ExpressionCacheKey.class))).thenReturn(expressionCacheValueSet);
		
		List<TaxCalculationOutputDTO> actual = ruleEngine.calculateTax(taxCalculationInputDTOlist, "123");

		assertEquals("SingleTaxType02", actual.get(0).getTaxes().getRuleId());
		assertEquals(new BigDecimal("100"), actual.get(0).getTaxes().getTaxBaseAmount());
		assertNull(actual.get(0).getTaxes().getDecimalPlaces());
		assertNull(actual.get(0).getTaxes().getRoundingMethod());
		assertNull(actual.get(0).getTaxes().getFactor());
	}
	
	@Test
	public void test_calculateTax_withOutCacheExpression() throws ClassNotFoundException, CompileException {
		
		Set<ExpressionCacheValue> expressionCacheValueSet = new HashSet<>();
		ExpressionCacheValue expressionCacheValue = new ExpressionCacheValue();
		ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
		expressionCacheValue.setExpressionEvaluator(new ExpressionEvaluator());
		expressionEvaluator.setExpressionType(BigDecimal.class);
		String[] parameterNamesArray = ExpressionHelper.explode(("quantity,unitPrice"));
		Class<?>[] parameterTypesArray = ExpressionHelper.stringToTypes("BigDecimal,BigDecimal");
		expressionEvaluator.setParameters(parameterNamesArray, parameterTypesArray);
		expressionEvaluator.cook("quantity.multiply(unitPrice)");
		expressionCacheValue.setExpressionEvaluator(expressionEvaluator);
		expressionCacheValue.setParameterTypes(parameterTypesArray);
		expressionCacheValue.setParameterNames(parameterNamesArray);
		expressionCacheValue.setRuleName("SingleTaxType02");
		expressionCacheValue.setValidFrom(DateUtility.toDateFromString("2001-01-01").getTime());
		expressionCacheValue.setValidTo(DateUtility.toDateFromString("2001-12-31").getTime());
		
		expressionCacheValueSet.add(expressionCacheValue);
	
		Mockito.lenient().when(mockCacheTaxCalculationLookupService.get(Mockito.any())).thenReturn(jsonRuleString);
		Mockito.lenient().when(supportedCountries.contains("CA")).thenReturn(true);
		Mockito.when(expressionMap.get(any(ExpressionCacheKey.class))).thenReturn(expressionCacheValueSet);	
		List<TaxCalculationOutputDTO> actual = ruleEngine.calculateTax(taxCalculationInputDTOlist, "123");
		assertEquals("SingleTaxType02", actual.get(0).getTaxes().getRuleId());
		assertEquals(new BigDecimal("95.238095238095238"), actual.get(0).getTaxes().getTaxBaseAmount());
		assertNull(actual.get(0).getTaxes().getDecimalPlaces());
		assertNull(actual.get(0).getTaxes().getRoundingMethod());
		assertNull(actual.get(0).getTaxes().getFactor());
	}
	
	@Test(expected = ApplicationException.class)
	public void test_calculateTax_withException() throws ClassNotFoundException, CompileException, NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
		
		Set<ExpressionCacheValue> expressionCacheValueSet = new HashSet<>();
		ExpressionCacheValue expressionCacheValue = new ExpressionCacheValue();
		ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
		expressionCacheValue.setExpressionEvaluator(new ExpressionEvaluator());
		expressionEvaluator.setExpressionType(BigDecimal.class);
		String[] parameterNamesArray = ExpressionHelper.explode(("quantity,unitPrice"));
		Class<?>[] parameterTypesArray = ExpressionHelper.stringToTypes("BigDecimal,BigDecimal");
		expressionEvaluator.setParameters(parameterNamesArray, parameterTypesArray);
		expressionEvaluator.cook("quantity.multiply(unitPrice)");
		expressionCacheValue.setExpressionEvaluator(expressionEvaluator);
		expressionCacheValue.setParameterTypes(parameterTypesArray);
		expressionCacheValue.setParameterNames(parameterNamesArray);
		expressionCacheValue.setRuleName("SingleTaxType02");
		expressionCacheValue.setValidFrom(DateUtility.toDateFromString("2001-01-01").getTime());
		expressionCacheValue.setValidTo(DateUtility.toDateFromString("9999-12-31").getTime());
		
		expressionCacheValueSet.add(expressionCacheValue);
		PowerMockito.mockStatic(ExpressionHelper.class);
		PowerMockito.doThrow(new NoSuchMethodException()).when(ExpressionHelper.class);
		ExpressionHelper.createObject(Mockito.any(), Mockito.any());
		Mockito.lenient().when(mockCacheTaxCalculationLookupService.get(Mockito.any())).thenReturn(jsonRuleString);
		Mockito.lenient().when(supportedCountries.contains("CA")).thenReturn(true);
		Mockito.when(expressionMap.get(any(ExpressionCacheKey.class))).thenReturn(expressionCacheValueSet);	
		List<TaxCalculationOutputDTO> actual = ruleEngine.calculateTax(taxCalculationInputDTOlist, "123");
	
	}
	
	

	@Test 
	public void test_calculateTax_DefaultRule() {
		Mockito.when(mockCacheTaxCalculationLookupService.get(Mockito.any())).thenReturn(null,jsonRuleStringDefault);
		Mockito.when(supportedCountries.contains("SE")).thenReturn(true);
		
		TaxLineBRS taxLineBRS2 = taxLineBRS;
		TaxCalculationInputDTO taxCalculationInputDTO2 = taxCalculationInputDTO;
		List<TaxCalculationInputDTO> taxCalculationInputDTOlist2 = new ArrayList<>();
		taxLineBRS2.setCountryCode(Item.CountryRegionCode.SE);
		taxCalculationInputDTO2.setTaxBaseInput(taxLineBRS2);
		taxCalculationInputDTOlist2.add(taxCalculationInputDTO2);
		
		List<TaxCalculationOutputDTO> actual = ruleEngine.calculateTax(taxCalculationInputDTOlist2, "123");

		assertEquals("SingleTaxType02", actual.get(0).getTaxes().getRuleId());
		assertEquals(new BigDecimal("95.238095238095238"), actual.get(0).getTaxes().getTaxBaseAmount());
		assertNull(actual.get(0).getTaxes().getDecimalPlaces());
		assertNull(actual.get(0).getTaxes().getRoundingMethod());
		assertNull(actual.get(0).getTaxes().getFactor());
	} 
}