package org.marceloleite.mercado.api.negotiation.serializer;

import java.io.IOException;

import org.marceloleite.mercado.api.negotiation.model.WithdrawalStatus;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class WithdrawalStatusDeserializer extends StdDeserializer<WithdrawalStatus> {

	private static final long serialVersionUID = 1L;

	public WithdrawalStatusDeserializer() {
		this(null);
	}

	public WithdrawalStatusDeserializer(Class<WithdrawalStatus> clazz) {
		super(clazz);
	}

	@Override
	public WithdrawalStatus deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		return WithdrawalStatus.getByValue(jsonParser.readValueAs(Long.class));
	}

}
