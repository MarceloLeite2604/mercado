package org.marceloleite.mercado.strategies.sixth.deser;

import java.io.IOException;

import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class SixthStrategyStatusSerializer extends StdSerializer<SixthStrategyStatus> {

	private static final long serialVersionUID = 1L;

	protected SixthStrategyStatusSerializer() {
		super(SixthStrategyStatus.class);
	}

	@Override
	public void serialize(SixthStrategyStatus sixthStrategyStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeString(sixthStrategyStatus.getName());
	}

}
