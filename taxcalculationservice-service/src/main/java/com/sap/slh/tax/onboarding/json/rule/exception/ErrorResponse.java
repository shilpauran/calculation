package com.sap.slh.tax.onboarding.json.rule.exception;

import java.util.ArrayList;
import java.util.List;

import com.sap.slh.tax.onboarding.json.rule.model.BaseResponse;

/**
 * This is the class used to send response to client side.
 *
 *
 */
public class ErrorResponse extends BaseResponse {

    /** For Serialization. */
    private static final long serialVersionUID = 1L;
    /** List of errors that occured. */
    private List< ErrorDetail > errors = new ArrayList< >();

    /** The unique key. */
    private String errorId;

    /**
     * This is default Constructor to set status as failure.
     */
    public ErrorResponse() {
        super( Status.FAILURE, "Operation failed" );
    }

    /**
     * Getter for all errors that occured.
     *
     * @return List<String>
     */
    public List< ErrorDetail > getErrors() {
        return errors;
    }

    /**
     * This method sets List of all errors to response.
     *
     * @param errors
     *            List of errors
     */
    public void setErrors( final List< ErrorDetail > errors ) {
        this.errors = errors;
    }

    /**
     * This method adds errors to response.
     *
     * @param errorCode
     *            error code
     * @param errorDetail
     *            error detail
     *
     */
    public void addError( final String errorCode, final String errorDetail ) {
        errors.add( new ErrorDetail( errorCode, errorDetail ) );
    }

    /**
     * Gets the unique key.
     *
     * @return the unique key
     */
    public String getErrorId() {
        return errorId;
    }

    /**
     * Sets the unique key.
     *
     * @param uniqueKey
     *            the new unique key
     */
    public void setErrorId( String uniqueKey ) {
        this.errorId = uniqueKey;
    }

}
