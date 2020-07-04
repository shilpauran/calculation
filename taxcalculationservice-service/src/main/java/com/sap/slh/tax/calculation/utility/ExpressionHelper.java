
package com.sap.slh.tax.calculation.utility;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ExpressionHelper {

	private ExpressionHelper() {
	}

	/**
	 * Creates an instance of the given <var>type</var>, by calling the
	 * single-string-parameter constructor, or, if the <var>value</var> equals "",
	 * the zero-parameter constructor.
	 */
	public static Object createObject(Class<?> type, String value)
			throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {

		// Wrap primitive parameters.
		if (type.isPrimitive()) {
			type = (type == boolean.class ? Boolean.class
					: type == char.class ? Character.class
							: type == byte.class ? Byte.class
									: type == short.class ? Short.class
											: type == int.class ? Integer.class
													: type == long.class ? Long.class
															: type == float.class ? Float.class
																	: type == double.class ? Double.class : void.class);
		}

		// Construct object, assuming it has a default constructor or a
		// constructor with one single "String" argument.
		if ("".equals(value)) {
			return type.getConstructor(new Class[0]).newInstance(new Object[0]);
		} else {
			return type.getConstructor(String.class).newInstance(value);
		}
	}

	/**
	 * @return <var>s</var>, split at the commas
	 */
	public static String[] explode(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		List<String> l = new ArrayList<String>();
		while (st.hasMoreTokens())
			l.add(st.nextToken());
		return (String[]) l.toArray(new String[l.size()]);
	}

	/**
	 * @return <var>s</var>, converted to a Java type
	 * @throws ClassNotFoundException
	 */
	public static Class<?> stringToType(String s) throws ClassNotFoundException {

		int brackets = 0;
		while (s.endsWith("[]")) {
			++brackets;
			s = s.substring(0, s.length() - 2);
		}

		if (brackets == 0) {
			if ("void".equals(s))
				return void.class;
			if ("boolean".equals(s))
				return boolean.class;
			if ("char".equals(s))
				return char.class;
			if ("byte".equals(s))
				return byte.class;
			if ("short".equals(s))
				return short.class;
			if ("int".equals(s))
				return int.class;
			if ("long".equals(s))
				return long.class;
			if ("float".equals(s))
				return float.class;
			if ("double".equals(s))
				return double.class;
			if ("String".equals(s))
				return String.class;
			if ("BigDecimal".equals(s))
				return BigDecimal.class;

		}

		// Automatically convert primitive type names.
		if ("void".equals(s)) {
			s = "V";
		} else if ("boolean".equals(s)) {
			s = "Z";
		} else if ("char".equals(s)) {
			s = "C";
		} else if ("byte".equals(s)) {
			s = "B";
		} else if ("short".equals(s)) {
			s = "S";
		} else if ("int".equals(s)) {
			s = "I";
		} else if ("long".equals(s)) {
			s = "J";
		} else if ("float".equals(s)) {
			s = "F";
		} else if ("double".equals(s)) {
			s = "D";
		} else if ("String".equals(s)) {
			s = "Ljava.lang.String";
		}

		while (--brackets >= 0)
			s = '[' + s;

		return Class.forName(s);

	}

	/**
	 * Converts the given comma-separated list of class names to an array of
	 * {@link Class}es.
	 * 
	 * @throws ClassNotFoundException
	 */
	public static Class<?>[] stringToTypes(String s) throws ClassNotFoundException {
		StringTokenizer st = new StringTokenizer(s, ",");
		List<Class<?>> l = new ArrayList<Class<?>>();
		while (st.hasMoreTokens())
			l.add(ExpressionHelper.stringToType(st.nextToken()));
		Class<?>[] res = new Class[l.size()];
		l.toArray(res);
		return res;
	}

}
