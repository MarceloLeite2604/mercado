package org.marceloleite.mercado.modeler.business.filter;

public interface Filter<T> {

	public T filter(T object);
}
