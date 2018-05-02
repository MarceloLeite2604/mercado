package org.marceloleite.mercado.api.negotiation.serializer;

import java.io.IOException;

import org.marceloleite.mercado.api.negotiation.methods.listsystemmessages.response.SystemMessageLevel;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class SystemMessageLevelSerializer extends StdSerializer<SystemMessageLevel> {

	private static final long serialVersionUID = 1L;
	
	public SystemMessageLevelSerializer() {
		this(null);
	}

	public SystemMessageLevelSerializer(Class<SystemMessageLevel> clazz) {
		super(clazz);
	}

	@Override
	public void serialize(SystemMessageLevel systemMessageLevel, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		jsonGenerator.writeString(systemMessageLevel.toString());
	}

}
