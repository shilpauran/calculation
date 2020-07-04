package com.sap.slh.tax.calculation.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.slh.tax.calculation.config.ApplicationConfiguration;
import com.sap.slh.tax.calculation.exception.InvalidRequestException;
import com.sap.slh.tax.calculation.model.TaxCalculationValidationResult;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.validator.StructuralValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TaxCalculationRequestValidation.class, ApplicationConfiguration.class,
		StructuralValidator.class })
public class TaxCalculationRequestValidationTest {

	@Autowired
	private StructuralValidator structuralValidator;

	@Mock
	SimpleDateFormat simpleDateFormat;
	Logger logger = LoggerFactory.getLogger(TaxCalculationRequestValidationTest.class);

	@InjectMocks
	@Autowired(required = true)
	TaxCalculationRequestValidation requestValidation;

	@Test
	public void validateTestSuccess() throws IOException {
		String documentString = "{\r\n" + "      \"id\":\"SO_1\",\r\n" + "      \"date\":\"2019-04-11\",\r\n"
				+ "      \"amountTypeCode\":\"NET\",\r\n" + "      \"currencyCode\":\"EUR\",\r\n"
				+ "      \"items\":[\r\n" + "         {\r\n" + "            \"id\":\"I1\",\r\n"
				+ "            \"quantity\":\"100\",\r\n" + "            \"unitPrice\":\"10\",\r\n"
				+ "            \"countryRegionCode\":\"GB\",\r\n" + "            \"taxEventCode\":\"10\",\r\n"
				+ "            \"isTaxEventNonTaxable\":false,\r\n" + "            \"taxes\":[\r\n"
				+ "               {\r\n" + "                  \"id\":\"1\",\r\n"
				+ "                  \"taxTypeCode\":\"1\",\r\n" + "                  \"taxRate\":\"20\",\r\n"
				+ "                  \"dueCategoryCode\":\"PAYABLE\",\r\n"
				+ "                  \"nonDeductibleTaxRate\":\"100\",\r\n"
				+ "                  \"isReverseChargeRelevant\": true\r\n" + "               }\r\n"
				+ "            ]\r\n" + "         }\r\n" + "      ]\r\n" + "   }";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		assertTrue(structuralValidator.isValid(document, Locale.ENGLISH));

		TaxCalculationValidationResult result = requestValidation.validate(document, Locale.ENGLISH);
		assertNotNull(result);
		assertTrue(result.isValid());
	}

	@Test(expected = InvalidRequestException.class)
	public void documentLevelTest() throws IOException {
		String documentString = "{\r\n" + "    \"items\": [\r\n" + "       \r\n" + "    ]\r\n" + "}";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		structuralValidator.isValid(document, Locale.ENGLISH);
	}

	@Test(expected = InvalidRequestException.class)
	public void itemLevelTest() throws IOException {
		String documentString = "{\r\n" + "    \"id\": \"SO_1\",\r\n" + "    \"date\": \"2019-08-27\",\r\n"
				+ "    \"amountTypeCode\": \"NET\",\r\n" + "    \"currencyCode\": \"CAD\",\r\n" + "    \"items\": [\r\n"
				+ "        {\r\n" + "          \r\n" + "            \"isTaxEventNonTaxable\": false,\r\n"
				+ "            \"taxes\": [\r\n" + "               \r\n" + "            ]\r\n" + "        },\r\n"
				+ "         {\r\n" + "            \"id\": \"Item1\",\r\n" + "            \"quantity\": 10,\r\n"
				+ "            \"unitPrice\": 1000000,\r\n" + "            \"countryRegionCode\": \"CA\",\r\n"
				+ "            \"taxEventCode\": \"11\",\r\n" + "            \"isTaxEventNonTaxable\": false,\r\n"
				+ "            \"taxes\": [\r\n" + "                {\r\n" + "                 \r\n"
				+ "                    \"taxRate\": 8,\r\n" + "                   \r\n"
				+ "                    \"nonDeductibleTaxRate\": 80,\r\n"
				+ "                    \"isReverseChargeRelevant\": false\r\n" + "                },\r\n"
				+ "                {\r\n" + "                    \"id\": \"2\",\r\n"
				+ "                    \"taxTypeCode\": \"2\",\r\n" + "                    \"taxRate\": 10,\r\n"
				+ "                    \"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "                    \"nonDeductibleTaxRate\": 80,\r\n"
				+ "                    \"isReverseChargeRelevant\": false\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        }\r\n" + "    ]\r\n" + "}";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		structuralValidator.isValid(document, Locale.ENGLISH);
	}

