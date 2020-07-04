package com.sap.slh.tax.calculation.utility;

/**
 * This is utility class to generate unique token for Errors.
 * 
 * @author 
 */
public final class ErrorTokenGenerator {


    /** ApplicationError token prefix. */
    private static final String ERROR_TOKEN_PREFIX = "TAXCALC-";

    /**
     * This method generates a random error token.
     *
     * @return String - error token
     */
    
    private ErrorTokenGenerator() { }
    
    public static String getErrorId() {
		return ERROR_TOKEN_PREFIX + getUniqueString();
	}

    /**
     * This method returns a unique string.
     *
     * @return String.
     */
    private static String getUniqueString() {
		return RandomStringUtils.generateSafeToken();
	}

}
