package com.sap.slh.tax.calculation.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sap.slh.tax.calculation.model.BaseModel;
import com.sap.slh.tax.calculation.model.api.Response;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.calculation.utility.ErrorTokenGenerator;
import com.sap.slh.tax.calculation.utility.JsonUtil;
import com.sap.slh.tax.onboarding.json.rule.exception.ErrorCode;
import com.sap.slh.tax.onboarding.json.rule.exception.ErrorResponse;
import com.sap.slh.tax.onboarding.json.rule.exception.JsonRuleException;
import com.sap.slh.tax.onboarding.json.rule.exception.TenantIDNotFoundException;

/**
 * General error handler for the application.
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandlerForSyncRequests {

	/** Logger instance. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerForSyncRequests.class);

	@Value(value = "${tax_calculation.logger.stack_trace_line_limit:10}")
	private Long logStackTraceLineLimit;

	/**
	 * Handle global http request method not supported.
	 *
	 * @param exception the exception
	 * @return the response
	 */
	// Does not call aspect
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Response<BaseModel> handleGlobalHttpRequestMethodNotSupported(final Exception exception) {
		return processException(exception, TaxCalculationProcessingStatusCode.OPERATION_UNSUPPORTED_ERROR);
	}

	/**
	 * Handle global application exception.
	 *
	 * @param exception the exception
	 * @return the response
	 */
	@ExceptionHandler(value = ApplicationException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Response<BaseModel> handleGlobalApplicationException(final ApplicationException exception) {
		return processApplicationException(exception);
	}

	/**
	 * Handle global illegal argument exception.
	 *
	 * @param exception the exception
	 * @return the response
	 */
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Response<BaseModel> handleGlobalIllegalArgumentException(final IllegalArgumentException exception) {
		return processIllegalArgumentException(exception);
	}

	/**
	 * Handle global general exception.
	 *
	 * @param exception the exception
	 * @return the response
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Response<BaseModel> handleGlobalGeneralException(final Exception exception) {
		return processException(exception, TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Response<BaseModel> handleGlobalHttpMessageNotReadableException(
			final HttpMessageNotReadableException exception) {
		return processException(exception, TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR);
	}

	/**
	 * This method is handler for MethodArgumentNotValidException Exceptions.
	 *
	 * @param validationException that has been thrown
	 * @return ErrorResponse response to send back
	 */
	// Does not call aspect
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Response<BaseModel> handleValidationException(final MethodArgumentNotValidException exception) {
		Response<BaseModel> response = new Response<>();
		response.setProcessingStatusCode(TaxCalculationProcessingStatusCode.REQUEST_NOT_VALID);
		response.setError(getApplicationError());
		response.setStatusMessage(TaxCalculationProcessingStatusCode.REQUEST_NOT_VALID.getValue());
		response.setStatus(TaxCalculationStatus.FAILURE);
		LOGGER.error(JsonUtil.toJsonString(response), exception);
		return response;
	}

	private TaxCalculationApplicationError getApplicationError() {
		TaxCalculationApplicationError applicationError = new TaxCalculationApplicationError();
		applicationError.setErrorId(ErrorTokenGenerator.getErrorId());
		return applicationError;
	}


	/**
	 * This method is handler for BindException Exceptions.
	 *
	 * @param bindException that has been thrown
	 * @return ErrorResponse response to send back
	 */
	// Does not call aspect
	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Response<BaseModel> handleBindingException(final BindException exception) {
		Response<BaseModel> response = new Response<>();
		response.setProcessingStatusCode(TaxCalculationProcessingStatusCode.REQUEST_NOT_VALID);
		response.setError(getApplicationError());
		response.setStatusMessage(TaxCalculationProcessingStatusCode.REQUEST_NOT_VALID.getValue());
		response.setStatus(TaxCalculationStatus.FAILURE);
		LOGGER.error(JsonUtil.toJsonString(response), exception);
		return response;
	}


	/**
	 * Process application exception.
	 *
	 * @param exception the exception
	 * @return the response
	 */
	public Response<BaseModel> processApplicationException(ApplicationException exception) {
		Response<BaseModel> response = new Response<>();
		response.setError(exception.getError());
		response.setStatusMessage(exception.getMessage());
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

	/**
	 * Process illegal argument exception.
	 *
	 * @param exception the exception
	 * @return the response
	 */
	public Response<BaseModel> processIllegalArgumentException(IllegalArgumentException exception) {
		Response<BaseModel> response = new Response<>();
		response.setError(getApplicationError());
		response.setStatusMessage(exception.getMessage());
		response.setProcessingStatusCode(TaxCalculationProcessingStatusCode.OPERATION_UNSUPPORTED_ERROR);
		response.setStatus(TaxCalculationStatus.FAILURE);
		LOGGER.error(JsonUtil.toJsonString(response), exception);
		return response;
	}

	public Response<BaseModel> processException(Exception exception,
			TaxCalculationProcessingStatusCode taxCalculationProcessingStatusCode) {
		Response<BaseModel> response = new Response<>();
		response.setError(getApplicationError());
		response.setStatusMessage(exception.getMessage());
		response.setProcessingStatusCode(taxCalculationProcessingStatusCode);
		response.setStatus(TaxCalculationStatus.FAILURE);
		LOGGER.error(JsonUtil.toJsonString(response), exception);
		return response;
	}
	
	public Response<BaseModel> processThrowable(Throwable t,
			TaxCalculationProcessingStatusCode taxCalculationProcessingStatusCode) {
		Response<BaseModel> response = new Response<>();
		response.setError(getApplicationError());
		response.setStatusMessage(t.getMessage());
		response.setProcessingStatusCode(taxCalculationProcessingStatusCode);
		response.setStatus(TaxCalculationStatus.FAILURE);
		LOGGER.error(JsonUtil.toJsonString(response), t);
		return response;
	}

	public Response<BaseModel> handleContainerException(final ExceptionContainer exception) {
		Response<BaseModel> response = new Response<>();
		List<ApplicationException> list = exception.getExceptions();
		Map<String, Object> debugInfomessage = new HashMap<>();
		StringBuilder builder = new StringBuilder();

		for (ApplicationException exception1 : list) {
			Map<String, Object> debugInfo = exception1.getDebugInfo();
			Set<String> keys = debugInfo.keySet();
			for (String key : keys) {
				String value = (String) debugInfo.get(key);
				if (!StringUtils.isEmpty(value)) {
					if (debugInfomessage.containsKey(key)) {
						StringBuilder build = new StringBuilder();
						String value1 = (String) debugInfomessage.get(key);
						if (!value.equalsIgnoreCase(value1)) {
							build.append(value1).append(",").append(value);
							debugInfomessage.put(key, build);
						}

					} else {
						debugInfomessage.put(key, debugInfo.get(key));
					}

				}

			}
			builder.append(",");
		}
		response.setProcessingStatusCode(TaxCalculationProcessingStatusCode.OPERATION_UNSUPPORTED_ERROR);
		response.setError(getApplicationError());
		response.setStatusMessage(builder.toString());

		response.setStatus(TaxCalculationStatus.FAILURE);
		LOGGER.error(JsonUtil.toJsonString(response), exception);
		return response;
	}
	
	@ExceptionHandler(value = TenantIDNotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ErrorResponse tenantNotFoundException(final TenantIDNotFoundException exception, final HttpServletRequest request) {
		return handleTenantException(exception, exception.getErrorCode(), request, null);
	}
	/**
	 * This method is handler for generic Exceptions.
	 *
	 * @param exception   that needs to be handled
	 * @param errorCode   the error code
	 * @param request    the request
	 * @param otherErrorLog the other error log
	 * @return ErrorResponse that needs to be send back
	 */
	private ErrorResponse handleTenantException(final Exception exception, final ErrorCode errorCode,
			HttpServletRequest request, final String otherErrorLog) {
		final String errorId = logException(exception, request, errorCode, otherErrorLog);
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.addError(errorCode.getCode(), errorCode.getErrorDetail());
		errorResponse.setErrorId(errorId);
		return errorResponse;
	}

	@ExceptionHandler(value = JsonRuleException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleSecurityException(final JsonRuleException exception, final HttpServletRequest request) {
		return handleException(exception, exception.getErrorCode(), request, null);
	}

	/**
	 * This method is handler for generic Exceptions.
	 *
	 * @param exception     that needs to be handled
	 * @param errorCode     the error code
	 * @param request       the request
	 * @param otherErrorLog the other error log
	 * @return ErrorResponse that needs to be send back
	 */
	private ErrorResponse handleException(final Exception exception, final ErrorCode errorCode,
			HttpServletRequest request, final String otherErrorLog) {
		final String errorId = logException(exception, request, errorCode, otherErrorLog);
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.addError(errorCode.getCode(), errorCode.getErrorDetail());
		errorResponse.setErrorId(errorId);
		return errorResponse;
	}
	

	/**
	 * This method is used to log Exceptions.
	 *
	 * @param exception     that needs to be logged
	 * @param request       the request
	 * @param otherErrorLog the other error log
	 * @return Error id
	 */
	private String logException(final Throwable exception, final HttpServletRequest request, final ErrorCode errorCode,
			final String otherErrorLog) {

		final String errorId = ErrorTokenGenerator.getErrorId();
		final StringBuilder error = new StringBuilder("Error id->" + errorId);
		error.append("\n" + exception.getLocalizedMessage());
		LOGGER.error(error.toString(), exception);
		return errorId;
	}
}
