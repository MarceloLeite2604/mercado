package org.marceloleite.mercado.commons.json.serializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.TradeType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class TradeTypeSerializer extends StdSerializer<TradeType> {

	public TradeTypeSerializer() {
		super(TradeType.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(TradeType tradeType, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		generator.writeString(tradeType.getValue());
	}

}
