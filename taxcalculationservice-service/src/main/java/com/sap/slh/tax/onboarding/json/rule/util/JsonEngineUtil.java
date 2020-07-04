package com.sap.slh.tax.onboarding.json.rule.util;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.SignedJWT;
import com.sap.slh.tax.calculation.exception.TenantNotFoundException;
import com.sap.slh.tax.calculation.utility.TaxCalculationConstants;

import io.micrometer.core.instrument.util.StringUtils;

public class JsonEngineUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonEngineUtil.class);
	
	public static String getTenantId(String authToken) {

		if (StringUtils.isEmpty(authToken)) {
			LOGGER.error("Authorization token not passed");
			throw new TenantNotFoundException();
		}
		try {
			String jwtToken = authToken.split("\\s")[1];
			SignedJWT signedJWT = null;

			signedJWT = SignedJWT.parse(jwtToken);

			Payload payload = signedJWT.getPayload();

			if (payload.toJSONObject().containsKey(TaxCalculationConstants.ZONE_ID)) {
				return (String) payload.toJSONObject().get(TaxCalculationConstants.ZONE_ID);

			} else {
				LOGGER.error("Tenant ID not found in the authentication token ");
				throw new TenantNotFoundException();
			}

		} catch (ParseException e) {
			LOGGER.error("Parse Exception occured while fetching tenant Id : ", e);
			throw new TenantNotFoundException();
		}
	}

}
