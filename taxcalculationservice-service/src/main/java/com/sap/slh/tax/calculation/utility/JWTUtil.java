package com.sap.slh.tax.calculation.utility;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.SignedJWT;
import com.sap.slh.tax.calculation.exception.TenantNotFoundException;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class JWTUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);

	public String getTenantId(String authToken) {

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
			LOGGER.error("Parse Exception occured while fetching tenant Id ", e.getMessage());
			throw new TenantNotFoundException();
		}

	}
}
