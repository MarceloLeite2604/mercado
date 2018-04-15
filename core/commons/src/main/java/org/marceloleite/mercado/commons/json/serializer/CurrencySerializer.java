package org.marceloleite.mercado.commons.json.serializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.Currency;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CurrencySerializer extends StdSerializer<Currency> {
	
	public CurrencySerializer() {
		super(Currency.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(Currency currency, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		generator.writeString(currency.getAcronym());
	}
}
