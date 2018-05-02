package org.marceloleite.mercado.commons.interfaces;

public interface Converter<F, T> {

	T convertTo(F object);
	
	F convertFrom(T object);
}
