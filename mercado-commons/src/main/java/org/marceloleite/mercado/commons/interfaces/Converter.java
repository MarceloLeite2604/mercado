package org.marceloleite.mercado.commons.interfaces;

public interface Converter<F, T> {

	T format(F object);
}
