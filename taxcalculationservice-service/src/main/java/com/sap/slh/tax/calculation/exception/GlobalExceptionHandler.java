package com.sap.slh.tax.calculation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.utility.JsonUtil;

@Component
public class GlobalExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Value(value = "${tax_calculation_service.logger.stack_trace_line_limit:10}")
	private Long logStackTraceLineLimit;

	public TaxCalculationResponse handleGlobalCustomException(final CustomException exception) {
		return processCustomException(exception);
	}


	private TaxCalculationResponse processCustomException(CustomException exception) {
		TaxCalculationResponse response = new TaxCalculationResponse();
		response.setError(exception.getError());
		response.setStatusMessage(exception.getProcessingStatusCode().getValue());
		response.setProcessingStatusCode(exception.getProcessingStatusCode());
		response.setStatus(exception.getStatus());
		String limitedStackTrace = getLimitedStackTrace(exception.getStackTrace());
		LOGGER.error("\nAPPLICATION ERROR : {} \nSTACK TRACE : \n{}", JsonUtil.toJsonString(response),
				limitedStackTrace);
		return response;

	}
	
	private String getLimitedStackTrace(StackTraceElement[] stackTraceElements) {
		if (stackTraceElements != null && stackTraceElements.length > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < logStackTraceLineLimit && i < stackTraceElements.length; i++) {
				stringBuilder.append(stackTraceElements[i]).append("\n");
			}
			return stringBuilder.toString();
		}
		return null;
	}

}
