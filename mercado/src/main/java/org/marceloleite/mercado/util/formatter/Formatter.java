package org.marceloleite.mercado.util.formatter;

public interface Formatter<FROM, TO> {

	TO format(FROM object);
}
