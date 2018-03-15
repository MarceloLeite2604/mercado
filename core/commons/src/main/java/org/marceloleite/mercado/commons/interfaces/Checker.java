package org.marceloleite.mercado.commons.interfaces;

@FunctionalInterface
public interface Checker<T> {

	public boolean check(T object);
}
