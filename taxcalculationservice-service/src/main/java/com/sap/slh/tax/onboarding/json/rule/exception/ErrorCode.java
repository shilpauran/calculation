package com.sap.slh.tax.onboarding.json.rule.exception;

/**
 * Bundles error codes.
 *
 */
public enum ErrorCode {

    /** internal server error. */
    INTERNAL_SERVER_ERROR( "system.error", "System error occurred" ),

    /** The access denied error. */
    ACCESS_DENIED_ERROR( "access.denied", "Access is denied. Please check the auth token provided in the header" ),

    /** invalid format error. */
    INVALID_FORMAT_ERROR( "invalid.format.error", "Input format is not valid" ),

    /** The json conversion error. */
    JSON_CONVERSION_ERROR( "json.conversion.error", "Object to Json transformation failed." ),

    /** The object conversion error. */
    OBJECT_CONVERSION_ERROR( "object.conversion.error", "Json to object transformation failed." ),

    /** The opportunity already exists. */
    KEY_NOT_FOUND( "key.not.found", "Redis key not found" ),

    /** The owner not found. */
    TENANT_NOT_FOUND( "tenant.id.not.found", "Tenant id not found." ),

    /** The result not found. */
    RESULT_NOT_FOUND( "brs.result.not.found", "Result not found." ),
	
	/**User Information not accessible. */
	IDZONE_NOT_FOUND( "brs.idzone.not.found","BRS Id Zone not found.");


    /** error code. */
    private String code;

    /** error detail. */
    private String errorDetail;

    /**
     * Parameterized constructor.
     *
     * @param errorCode
     *            the error code
     * @param errorDetail
     *            the error detail
     */
    private ErrorCode( final String errorCode, final String errorDetail ) {
        this.code = errorCode;
        this.errorDetail = errorDetail;

    }

    /**
     * This method returns the error code.
     *
     * @return error code.
     */
    public String getCode() {
        return code;
    }

    /**
     * This method returns the error detail.
     *
     * @return error detail.
     */
    public String getErrorDetail() {
        return errorDetail;
    }
}
