package org.marceloleite.mercado.business.checker;

public interface Check<T> {

	public boolean check(T object);
}
