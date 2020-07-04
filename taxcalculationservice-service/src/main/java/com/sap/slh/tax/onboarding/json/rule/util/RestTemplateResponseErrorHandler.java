package com.sap.slh.tax.onboarding.json.rule.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateResponseErrorHandler 
  implements ResponseErrorHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);
 
    @Override
    public boolean hasError(ClientHttpResponse response) 
      throws IOException {
    	return new DefaultResponseErrorHandler().hasError(response);
    }
 
    @Override
    public void handleError(ClientHttpResponse response) 
      throws IOException {
    	if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR)
    		LOGGER.error("400 series issue occured");
    }
}