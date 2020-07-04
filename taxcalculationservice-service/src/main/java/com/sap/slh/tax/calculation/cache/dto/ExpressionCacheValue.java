package com.sap.slh.tax.calculation.cache.dto;

import org.codehaus.janino.ExpressionEvaluator;

public class ExpressionCacheValue {

	private ExpressionEvaluator expressionEvaluator;
	private Class<?>[] parameterTypes;
	private String[] parameterNames;
	private String ruleName;
	private Long validFrom;
	private Long validTo;

	public ExpressionEvaluator getExpressionEvaluator() {
		return expressionEvaluator;
	}

	public void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator) {
		this.expressionEvaluator = expressionEvaluator;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public String[] getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Long getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Long validFrom) {
		this.validFrom = validFrom;
	}

	public Long getValidTo() {
		return validTo;
	}

	public void setValidTo(Long validTo) {
		this.validTo = validTo;
	}

}
