package com.sap.slh.tax.onboarding.json.rule.exception;

import java.io.Serializable;

public class ErrorDetail implements Serializable {

	/** For serialization. */
	private static final long serialVersionUID = -7855228152246149403L;
	/** Error code. */
	private String code;
	/** Error detail. */
	private String detail;

	/**
	 * Empty consturctor.
	 */
	public ErrorDetail() {
		// Do nothing
	}

	/**
	 * Constructor.
	 *
	 * @param code   error code
	 * @param detail error detail
	 */
	public ErrorDetail(final String code, final String detail) {
		this.code = code;
		this.detail = detail;
	}

	/**
	 * Get the error code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Set the error code.
	 *
	 * @param code the code to set
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * Get the error detail.
	 *
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Set the error detail.
	 *
	 * @param detail the detail to set
	 */
	public void setDetail(final String detail) {
		this.detail = detail;
	}

}
