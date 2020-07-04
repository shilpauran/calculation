package com.sap.slh.tax.calculation.logging.aspect;

import static org.junit.Assert.assertNull;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.sap.slh.tax.calculation.exception.GlobalExceptionHandler;
import com.sap.slh.tax.calculation.exception.TenantNotFoundException;
import com.sap.slh.tax.calculation.external.service.TaxCalculationListener;
import com.sap.slh.tax.calculation.model.common.TaxCalculationRequest;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponse;
import com.sap.slh.tax.calculation.utility.JWTUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(LoggingAspectTestConfiguration.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class LoggingAspectTest {
	@MockBean
	JWTUtil jwtUtil;
	@MockBean
	GlobalExceptionHandler globalExceptionHAndler;

	@Autowired
	TaxCalculationListener target;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void aroundTestWithCorrelationId() {
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("X-CorrelationID", UUID.randomUUID().toString());
		messageProperties.setHeader(HttpHeaders.AUTHORIZATION, "bearer 1234");
		Message calculationRequest = converter.toMessage(taxCalculationRequest, messageProperties);
		Mockito.when(jwtUtil.getTenantId("bearer 1234")).thenReturn("1234");
		TaxCalculationResponse response = target.calculateTaxes(calculationRequest);
		assertNull(response);
	}

	@Test
	public void aroundTestWithoutCorrelationId() {
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader(HttpHeaders.AUTHORIZATION, "bearer 1234");
		Message calculationRequest = converter.toMessage(taxCalculationRequest, messageProperties);
		Mockito.when(jwtUtil.getTenantId("bearer 1234")).thenThrow(new TenantNotFoundException());
		TaxCalculationResponse response = target.calculateTaxes(calculationRequest);
		assertNull(response);
	}
}
