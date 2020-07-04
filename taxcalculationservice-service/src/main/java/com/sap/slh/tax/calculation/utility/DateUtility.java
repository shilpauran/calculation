package com.sap.slh.tax.calculation.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;
import com.sap.slh.tax.onboarding.json.rule.exception.ErrorCode;
import com.sap.slh.tax.onboarding.json.rule.exception.JsonRuleException;

public class DateUtility {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtility.class);

	private DateUtility() {

	}

	public static Date toDateFromString(String date) {
		try {
			return new SimpleDateFormat(TaxCalculationConstants.DATE_PATTERN).parse(date);
		} catch (ParseException exception) {
			final String errorMsg = " Date parsing failed ";
			throw new ApplicationException(errorMsg, exception,
					TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR, TaxCalculationStatus.FAILURE);
		}

	}

	public static String toStringFromDate(Date date) {
		return new SimpleDateFormat(TaxCalculationConstants.DATE_PATTERN).format(date);
	}
	
	
	public static Date toDate(String pattern, String date) {
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException exception) {
			LOGGER.error("Date Conversation error pattern : {} date : {} ", pattern, date);
			throw new JsonRuleException(ErrorCode.INTERNAL_SERVER_ERROR.getErrorDetail(), exception,
					ErrorCode.INTERNAL_SERVER_ERROR);
		}

	}
}
