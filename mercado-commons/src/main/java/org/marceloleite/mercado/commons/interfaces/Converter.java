package org.marceloleite.mercado.commons.interfaces;

public interface Converter<F, T> {

	T convert(F object);
}
