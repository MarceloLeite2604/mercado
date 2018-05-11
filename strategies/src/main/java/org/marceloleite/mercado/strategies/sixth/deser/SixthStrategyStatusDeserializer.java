package org.marceloleite.mercado.strategies.sixth.deser;

import java.io.IOException;

import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class SixthStrategyStatusDeserializer extends StdDeserializer<SixthStrategyStatus> {

	private static final long serialVersionUID = 1L;

	protected SixthStrategyStatusDeserializer() {
		super(SixthStrategyStatus.class);
	}

	@Override
	public SixthStrategyStatus deserialize(JsonParser jsonParser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		return SixthStrategyStatus.findByName(jsonParser.getText());
	}

}
