package org.marceloleite.mercado.commons.json.deserializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.TradeType;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class TradeTypeDeserializer extends StdDeserializer<TradeType> {
	
	public TradeTypeDeserializer() {
		super(TradeType.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public TradeType deserialize(JsonParser parser, DeserializationContext context)
			throws IOException {
		return TradeType.getByValue(parser.readValueAs(String.class));
	}
}
