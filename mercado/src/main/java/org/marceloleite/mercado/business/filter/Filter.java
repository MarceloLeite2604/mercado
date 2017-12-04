package org.marceloleite.mercado.business.filter;

public interface Filter<T> {

	public T filter(T object);
}
