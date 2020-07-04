package com.sap.slh.tax.calculation.service;

import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.AMOUNT_TYPE_CODE;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.COUNTRY_REGION_CODE;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.ID;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_REVERSE_CHARGE_RELEVEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_EVENT_NON_TAXABLE;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE1_RELEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE1_REVERSE_CHARGE_RELEVEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE2_RELEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE2_REVERSE_CHARGE_RELEVEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE3_RELEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE3_REVERSE_CHARGE_RELEVEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE4_RELEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.IS_TAX_TYPE4_REVERSE_CHARGE_RELEVEVANT;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.QUANTITY;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.RATE_FOR_TAX_TYPE1;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.RATE_FOR_TAX_TYPE2;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.RATE_FOR_TAX_TYPE3;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.RATE_FOR_TAX_TYPE4;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.TAX_RATE;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.TAX_TYPE_CODE;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.UNIT_PRICE;
import static com.sap.slh.tax.calculation.engine.JsonRulesConstants.DEFAULT_COUNTRY_KEY;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sap.slh.tax.calculation.cache.dto.ExpressionCacheKey;
import com.sap.slh.tax.calculation.cache.dto.ExpressionCacheValue;
import com.sap.slh.tax.calculation.cache.dto.JsonRuleLookupKey;
import com.sap.slh.tax.calculation.cache.lookup.CacheTaxCalculationLookupService;
import com.sap.slh.tax.calculation.dto.ReverseCharge;
import com.sap.slh.tax.calculation.dto.TaxCalculationInputDTO;
import com.sap.slh.tax.calculation.dto.TaxCalculationOutputDTO;
import com.sap.slh.tax.calculation.dto.TaxDetails;
import com.sap.slh.tax.calculation.dto.TaxLineBRS;
import com.sap.slh.tax.calculation.dto.TaxRates;
import com.sap.slh.tax.calculation.dto.Taxes;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.Item;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.calculation.utility.DateUtility;
import com.sap.slh.tax.calculation.utility.ExpressionHelper;

@Service("ruleEngineTaxCalculationService")
public class RuleEngineTaxCalculationService extends TaxCalculationServiceChain {

	private static final Logger log = LoggerFactory.getLogger(RuleEngineTaxCalculationService.class);

	@Autowired
	private CacheTaxCalculationLookupService cacheTaxCalculationLookupService;

	@Value("${tax.calculation.service.supported.countries}")
	private String supportedCountry;

	private Map<ExpressionCacheKey, Set<ExpressionCacheValue>> expressionMap = new HashMap<>();

	private List<String> supportedCountries;

	@PostConstruct
	public void initSupportedCountries() {
		supportedCountries = Arrays.asList(supportedCountry.split(","));

	}

	@Override
	public List<TaxCalculationOutputDTO> calculateTax(List<TaxCalculationInputDTO> taxCalculationInput,
			String tenantId) {
		log.debug("Request for Tax Calculation : {}", taxCalculationInput);
		List<TaxCalculationOutputDTO> calcResults = new ArrayList<>();
		List<TaxCalculationInputDTO> noCacheInput = new ArrayList<>();
		if (!CollectionUtils.isEmpty(taxCalculationInput)) {
			Map<Item.CountryRegionCode, List<TaxCalculationInputDTO>> taxCalcRequestPerCountry = taxCalculationInput
					.stream().collect(
							Collectors.groupingBy(taxCalcRequest -> taxCalcRequest.getTaxBaseInput().getCountryCode()));
			Date requestDate = DateUtility.toDateFromString(taxCalculationInput.get(0).getTaxBaseInput().getDate());
			taxCalcRequestPerCountry.forEach((countryRegionCode, taxCalculationRequestList) -> {
				taxCalculationRequestList.forEach(taxCalculationRequest -> {
					Map<String, String> ruleMap = createRuleMap(taxCalculationRequest);
					ExpressionCacheKey key = createExpressionCacheKey(ruleMap, tenantId);
					Set<ExpressionCacheValue> expressionCacheValueSet = expressionMap.get(key);
					if (!CollectionUtils.isEmpty(expressionCacheValueSet)) {
						ExpressionCacheValue value = expressionCacheValueSet.stream()
								.filter(expressionCacheValue -> expressionCacheValue.getValidFrom() <= requestDate
										.getTime() && requestDate.getTime() <= expressionCacheValue.getValidTo())
								.findAny().orElse(null);
						try {
							if (value != null) {
								BigDecimal baseAmount = calculateBaseAmount(value, ruleMap);
								TaxCalculationOutputDTO taxCalculationOutputDTO = from(baseAmount, value.getRuleName(),
										ruleMap);
								calcResults.add(taxCalculationOutputDTO);
							} else {
								noCacheInput.add(taxCalculationRequest);
							}
						} catch (NoSuchMethodException | InstantiationException | InvocationTargetException
								| IllegalAccessException e) {
							final String errorMsg = "Error occured while evaluation of baseAmount for country : {} and request : {} : countryRegionCode, ruleMap.toString()";
							throw new ApplicationException(errorMsg, e,
									TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR,
									TaxCalculationStatus.FAILURE);
						}
					} else {
						noCacheInput.add(taxCalculationRequest);
					}
				});

			});
			if (!CollectionUtils.isEmpty(noCacheInput)) {
				List<TaxCalculationOutputDTO> calculationResults = calculateTaxRuleEngine(noCacheInput, tenantId);
				calcResults.addAll(calculationResults);
			}

		}
		return calcResults;

	}

