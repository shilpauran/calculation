package com.sap.slh.tax.calculation.model.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sap.slh.tax.calculation.model.common.TaxCalculationApplicationError;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationResponseLine;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;

/**
 * The Class Response.
 * 
 * @param <T>
 *            the generic type
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> implements Serializable {

	private static final long serialVersionUID = -1710728014301374638L;

	private TaxCalculationStatus status;

    /** The status code. */
    private TaxCalculationProcessingStatusCode processingStatusCode;

    /** The status message. */
    private String statusMessage;

    /** The meta data. */
    private transient MetaData metaData;

    /** The result. */
    private TaxCalculationResponseLine result;

    private TaxCalculationApplicationError error;

    /**
     * Gets the meta data.
     *
     * @return the metaData
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Sets the meta data.
     *
     * @param metaData
     *            the metaData to set
     */
    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */


    public TaxCalculationProcessingStatusCode getProcessingStatusCode() {
        return processingStatusCode;
    }

    public void setProcessingStatusCode(TaxCalculationProcessingStatusCode processingStatusCode) {
        this.processingStatusCode = processingStatusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public TaxCalculationApplicationError getError() {
        return error;
    }

    public void setError(TaxCalculationApplicationError applicationError) {
        this.error = applicationError;
    }

	public TaxCalculationResponseLine getResult() {
		return result;
	}

	public void setResult(TaxCalculationResponseLine result) {
		this.result = result;
	}

	public TaxCalculationStatus getStatus() {
		return status;
	}

	public void setStatus(TaxCalculationStatus status) {
		this.status = status;
	}
}
