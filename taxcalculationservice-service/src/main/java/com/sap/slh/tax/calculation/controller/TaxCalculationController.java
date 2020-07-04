package com.sap.slh.tax.calculation.controller;

import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.api.Response;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.calculation.model.uri.TaxCalculationUriConstant;
import com.sap.slh.tax.calculation.service.TaxCalculationService;
import com.sap.slh.tax.calculation.utility.JWTUtil;
import com.sap.slh.tax.calculation.utility.JsonUtil;
import com.sap.slh.tax.onboarding.cache.CacheConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@RestController
@Api(value = "tax calculation operations")
public class TaxCalculationController extends BaseAPIController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaxCalculationController.class);
	@Autowired
	private TaxCalculationService taxCalculationService;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private JWTUtil jwtUtil;
	@ApiOperation(value = "calculate tax", notes = "calculate tax", nickname = "calculate tax", response = TaxCalculationResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the response"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 204, message = "Content not found", response = Object.class),
			@ApiResponse(code = 206, message = "Partial Content", response = Object.class),
			@ApiResponse(code = 500, message = "Internal Server Error")
	})
	@RequestMapping(value = TaxCalculationUriConstant.CALCULATE_TAX, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<TaxCalculationResponse> calculate(
			@RequestBody(required = true) final TaxCalculationRequest taxCalculationRequest,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
		LOGGER.info("Calculate tax request data : {}", JsonUtil.toJsonString(taxCalculationRequest));
		String tenantId = jwtUtil.getTenantId(authToken);	
		if (!redisTemplate.hasKey(tenantId + CacheConstant.CACHE_SET_SUFFIX)) {
			throw new ApplicationException(TaxCalculationProcessingStatusCode.TENANT_ID_NOT_FOUND.getValue(),
					TaxCalculationProcessingStatusCode.INVALID_PARAMETER, TaxCalculationStatus.INVALID_REQUEST);
		}
		TaxCalculationResponse response = taxCalculationService.calculateTax(taxCalculationRequest, Locale.ENGLISH,
				tenantId);
		LOGGER.info("Calculate tax request data : {} response : {} ", JsonUtil.toJsonString(taxCalculationRequest),
				JsonUtil.toJsonString(response));
		Response<TaxCalculationResponse> resource = new Response<>();
		resource.setResult(response.getResult());
		resource.setStatus(TaxCalculationStatus.SUCCESS);
		resource.setStatusMessage(TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY.getValue());
		resource.setProcessingStatusCode(TaxCalculationProcessingStatusCode.TAX_CALCULATED_SUCCESSFULLY);
		return resource;
	}
}
