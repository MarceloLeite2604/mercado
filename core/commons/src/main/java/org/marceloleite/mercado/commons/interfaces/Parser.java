package org.marceloleite.mercado.commons.interfaces;

@FunctionalInterface
public interface Parser<E> {

	public E parse(String string);
}
