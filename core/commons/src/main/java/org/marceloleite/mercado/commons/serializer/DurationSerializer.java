package org.marceloleite.mercado.commons.serializer;

import java.io.IOException;
import java.time.Duration;

import org.marceloleite.mercado.commons.converter.DurationToStringFormatConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DurationSerializer extends StdSerializer<Duration> {

	private static final long serialVersionUID = 1L;

	public DurationSerializer() {
		super(Duration.class);
	}

	@Override
	public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializer)
			throws IOException {
		jsonGenerator.writeNumber(Long.parseLong(new DurationToStringFormatConverter().convertTo(duration)));
	}

}
