package com.sap.slh.tax.calculation.utility;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

public class RandomStringUtils {
	
	private RandomStringUtils() {
		
	}

	public static String generateSafeToken() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[7];
		random.nextBytes(bytes);
		Encoder encoder = Base64.getUrlEncoder().withoutPadding();
		return encoder.encodeToString(bytes);
	}

}
