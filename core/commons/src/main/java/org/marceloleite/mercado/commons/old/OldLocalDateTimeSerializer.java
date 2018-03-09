package org.marceloleite.mercado.commons.old;

import java.io.IOException;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.converter.LocalDateTimeToStringConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class OldLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OldLocalDateTimeSerializer() {
		this(null);
	}

	public OldLocalDateTimeSerializer(Class<LocalDateTime> clazz) {
		super(clazz);
	}

	@Override
	public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializer)
			throws IOException {
		jsonGenerator.writeString(new LocalDateTimeToStringConverter().convertTo(localDateTime));
	}

}
