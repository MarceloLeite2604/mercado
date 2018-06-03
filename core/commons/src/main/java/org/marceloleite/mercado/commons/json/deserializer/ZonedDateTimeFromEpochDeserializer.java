package org.marceloleite.mercado.commons.json.deserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ZonedDateTimeFromEpochDeserializer extends StdDeserializer<ZonedDateTime> {

	private static final long serialVersionUID = 1L;

	public ZonedDateTimeFromEpochDeserializer() {
		super(ZonedDateTime.class);
	}

	@Override
	public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		return ZonedDateTimeUtils.convertFromEpochTime(jsonParser.getValueAsLong());
	}

}
