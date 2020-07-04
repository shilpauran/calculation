package com.sap.slh.tax.calculation.model.common;

import java.util.HashMap;
import java.util.Map;

public enum TaxCalculationErrorMessage {

	ITEM_ID_EXISTS("item.id.exists"), TAX_ID_EXISTS("tax.id.exists"),
	DATE_INVALID("date.invalid"), TAX_OBJECT_INVALID("tax.object.invalid"),
	ITEM_OBJECT_INVALID("item.object.invalid"), TAX_TYPE_CODE_INVALID("tax.type.code.invalid"),
	TAX_RATE_MISSING("tax.rate.missing"), TAX_RATE_INVALID("tax.rate.invalid"),
	REQUEST_STRUCTURE_INVALID("request.structure.invalid"), REQUEST_PAYLOAD_EMPTY("request.payload.empty"),
	LINE_ITEM_INVALID("line.item.invalid"), ERROR_DOCUMENT_ID_REQUIRED("error.document.id.required"),
	ERROR_DOCUMENT_DATE_REQUIRED("error.document.date.required"),
	ERROR_DOCUMENT_AMOUNTTYPECODE_REQUIRED("error.document.amountTypeCode.required"),
	ERROR_DOCUMENT_CURRENCYCODE_REQUIRED("error.document.currencyCode.required"),
	ERROR_DOCUMENT_ITEMS_REQUIRED("error.document.items.required"), ERROR_ITEM_ID_REQUIRED("error.item.id.required"),
	ERROR_ITEM_QUANTITY_REQUIRED("error.item.quantity.required"),
	ERROR_ITEM_UNITPRICE_REQUIRED("error.item.unitPrice.required"),
	ERROR_ITEM_COUNTRYREGIONCODE_REQUIRED("error.item.countryRegionCode.required"),
	ERROR_ITEM_TAXEVENTCODE_REQUIRED("error.item.taxEventCode.required"),
	ERROR_ITEM_TAXES_REQUIRED("error.item.taxes.required"), ERROR_TAX_ID_REQUIRED("error.tax.id.required"),
	ERROR_TAX_TAXTYPECODE_REQUIRED("error.tax.taxTypeCode.required"),
	ERROR_TAX_DUECATEGORYCODE_REQUIRED("error.tax.dueCategoryCode.required");

	private static final Map<String, TaxCalculationErrorMessage> CONSTANTS = new HashMap<>();

	private String value;

	/**
	 * Instantiates a new response value.
	 *
	 * @param value the value
	 */
	private TaxCalculationErrorMessage(final String value) {
		this.value = value;
	}

	static {
		for (TaxCalculationErrorMessage c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	public static TaxCalculationErrorMessage fromValue(String value) {
		TaxCalculationErrorMessage constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value);
		} else {
			return constant;
		}

	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

}
