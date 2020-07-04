package com.sap.slh.tax.calculation.utility;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.slh.tax.calculation.exception.ApplicationException;
import com.sap.slh.tax.calculation.model.common.TaxCalculationProcessingStatusCode;
import com.sap.slh.tax.calculation.model.common.TaxCalculationStatus;


/**
 * Provides utility method for Object to Json conversion.
 *
 * @author 
 *
 */
public final class JsonUtil{

	private JsonUtil() { }
	
	private static final ObjectMapper propertyMapper = new ObjectMapper();
	

    /**
     * This converts Object of type E to its JSON string.
     *
     * @param <E>
     *            the element type
     * @param type
     *            - The type of Object.
     * @return - JSON String.
     */
    public static <E> String toJsonString(final E type) {
        final E object = type;
        String jsonValue = null;
        if (object != null) {
            try {
                propertyMapper.setSerializationInclusion(Include.NON_NULL);
                jsonValue = propertyMapper.writeValueAsString(object);
            } catch (IOException exception) {
                final String errorMsg = "Object to Json transformation failed : " + object.toString();
                throw new ApplicationException(errorMsg, exception, TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR,TaxCalculationStatus.FAILURE);
            }
        }
        return jsonValue;
    }
    
	public static <E> E toObjectFromByte(final byte[] src, final Class<E> clazz) {

		E object = null;
		propertyMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			object = propertyMapper.readValue(src, clazz);
		} catch (IOException exception) {
			final String errorMsg = "Json to Object transformation failed: " + src;
			throw new ApplicationException(errorMsg, exception, TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR,TaxCalculationStatus.FAILURE);
		}
		return object;
	}
	
	public static <E> List<E> toListFromString(String src, final TypeReference<List<E>> valueTypeRef)
	{
		List<E> object = null;
		try {
			object = propertyMapper.readValue(src,valueTypeRef);
		} catch (IOException exception) {
			final String errorMsg = "Json to Object transformation failed: " + src;
			throw new ApplicationException(errorMsg, exception, TaxCalculationProcessingStatusCode.INTERNAL_SERVER_ERROR,TaxCalculationStatus.FAILURE);
		}
		return object;
	}
	

}
