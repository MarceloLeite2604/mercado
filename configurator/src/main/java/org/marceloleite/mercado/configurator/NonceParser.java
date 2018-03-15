package org.marceloleite.mercado.configurator;

import org.marceloleite.mercado.commons.interfaces.Parser;

public class NonceParser implements Parser<Long> {

	@Override
	public Long parse(String string) {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException exception) {
			System.out.println("Invalid nunce value.");
			return null;
		}
	}

}
