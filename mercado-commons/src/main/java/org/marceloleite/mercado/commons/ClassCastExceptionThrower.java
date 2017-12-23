package org.marceloleite.mercado.commons;

public class ClassCastExceptionThrower {

	private Class<?> requiredClass;

	private Object objectFailedToCast;

	public ClassCastExceptionThrower(Class<?> requiredClass, Object objectFailedToCast) {
		super();
		this.requiredClass = requiredClass;
		this.objectFailedToCast = objectFailedToCast;
	}

	public void throwException() {
		throw new ClassCastException("Cannot cast object as \"" + requiredClass.getName() + "\". Object class is \""
				+ objectFailedToCast.getClass().getName() + "\".");
	}
}
