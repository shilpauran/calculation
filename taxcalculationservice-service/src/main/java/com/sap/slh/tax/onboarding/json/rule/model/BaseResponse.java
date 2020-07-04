package com.sap.slh.tax.onboarding.json.rule.model;

import java.io.Serializable;

/**
 * This class is base class for all responses returned to client.
 *
 *
 */
public class BaseResponse implements Serializable {

	/** For Serialization. */
	private static final long serialVersionUID = 8990417617231048103L;

	/**
	 * Response statuses.
	 */
	public enum Status {
		/**
		 * This enum is defined to set CREATED_BY as Application.
		 */
		FAILURE, SUCCESS, DUPLICATE, OK;
	}

	/** Response Message. */
	private String message;

	/** Response status if it is FAILURE or OK. */
	private Status status;

	/**
	 * Default constructor which sets response as OK initially.
	 */
	public BaseResponse() {
		this(Status.SUCCESS, "Operation was successful");
	}

	/**
	 * PArametereized constructor to set status and message of response.
	 *
	 * @param status  response status
	 * @param message text to show as response
	 */
	public BaseResponse(final Status status, final String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(final Status status) {
		this.status = status;
	}

}
