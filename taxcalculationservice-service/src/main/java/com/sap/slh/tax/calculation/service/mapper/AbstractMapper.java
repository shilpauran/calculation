package com.sap.slh.tax.calculation.service.mapper;

import com.sap.slh.tax.calculation.exception.ApplicationException;

/**
 * This abstract class is provide partial implementation of builder.
 *
 *
 * @param <A> is the domain class.
 * @param <B> is the dto class.
 */
public abstract class AbstractMapper<A, B> implements Mapper<A, B> {

	/**
	 * prototypeA is the object of class type A.
	 */
	private final Class<A> prototypeA;

	/**
	 * prototypeB is the object of class type B.
	 */
	private final Class<B> prototypeB;

	/**
	 * This is parameterized constructor.
	 *
	 * @param prototypeA is the object of class type A.
	 * @param prototypeB is the object of class type B.
	 */
	public AbstractMapper(final Class<A> prototypeA, final Class<B> prototypeB) {
		this.prototypeA = prototypeA;
		this.prototypeB = prototypeB;
	}

	/**
	 * This method converts dto object to domain object.
	 *
	 * @param sourceObject is the dto object to be converted to domain.
	 * @return A - destination object
	 */
	@Override
	public A convertFrom(final B sourceObject) {
		A destinationObject = null;

		if (sourceObject != null) {
			try {
				destinationObject = prototypeA.newInstance();
				convertFrom(sourceObject, destinationObject);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ApplicationException(e.getMessage(), e);
			}
		}

		return destinationObject;
	}

	/**
	 * This method converts domain object to dto object.
	 *
	 * @param sourceObject is the domain object to be converted to dto.
	 * @return B - destination object
	 */
	@Override
	public B convertTo(final A sourceObject) {
		B destinationObject = null;

		if (sourceObject != null) {
			try {
				destinationObject = prototypeB.newInstance();
				convertTo(sourceObject, destinationObject);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ApplicationException(e.getMessage(), e);
			}
		}
		return destinationObject;
	}
}
