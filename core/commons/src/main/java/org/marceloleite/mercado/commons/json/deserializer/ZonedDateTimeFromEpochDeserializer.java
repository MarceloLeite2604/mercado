package org.marceloleite.mercado.commons.json.deserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.EpochSecondsToZonedDateTimeConveter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ZonedDateTimeFromEpochDeserializer extends StdDeserializer<ZonedDateTime> {

	public ZonedDateTimeFromEpochDeserializer() {
		super(ZonedDateTime.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		return EpochSecondsToZonedDateTimeConveter.getInstance().convertFrom(parser.readValueAs(Long.class));
	}

}
