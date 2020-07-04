package com.sap.slh.tax.onboarding.json.rule.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sap.slh.tax.onboarding.json.rule.exception.ErrorCode;
import com.sap.slh.tax.onboarding.json.rule.exception.JsonRuleException;

@Component
public class RestServiceUtil {

	@Autowired
	RestTemplate taxRestTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceUtil.class);


	public Object getCalculationRules(UriComponentsBuilder builder) {
		
		final HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		ResponseEntity<Object> responseEntity = taxRestTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				buildRequest(headers, null), Object.class);

		if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			LOGGER.error("Call to get calculation rules failed with status code : {} ", responseEntity.getStatusCode());
			throw new JsonRuleException(ErrorCode.INTERNAL_SERVER_ERROR.getErrorDetail(),
					ErrorCode.INTERNAL_SERVER_ERROR);
		}

		return responseEntity.getBody();
	}
	

	public static HttpEntity<Object> buildRequest(final HttpHeaders headers, final Object request) {
		return new HttpEntity<>(request, headers);
	}


	public static UriComponentsBuilder buildGetCalculationRulesUri(String url) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(url + "/calculation/rules");
		LOGGER.error(" get calculation rules URL {}", builder.toUriString());
		return builder;
	}

}