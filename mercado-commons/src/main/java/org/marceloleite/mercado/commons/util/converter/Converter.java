package org.marceloleite.mercado.commons.util.converter;

public interface Converter<F, T> {

	T convert(F object);
}
