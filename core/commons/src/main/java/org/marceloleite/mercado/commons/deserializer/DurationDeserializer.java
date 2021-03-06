package org.marceloleite.mercado.commons.deserializer;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.DurationToStringFormatConverter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DurationDeserializer extends StdDeserializer<Duration> {

	private static final long serialVersionUID = 1L;

	public DurationDeserializer() {
		this(null);
	}

	public DurationDeserializer(Class<ZonedDateTime> clazz) {
		super(clazz);
	}

	@Override
	public Duration deserialize(JsonParser jsonParser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
		return new DurationToStringFormatConverter().convertFrom(jsonNode.textValue());
	}

}
