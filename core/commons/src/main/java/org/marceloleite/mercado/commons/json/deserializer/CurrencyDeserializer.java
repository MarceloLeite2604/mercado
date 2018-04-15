package org.marceloleite.mercado.commons.json.deserializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.Currency;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CurrencyDeserializer extends StdDeserializer<Currency> {
	
	public CurrencyDeserializer() {
		super(Currency.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public Currency deserialize(JsonParser parser, DeserializationContext context)
			throws IOException {
		return Currency.getByAcronym(parser.readValueAs(String.class));
	}

}