	private List<TaxCalculationOutputDTO> calculateTaxRuleEngine(List<TaxCalculationInputDTO> taxCalculationInput,
			String tenantId) {
		List<TaxCalculationOutputDTO> calcResults = new ArrayList<>();
		Map<Item.CountryRegionCode, List<TaxCalculationInputDTO>> taxCalcRequestPerCountry = taxCalculationInput
				.stream()
				.collect(Collectors.groupingBy(taxCalcRequest -> taxCalcRequest.getTaxBaseInput().getCountryCode()));
		Date requestDate = DateUtility.toDateFromString(taxCalculationInput.get(0).getTaxBaseInput().getDate());
		taxCalcRequestPerCountry.forEach((countryRegionCode, taxCalculationRequestList) -> {
			if (supportedCountries.contains(countryRegionCode.value())) {
				JsonRuleLookupKey lookupKey = new JsonRuleLookupKey();
				lookupKey.setCountryRegionCode(countryRegionCode.value());
				lookupKey.setTenantId(tenantId);
				String jsonRuleString = cacheTaxCalculationLookupService.get(lookupKey);
				if (StringUtils.isEmpty(jsonRuleString)) {
					lookupKey.setCountryRegionCode(DEFAULT_COUNTRY_KEY);
					jsonRuleString = cacheTaxCalculationLookupService.get(lookupKey);
				}
				String jsonRule = jsonRuleString;
				taxCalculationRequestList.forEach(taxCalculationRequest -> {
					Map<String, String> ruleMap = createRuleMap(taxCalculationRequest);
					if (!StringUtils.isEmpty(jsonRule)) {
						try {
							JSONArray results = fetchMatchedResults(jsonRule, ruleMap);
							if (results != null) {
								JSONObject result = fetchValidResult(results, requestDate);
								if (result != null) {
									ExpressionCacheValue expressionCacheValue = createExpressionCacheValue(result);
									ExpressionCacheKey key = createExpressionCacheKey(ruleMap, tenantId);
									Set<ExpressionCacheValue> expressionCacheValueSet = expressionMap.get(key);
									if (CollectionUtils.isEmpty(expressionCacheValueSet)) {
										expressionCacheValueSet = new HashSet<>();
									}
									expressionCacheValueSet.add(expressionCacheValue);
									expressionMap.put(key, expressionCacheValueSet);
									BigDecimal baseAmount = calculateBaseAmount(expressionCacheValue, ruleMap);
									TaxCalculationOutputDTO taxCalculationOutputDTO = from(baseAmount,
											expressionCacheValue.getRuleName(), ruleMap);
									calcResults.add(taxCalculationOutputDTO);
								}

							}
						} catch (NoSuchMethodException | InstantiationException | InvocationTargetException
								| IllegalAccessException | CompileException | ClassNotFoundException e) {
							final String errorMsg = "Error occured while evaluation of baseAmount for country : {} and request : {} : countryRegionCode, ruleMap.toString()";
							throw new ApplicationException(errorMsg, e,
									TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR,
									TaxCalculationStatus.FAILURE);
						}
					}

				});
			}
		});
		return calcResults;
	}

	private JSONObject fetchValidResult(JSONArray results, Date requestDate) {
		for (int i = 0; i < results.length(); i++) {
			Date validFrom = DateUtility.toDateFromString(results.getJSONObject(i).getString("valid_from"));
			Date validTo = DateUtility.toDateFromString(results.getJSONObject(i).getString("valid_to"));
			if (!(requestDate.before(validFrom) || requestDate.after(validTo))) {
				return results.getJSONObject(i);
			}
		}
		return null;
	}

