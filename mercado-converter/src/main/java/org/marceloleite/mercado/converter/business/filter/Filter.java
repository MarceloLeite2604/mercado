package org.marceloleite.mercado.converter.business.filter;

public interface Filter<T> {

	public T filter(T object);
}
