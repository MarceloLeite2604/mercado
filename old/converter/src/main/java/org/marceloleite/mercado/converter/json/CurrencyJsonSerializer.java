package org.marceloleite.mercado.converter.json;

import java.io.IOException;

import org.marceloleite.mercado.commons.Currency;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CurrencyJsonSerializer extends StdSerializer<Currency> {

	private static final long serialVersionUID = 1L;

	public CurrencyJsonSerializer() {
		super(Currency.class);
	}

	@Override
	public void serialize(Currency currency, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeStartObject();
		generator.writeFieldName("currency");
		generator.writeString(currency.getAcronym());
		generator.writeEndObject();
	}

}
