package org.marceloleite.mercado.commons.json.deserializer;

import java.io.IOException;
import java.time.LocalTime;

import org.marceloleite.mercado.commons.utils.LocalTimeUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {

	private static final long serialVersionUID = 1L;

	public LocalTimeDeserializer() {
		super(LocalTime.class);
	}

	@Override
	public LocalTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		return LocalTimeUtils.parse(jsonParser.getText());
	}

}