	@Test
	public void invalidTaxRateTest() throws IOException {
		String documentString = "{\r\n" + "      \"id\":\"SO_1\",\r\n"
				+ "      \"date\":\"2012-10-01T09:45:00.000+02:00\",\r\n" + "      \"amountTypeCode\":\"NET\",\r\n"
				+ "      \"currencyCode\":\"EUR\",\r\n" + "      \"items\":[\r\n" + "         {\r\n"
				+ "            \"id\":\"I1\",\r\n" + "            \"quantity\":\"100\",\r\n"
				+ "            \"unitPrice\":\"10\",\r\n" + "            \"countryRegionCode\":\"GB\",\r\n"
				+ "            \"taxEventCode\":\"10\",\r\n" + "            \"isTaxEventNonTaxable\":false,\r\n"
				+ "            \"taxes\":[\r\n" + "               {\r\n" + "                  \"id\":\"1\",\r\n"
				+ "                  \"taxTypeCode\":\"1\",\r\n" + "                  \"taxRate\":\"-20\",\r\n"
				+ "                  \"dueCategoryCode\":\"PAYABLE\",\r\n"
				+ "                  \"nonDeductibleTaxRate\":\"100\",\r\n"
				+ "                  \"isReverseChargeRelevant\": true\r\n" + "               }\r\n"
				+ "            ]\r\n" + "         }\r\n" + "      ]\r\n" + "   }";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}
		assertTrue(structuralValidator.isValid(document, Locale.ENGLISH));
		TaxCalculationValidationResult result = requestValidation.validate(document, Locale.ENGLISH);
		assertNotNull(result);
		assertFalse(result.isValid());
	}

	@Test
	public void invalidDateTest() throws IOException, ParseException {
		String documentString = "{\r\n" + "      \"id\":\"SO_1\",\r\n"
				+ "      \"date\":\"2012-10-01T09:45:00.000+02:00\",\r\n" + "      \"amountTypeCode\":\"NET\",\r\n"
				+ "      \"currencyCode\":\"EUR\",\r\n" + "      \"items\":[\r\n" + "         {\r\n"
				+ "            \"id\":\"I1\",\r\n" + "            \"quantity\":\"100\",\r\n"
				+ "            \"unitPrice\":\"10\",\r\n" + "            \"countryRegionCode\":\"GB\",\r\n"
				+ "            \"taxEventCode\":\"10\",\r\n" + "            \"isTaxEventNonTaxable\":false,\r\n"
				+ "            \"taxes\":[\r\n" + "               {\r\n" + "                  \"id\":\"1\",\r\n"
				+ "                  \"taxTypeCode\":\"1\",\r\n" + "                  \"taxRate\":\"-20\",\r\n"
				+ "                  \"dueCategoryCode\":\"PAYABLE\",\r\n"
				+ "                  \"nonDeductibleTaxRate\":\"100\",\r\n"
				+ "                  \"isReverseChargeRelevant\": true\r\n" + "               }\r\n"
				+ "            ]\r\n" + "         }\r\n" + "      ]\r\n" + "   }";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}
		assertTrue(structuralValidator.isValid(document, Locale.ENGLISH));
		when(simpleDateFormat.parse(ArgumentMatchers.anyString())).thenThrow(ParseException.class);
		TaxCalculationValidationResult result = requestValidation.validate(document, Locale.ENGLISH);
		assertNotNull(result);
		assertFalse(result.isValid());
	}

	@Test
	public void validationExceptions() {
		String documentString = "{\r\n" + "    \r\n" + "    \"date\": \"2019-08-27\",\r\n"
				+ "    \"amountTypeCode\": \"GROSS\",\r\n" + "    \"currencyCode\": \"CAD\",\r\n"
				+ "    \"items\": [\r\n" + "        {\r\n" + "            \"id\": \"Item1\",\r\n"
				+ "            \"quantity\": 10,\r\n" + "            \"unitPrice\": 10,\r\n"
				+ "            \"countryRegionCode\": \"CA\",\r\n" + "            \"taxEventCode\": \"11\",\r\n"
				+ "            \"isTaxEventNonTaxable\": false,\r\n" + "            \"taxes\": [\r\n"
				+ "                {\r\n" + "                    \"id\": \"1\",\r\n"
				+ "                    \"taxTypeCode\": \"1\",\r\n" + "                    \"taxRate\": 8,\r\n"
				+ "                    \"dueCategoryCode\": \"RECEIVABLE\",\r\n"
				+ "                    \"nonDeductibleTaxRate\": 0,\r\n"
				+ "                    \"isReverseChargeRelevant\": false\r\n" + "                },\r\n"
				+ "                {\r\n" + "                    \"id\": \"2\",\r\n"
				+ "                    \"taxTypeCode\": \"3\",\r\n" + "                    \"taxRate\": 10,\r\n"
				+ "                    \"dueCategoryCode\": \"RECEIVABLE\",\r\n"
				+ "                    \"nonDeductibleTaxRate\": 0,\r\n"
				+ "                    \"isReverseChargeRelevant\": false\r\n" + "                }\r\n"
				+ "            ]\r\n" + "        }\r\n" + "    ]\r\n" + "}";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		TaxCalculationValidationResult result = requestValidation.validate(document, Locale.ENGLISH);
		assertNotNull(result);
		assertTrue(result.isValid());
	}

	@Test
	public void negativeCheckTest() throws IOException {
		String documentString = "{\r\n" + "      \"id\":\"SO_1\",\r\n" + "      \"date\":\"2019-04-11\",\r\n"
				+ "      \"amountTypeCode\":\"netty\",\r\n" + "      \"currencyCode\":\"EUR\",\r\n"
				+ "      \"items\":[\r\n" + "         {\r\n" + "            \"id\":\"I1\",\r\n"
				+ "            \"quantity\":\"100\",\r\n" + "            \"unitPrice\":\"10\",\r\n"
				+ "            \"countryRegionCode\":\"GB\",\r\n" + "            \"taxEventCode\":\"10\",\r\n"
				+ "            \"isTaxEventNonTaxable\":false,\r\n" + "            \"taxes\":[\r\n"
				+ "               {\r\n" + "                  \"id\":\"1\",\r\n"
				+ "                  \"taxTypeCode\":\"1\",\r\n" + "                  \"taxRate\":\"20\",\r\n"
				+ "                  \"dueCategoryCode\":\"PAYABLE\",\r\n"
				+ "                  \"nonDeductibleTaxRate\":\"100\",\r\n"
				+ "                  \"isReverseChargeRelevant\": true\r\n" + "               }\r\n"
				+ "            ]\r\n" + "         }\r\n" + "      ]\r\n" + "}";
		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			logger.info("Negative test case passed");

		}
		TaxCalculationValidationResult result = requestValidation.validate(document, Locale.ENGLISH);
		assertFalse(result.isValid());

	}

	@Test
	public void noDocumentsTest() throws IOException {
		String documentString = "{\r\n" + "   \r\n" + "}";
		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		TaxCalculationValidationResult result = requestValidation.validate(document, Locale.ENGLISH);
		assertFalse(result.isValid());

	}

	@Test
	public void businessCheckCoverageTest() throws IOException {
		String documentString = "{\r\n" + "		\"id\": \"SO_1\",\r\n" + "		\"date\": \"2019-04-11\",\r\n"
				+ "		\"amountTypeCode\": \"NET\",\r\n" + "		\"currencyCode\": \"EUR\",\r\n"
				+ "		\"items\": [{\r\n" + "				\"id\": \"       \",\r\n"
				+ "				\"quantity\": \"100\",\r\n" + "				\"unitPrice\": \"10\",\r\n"
				+ "				\"countryRegionCode\": \"GB\",\r\n" + "				\"taxEventCode\": \"       \",\r\n"
				+ "				\"isTaxEventNonTaxable\": false,\r\n" + "				\"taxes\": [{\r\n"
				+ "					\"id\": \"1\",\r\n" + "					\"taxTypeCode\": \"        \",\r\n"
				+ "					\r\n" + "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				},{\r\n"
				+ "					\"id\": \"1\",\r\n" + "					\"taxTypeCode\": \"1\",\r\n"
				+ "					\"taxRate\": \"0.0\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			}\r\n"
				+ "		]\r\n" + "	},\r\n" + "	{\r\n" + "		\"id\": \"           \",\r\n"
				+ "		\"date\": \"2019-04-11\",\r\n" + "		\"amountTypeCode\": \"NET\",\r\n"
				+ "		\"currencyCode\": \"EUR\",\r\n" + "		\"items\": [{\r\n"
				+ "				\"id\": \"       \",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"       \",\r\n"
				+ "				\"isTaxEventNonTaxable\": false,\r\n" + "				\"taxes\": [{\r\n"
				+ "					\"id\": \"1\",\r\n" + "					\"taxTypeCode\": \"1\",\r\n"
				+ "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			}\r\n"
				+ "		]\r\n" + "	}, {\r\n" + "		\"id\": \"doc1\",\r\n" + "		\"date\": \"2019-04-11\",\r\n"
				+ "		\"amountTypeCode\": \"NET\",\r\n" + "		\"currencyCode\": \"EUR\",\r\n"
				+ "		\"items\": [{\r\n" + "				\"id\": \"       \",\r\n"
				+ "				\"quantity\": \"100\",\r\n" + "				\"unitPrice\": \"10\",\r\n"
				+ "				\"countryRegionCode\": \"GB\",\r\n" + "				\"taxEventCode\": \"10\",\r\n"
				+ "				\"isTaxEventNonTaxable\": false,\r\n" + "				\"taxes\": [{\r\n"
				+ "					\"id\": \"1\",\r\n" + "					\"taxTypeCode\": \"1\",\r\n"
				+ "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			}\r\n"
				+ "		]\r\n" + "	}, {\r\n" + "		\"id\": \"doc1\",\r\n" + "		\"date\": \"2019-28-69\",\r\n"
				+ "		\"amountTypeCode\": \"NET\",\r\n" + "		\"currencyCode\": \"EUR\",\r\n"
				+ "		\"items\": [{\r\n" + "				\"id\": \"       \",\r\n"
				+ "				\"quantity\": \"100\",\r\n" + "				\"unitPrice\": \"10\",\r\n"
				+ "				\"countryRegionCode\": \"GB\",\r\n" + "				\"taxEventCode\": \"10\",\r\n"
				+ "				\"isTaxEventNonTaxable\": false,\r\n" + "				\"taxes\": [{\r\n"
				+ "					\"id\": \"1\",\r\n" + "					\"taxTypeCode\": \"1\",\r\n"
				+ "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			},\r\n"
				+ "			{\r\n" + "				\"id\": \"I1\",\r\n" + "				\"quantity\": \"100\",\r\n"
				+ "				\"unitPrice\": \"10\",\r\n" + "				\"countryRegionCode\": \"GB\",\r\n"
				+ "				\"taxEventCode\": \"10\",\r\n" + "				\"isTaxEventNonTaxable\": false,\r\n"
				+ "				\"taxes\": [{\r\n" + "					\"id\": \"1\",\r\n"
				+ "					\"taxTypeCode\": \"1\",\r\n" + "					\"taxRate\": \"20\",\r\n"
				+ "					\"dueCategoryCode\": \"PAYABLE\",\r\n"
				+ "					\"nonDeductibleTaxRate\": \"100\",\r\n"
				+ "					\"isReverseChargeRelevant\": true\r\n" + "				}]\r\n" + "			}\r\n"
				+ "		]\r\n" + "	}\r\n";
		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		TaxCalculationValidationResult result = requestValidation.validate(document, Locale.ENGLISH);

		assertNotNull(result);
		assertFalse(result.isValid());
	}

	@Test(expected = InvalidRequestException.class)
	public void itemsLevelTest() throws IOException {
		String documentString = "{\r\n" + "    \"\": [\r\n" + "       \r\n" + "    ]\r\n" + "}";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		structuralValidator.isValid(document, Locale.ENGLISH);
	}

	@Test(expected = InvalidRequestException.class)
	public void taxLineLevelTest() throws IOException {
		String documentString = "{\r\n    \"id\": \"SO_1\",\r\n    \"date\": \"2019-08-27\",\r\n    \"amountTypeCode\": \"GROSS\",\r\n    \"currencyCode\": \"CAD\",\r\n    \"items\": [\r\n        {\r\n            \"id\": \"Item1\",\r\n            \"quantity\": 10,\r\n            \"unitPrice\": 10,\r\n            \"countryRegionCode\": \"CA\",\r\n            \"taxEventCode\": \"11\",\r\n            \"isTaxEventNonTaxable\": false\r\n          \r\n        }\r\n    ]\r\n}";

		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		structuralValidator.isValid(document, Locale.ENGLISH);
	}
	
	@Test(expected = InvalidRequestException.class)
	public void taxLineEmpTest() throws IOException {
		String documentString = "{\r\n    \"id\": \"SO_1\",\r\n    \"date\": \"2019-08-27\",\r\n    \"amountTypeCode\": \"GROSS\",\r\n    \"currencyCode\": \"CAD\",\r\n    \"items\": [\r\n        {\r\n            \"id\": \"Item1\",\r\n            \"quantity\": 10,\r\n            \"unitPrice\": 10,\r\n            \"countryRegionCode\": \"CA\",\r\n            \"taxEventCode\": \"11\",\r\n            \"isTaxEventNonTaxable\": false,\r\n            \"taxes\": [\r\n                {\r\n                    \"id\": \"\",\r\n                    \"taxTypeCode\": \"\",\r\n                    \"taxRate\": 8,\r\n                    \"dueCategoryCode\": \"RECEIVABLE\",\r\n                    \"nonDeductibleTaxRate\": 0,\r\n                    \"isReverseChargeRelevant\": false\r\n                }\r\n            ]\r\n        }\r\n    ]\r\n}";
		ObjectMapper mapper = new ObjectMapper();
		TaxCalculationRequest document = null;
		try {
			document = mapper.readValue(documentString, new TypeReference<TaxCalculationRequest>() {
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		structuralValidator.isValid(document, Locale.ENGLISH);
	}
}