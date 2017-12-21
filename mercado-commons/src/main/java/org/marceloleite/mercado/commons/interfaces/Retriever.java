package org.marceloleite.mercado.commons.interfaces;

public interface Retriever<T> {

	T retrieve(Object... args);
}
