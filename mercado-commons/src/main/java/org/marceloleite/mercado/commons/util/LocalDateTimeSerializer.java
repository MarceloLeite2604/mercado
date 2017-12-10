package org.marceloleite.mercado.commons.util;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LocalDateTimeSerializer() {
		this(null);
	}

	public LocalDateTimeSerializer(Class<LocalDateTime> clazz) {
		super(clazz);
	}

	@Override
	public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializer)
			throws IOException {
		/*jsonGenerator.writeStartObject();*/
		jsonGenerator.writeString(new LocalDateTimeToString().format(localDateTime));
		/*jsonGenerator.writeEndObject();*/
	}

}
