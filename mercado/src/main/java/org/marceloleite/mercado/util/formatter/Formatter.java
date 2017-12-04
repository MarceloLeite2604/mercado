package org.marceloleite.mercado.util.formatter;

public interface Formatter<T1, T2> {

	T2 format(T1 object);
}
