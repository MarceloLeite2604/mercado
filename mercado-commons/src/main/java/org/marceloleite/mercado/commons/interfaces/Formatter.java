package org.marceloleite.mercado.commons.interfaces;

public interface Formatter<FROM, TO> {

	TO format(FROM object);
}
