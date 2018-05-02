package org.marceloleite.mercado.api.negotiation.serializer;

import java.io.IOException;

import org.marceloleite.mercado.api.negotiation.model.SystemMessageLevel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class SystemMessageLevelDeserializer extends StdDeserializer<SystemMessageLevel> {

	private static final long serialVersionUID = 1L;

	public SystemMessageLevelDeserializer() {
		this(null);
	}

	public SystemMessageLevelDeserializer(Class<SystemMessageLevel> clazz) {
		super(clazz);
	}

	@Override
	public SystemMessageLevel deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		return SystemMessageLevel.getByName(jsonParser.readValueAs(String.class));
	}

}
