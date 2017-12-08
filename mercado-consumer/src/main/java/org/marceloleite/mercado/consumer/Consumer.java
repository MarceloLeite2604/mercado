package org.marceloleite.mercado.consumer;

public interface Consumer<T> {

	T consume(Object... args);
}