	private JSONArray fetchMatchedResults(String jsonRuleString, Map<String, String> ruleMap)
			throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException,
			CompileException, ClassNotFoundException {
		JSONObject obj = new JSONObject(jsonRuleString);
		JSONArray array = obj.getJSONArray("rules");
		for (int i = 0; i < array.length(); i++) {
			String expression = array.getJSONObject(i).getString("expression");
			String parameterNames = array.getJSONObject(i).getString("parameter_names");
			String parameterTypes = array.getJSONObject(i).getString("parameter_types");
			boolean isRuleMatched = evaluateRule(ruleMap, expression, parameterNames, parameterTypes);
			if (isRuleMatched) {
				return array.getJSONObject(i).getJSONArray("results");
			}
		}
		return null;
	}

	private boolean evaluateRule(Map<String, String> properties, String expression, String parameterNames,
			String parameterTypes) throws NoSuchMethodException, InstantiationException, InvocationTargetException,
			IllegalAccessException, CompileException, ClassNotFoundException {
		ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
		expressionEvaluator.setExpressionType(boolean.class);
		String[] parameterNamesArray = ExpressionHelper.explode(parameterNames);
		Class<?>[] parameterTypesArray = ExpressionHelper.stringToTypes(parameterTypes);
		Object[] arguments = new Object[parameterNamesArray.length];
		for (int j = 0; j < parameterNamesArray.length; ++j) {
			arguments[j] = ExpressionHelper.createObject(parameterTypesArray[j],
					properties.get(parameterNamesArray[j]));
		}
		expressionEvaluator.setParameters(parameterNamesArray, parameterTypesArray);
		expressionEvaluator.cook(expression);
		return (boolean) expressionEvaluator.evaluate(arguments);

	}

	private ExpressionCacheValue createExpressionCacheValue(JSONObject result)
			throws CompileException, ClassNotFoundException {
		ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
		ExpressionCacheValue expressionCacheValue = new ExpressionCacheValue();
		expressionEvaluator.setExpressionType(BigDecimal.class);
		String[] parameterNamesArray = ExpressionHelper.explode(result.getString("parameter_names"));
		Class<?>[] parameterTypesArray = ExpressionHelper.stringToTypes(result.getString("parameter_types"));
		expressionEvaluator.setParameters(parameterNamesArray, parameterTypesArray);
		expressionEvaluator.cook(result.getString("baseAmount"));
		expressionCacheValue.setExpressionEvaluator(expressionEvaluator);
		expressionCacheValue.setParameterTypes(parameterTypesArray);
		expressionCacheValue.setParameterNames(parameterNamesArray);
		expressionCacheValue.setRuleName(result.getString("ruleName"));
		expressionCacheValue.setValidFrom(DateUtility.toDateFromString(result.getString("valid_from")).getTime());
		expressionCacheValue.setValidTo(DateUtility.toDateFromString(result.getString("valid_to")).getTime());
		return expressionCacheValue;

	}

	private Map<String, String> createRuleMap(TaxCalculationInputDTO calculationInputDTO) {
		Map<String, String> inputMap = new HashMap<>();
		TaxLineBRS taxLinesBRS = calculationInputDTO.getTaxBaseInput();
		ReverseCharge reverseCharge = calculationInputDTO.getReverseCharge();
		TaxDetails taxDetails = calculationInputDTO.getTaxDetails();
		TaxRates taxRates = calculationInputDTO.getTaxRates();
		inputMap.put(COUNTRY_REGION_CODE, taxLinesBRS.getCountryCode().value());
		inputMap.put(AMOUNT_TYPE_CODE, taxLinesBRS.getAmountTypeCode().value());
		inputMap.put(IS_REVERSE_CHARGE_RELEVEVANT, taxLinesBRS.getIsReverseChargeRelevant().toLowerCase());
		inputMap.put(ID, taxLinesBRS.getId());
		inputMap.put(IS_TAX_EVENT_NON_TAXABLE, taxLinesBRS.getIsTaxEventNonTaxable().toLowerCase());
		inputMap.put(QUANTITY, taxLinesBRS.getQuantity().toString());
		if (!StringUtils.isEmpty(taxLinesBRS.getTaxRate())) {
			inputMap.put(TAX_RATE, taxLinesBRS.getTaxRate().toString());
		}
		inputMap.put(TAX_TYPE_CODE, taxLinesBRS.getTaxTypeCode());
		inputMap.put(UNIT_PRICE, taxLinesBRS.getUnitPrice().toString());
		inputMap.put(IS_TAX_TYPE1_REVERSE_CHARGE_RELEVEVANT, reverseCharge.getIsTaxType1Relevant());
		inputMap.put(IS_TAX_TYPE2_REVERSE_CHARGE_RELEVEVANT, reverseCharge.getIsTaxType2Relevant());
		inputMap.put(IS_TAX_TYPE3_REVERSE_CHARGE_RELEVEVANT, reverseCharge.getIsTaxType3Relevant());
		inputMap.put(IS_TAX_TYPE4_REVERSE_CHARGE_RELEVEVANT, reverseCharge.getIsTaxType4Relevant());
		inputMap.put(IS_TAX_TYPE1_RELEVANT, taxDetails.getIsTaxType1Relevant());
		inputMap.put(IS_TAX_TYPE2_RELEVANT, taxDetails.getIsTaxType2Relevant());
		inputMap.put(IS_TAX_TYPE3_RELEVANT, taxDetails.getIsTaxType3Relevant());
		inputMap.put(IS_TAX_TYPE4_RELEVANT, taxDetails.getIsTaxType4Relevant());
		Double rateForTaxType1 = taxRates.getRateForTaxType1();
		if (rateForTaxType1 != null) {
			inputMap.put(RATE_FOR_TAX_TYPE1, taxRates.getRateForTaxType1().toString());
		}
		Double rateForTaxType2 = taxRates.getRateForTaxType2();
		if (rateForTaxType2 != null) {
			inputMap.put(RATE_FOR_TAX_TYPE2, taxRates.getRateForTaxType2().toString());
		}
		Double rateForTaxType3 = taxRates.getRateForTaxType3();
		if (rateForTaxType3 != null) {
			inputMap.put(RATE_FOR_TAX_TYPE3, taxRates.getRateForTaxType3().toString());
		}
		Double rateForTaxType4 = taxRates.getRateForTaxType4();
		if (rateForTaxType4 != null) {
			inputMap.put(RATE_FOR_TAX_TYPE4, taxRates.getRateForTaxType4().toString());
		}
		return inputMap;

	}

