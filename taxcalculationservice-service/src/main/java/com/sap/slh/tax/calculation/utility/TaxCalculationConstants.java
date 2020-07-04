package com.sap.slh.tax.calculation.utility;

public class TaxCalculationConstants {

	public static final String TAXABLE_BASE_AMOUNT = "taxablebaseAmount";
	public static final String TAX_AMOUNT = "taxAmount";
	public static final String DEDUCTIBLE_TAXAMOUNT = "deductibleTaxAmount";
	public static final String NONDEDUCTIBLE_TAXAMOUNT = "nonDeductibleTaxAmount";
	public static final String NONDEDUCTIBLE_TAX_RATE = "nonDeductibleTaxRate";

	public static final String ID = "id";
	public static final String DATE = "date";
	public static final String TAX_EVENT_CODE = "taxEventCode";
	public static final String TAXES = "taxes";
	public static final String ITEMS = "items";
	public static final String TAX_TYPE_CODE = "taxTypeCode";
	public static final String IS_TAX_EVENT_NON_TAXABLE = "isTaxEventNonTaxable";
	public static final String TAX_RATE = "taxRate";

	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String SLASH = "/";
	public static final String JSON_SCHEMA_PATH = "classpath:json_schema.json";
	public static final String JSON_SCHEMA_INVALID = "Invalid json schema structure found";

	public static final String TRUE = "TRUE";

	public static final String TAX_TYPECODE_1 = "1";
	public static final String TAX_TYPECODE_2 = "2";
	public static final String TAX_TYPECODE_3 = "3";
	public static final String TAX_TYPECODE_4 = "4";
	public static final String ROUND_HALF_UP = "ROUND_HALF_UP";
	public static final int DEFAULT_DECIMAL_PLACES = 2;
	public static final String DEFAULT_ROUND_HALF_UP = "ROUND_HALF_UP";

	public static final String RULESERVICEIDS = "RULESERVICEIDS";
	public static final String RECEIVABLE = "RECEIVABLE";
	public static final CharSequence DOT = ".";

	public static final String RULETYPE = "TaxBaseCalculation";
	public static final String ZONE_ID = "zid";
	public static final String EXT_ATTR = "ext_attr";
	public static final String ID_ZONE = "zdn";
	public static final String BEARER = "bearer ";

	public static final String VCAPENTRY_MSG = "Could not retrieve information from VCAP entry %s";

	public static final String CORRELATION_ID = "correlation_id";
	public static final String X_CORRELATION_ID = "X-CorrelationID";
	public static final String TENANT_ID = "tenant_id";

	public static final String CIRCUIT_OPEN_MESSAGE = "Tax calculation service unavailable. Please try again later";
	public static final String CB_STATUS_OPEN = "OPEN";
	public static final String CB_STATUS_CLOSED = "CLOSED";

	private TaxCalculationConstants() {
	}

}
