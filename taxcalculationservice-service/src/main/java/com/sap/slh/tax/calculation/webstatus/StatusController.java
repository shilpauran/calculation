package com.sap.slh.tax.calculation.webstatus;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);

	@RequestMapping(value = "/calculation/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String checkStatus() {
		LOGGER.debug("Application Status Check Called : {}", new Date(System.currentTimeMillis()));
		return "Status:UP";
	}

}