	private ExpressionCacheKey createExpressionCacheKey(Map<String, String> map, String tenantId) {
		ExpressionCacheKey cacheKey = new ExpressionCacheKey();
		cacheKey.setTenantId(tenantId);
		cacheKey.setCountry(map.get(COUNTRY_REGION_CODE));
		cacheKey.setAmountTypeCode(map.get(AMOUNT_TYPE_CODE));
		cacheKey.setIsReverseChargeRelevant(map.get(IS_REVERSE_CHARGE_RELEVEVANT));
		cacheKey.setIsTaxEventNonTaxable(map.get(IS_TAX_EVENT_NON_TAXABLE));
		cacheKey.setIsTaxType1Relevant(map.get(IS_TAX_TYPE1_RELEVANT));
		cacheKey.setIsTaxType1ReverseChargeRelevant(map.get(IS_TAX_TYPE1_REVERSE_CHARGE_RELEVEVANT));
		cacheKey.setIsTaxType2Relevant(map.get(IS_TAX_TYPE2_RELEVANT));
		cacheKey.setIsTaxType2ReverseChargeRelevant(map.get(IS_TAX_TYPE2_REVERSE_CHARGE_RELEVEVANT));
		cacheKey.setIsTaxType3Relevant(map.get(IS_TAX_TYPE3_RELEVANT));
		cacheKey.setIsTaxType3ReverseChargeRelevant(map.get(IS_TAX_TYPE3_REVERSE_CHARGE_RELEVEVANT));
		cacheKey.setIsTaxType4Relevant(map.get(IS_TAX_TYPE4_RELEVANT));
		cacheKey.setIsTaxType4ReverseChargeRelevant(map.get(IS_TAX_TYPE4_REVERSE_CHARGE_RELEVEVANT));
		cacheKey.setTaxTypeCode(map.get(TAX_TYPE_CODE));
		return cacheKey;
	}

	private TaxCalculationOutputDTO from(BigDecimal baseAmount, String ruleName, Map<String, String> ruleMap) {
		log.error("rule : {} matched for taxLine id : {}", ruleName, ruleMap.get(ID));
		TaxCalculationOutputDTO taxCalculationOutputDTO = new TaxCalculationOutputDTO();
		Taxes taxes = new Taxes();
		taxes.setId(ruleMap.get(ID));
		taxes.setTaxBaseAmount(baseAmount);
		taxes.setRuleId(ruleName);
		taxCalculationOutputDTO.setTaxes(taxes);
		return taxCalculationOutputDTO;
	}

	private BigDecimal calculateBaseAmount(ExpressionCacheValue value, Map<String, String> ruleMap)
			throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
		ExpressionEvaluator ee = value.getExpressionEvaluator();
		Class<?>[] parameterTypes = value.getParameterTypes();
		String[] parameterNames = value.getParameterNames();
		Object[] arguments = new Object[parameterNames.length];
		for (int j = 0; j < parameterNames.length; ++j) {
			arguments[j] = ExpressionHelper.createObject(parameterTypes[j], ruleMap.get(parameterNames[j]));
		}
		return (BigDecimal) ee.evaluate(arguments);
	}
}