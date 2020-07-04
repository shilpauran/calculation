package com.sap.slh.tax.onboarding.json.rule.exception;

/**
 * Base class for all BRS related exception.
 *
 */
public class JsonRuleException extends RuntimeException {

	/** For serialization. */
	private static final long serialVersionUID = -3838987348527735362L;

	/** Error code. */
	private final ErrorCode errorCode;

	/**
	 * Constructor to set error message.
	 *
	 * @param message   - error message
	 * @param errorCode - error error code
	 */
	public JsonRuleException(final String message, final ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Constructor to set error message and <code>Throwable</code> instance.
	 *
	 * @param message   - error message
	 * @param exception - <code>Throwable</code> instance
	 */
	public JsonRuleException(final String message, final Throwable exception) {
		super(message, exception);
		this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
	}

	/**
	 * Constructor to set error message and <code>Throwable</code> instance.
	 *
	 * @param message   - error message
	 * @param exception - <code>Throwable</code> instance
	 * @param errorCode - error error code
	 */
	public JsonRuleException(final String message, final Throwable exception, final ErrorCode errorCode) {
		super(message, exception);
		this.errorCode = errorCode;
	}

	/**
	 * Return the error code.
	 *
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
