package org.marceloleite.mercado.commons.deserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

	private static final long serialVersionUID = 1L;

	public ZonedDateTimeDeserializer() {
		this(null);
	}

	public ZonedDateTimeDeserializer(Class<ZonedDateTime> clazz) {
		super(clazz);
	}

	@Override
	public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
		/*System.out.println(jsonNode.textValue());*/
		return new ZonedDateTimeToStringConverter().convertFrom(jsonNode.textValue());
	}

}
