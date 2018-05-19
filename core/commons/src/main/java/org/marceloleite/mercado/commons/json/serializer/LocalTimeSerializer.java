package org.marceloleite.mercado.commons.json.serializer;

import java.io.IOException;
import java.time.LocalTime;

import org.marceloleite.mercado.commons.utils.LocalTimeUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalTimeSerializer extends StdSerializer<LocalTime> {

	public LocalTimeSerializer() {
		super(LocalTime.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(LocalTime localTime, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		generator.writeString(LocalTimeUtils.format(localTime));
	}

}
