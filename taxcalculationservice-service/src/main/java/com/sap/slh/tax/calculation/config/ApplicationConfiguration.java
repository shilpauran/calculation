package com.sap.slh.tax.calculation.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

@EnableAutoConfiguration
@Configuration
public class ApplicationConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);
	
	@Value("${tax.rest_template_max_total_connection:500}")
    private int restTemplateMaxConnectionTotal;
    @Value("${tax.rest_template_max_per_route_connection:500}")
    private int restTemplateMaxPerRouteConnection;
    @Value("${tax.rest_template_request_timeout:500}")
    private int restTemplateRequestTimeout;

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Gson gson() {
		logger.debug("xVal - GSON builder called");
		return new GsonBuilder().setDateFormat(TaxCalculationConstants.DATE_PATTERN).create();
	}
	
	 @Bean(name = "taxRestTemplate")
	    public RestTemplate taxRestTemplate() {
	        final RestTemplate proxyTemplate = new RestTemplate();
	        HttpMessageConverter<?> stringHttpMessageConverternew = new StringHttpMessageConverter(UTF_8);
	        HttpMessageConverter<?> jacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
	        List<HttpMessageConverter<?>> convertors = new ArrayList<>();
	        convertors.add(stringHttpMessageConverternew);
	        convertors.add(jacksonHttpMessageConverter);
	        proxyTemplate.setMessageConverters(convertors);
	        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
	        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(restTemplateMaxPerRouteConnection);
	        poolingHttpClientConnectionManager.setMaxTotal(restTemplateMaxConnectionTotal);
	        final HttpClient httpClient = HttpClientBuilder.create()
	                .setConnectionManager(poolingHttpClientConnectionManager).build();
	        final HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
	                httpClient);
	        httpComponentsClientHttpRequestFactory.setConnectTimeout(restTemplateRequestTimeout * 1000);
	        httpComponentsClientHttpRequestFactory.setReadTimeout(restTemplateRequestTimeout * 1000);
	        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(restTemplateRequestTimeout * 1000);
	        proxyTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
	        return proxyTemplate;
	    }


}
