package org.marceloleite.mercado.commons.deserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {
	
	private static final ZonedDateTimeToStringConverter CONVERTER = new ZonedDateTimeToStringConverter();

	public ZonedDateTimeDeserializer() {
		super(ZonedDateTime.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context)
			throws IOException {
		return CONVERTER.convertFrom(parser.readValueAs(String.class));
	}

}
