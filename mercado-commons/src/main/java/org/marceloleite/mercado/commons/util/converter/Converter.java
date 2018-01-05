package org.marceloleite.mercado.commons.util.converter;

public interface Converter<F, T> {

	T convertTo(F object);
	
	F convertFrom(T object);
}
