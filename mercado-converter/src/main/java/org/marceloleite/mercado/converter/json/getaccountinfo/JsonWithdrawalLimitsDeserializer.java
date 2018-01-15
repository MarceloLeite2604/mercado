package org.marceloleite.mercado.converter.json.getaccountinfo;

import java.io.IOException;
import java.util.Iterator;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.json.JsonToClassObjectConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonBalance;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonCurrencyAvailable;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonWithdrawalLimits;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JsonWithdrawalLimitsDeserializer extends StdDeserializer<JsonWithdrawalLimits> {

	private static final long serialVersionUID = 1L;

	public JsonWithdrawalLimitsDeserializer() {
		super(JsonBalance.class);
	}

	protected JsonWithdrawalLimitsDeserializer(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public JsonWithdrawalLimits deserialize(JsonParser jsonParser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		JsonWithdrawalLimits jsonWithdrawalLimits = null;
		JsonNode jsonRootNode = jsonParser.getCodec().readTree(jsonParser);
		if (jsonRootNode != null) {
			jsonWithdrawalLimits = new JsonWithdrawalLimits();
			Iterator<String> fieldNames = jsonRootNode.fieldNames();
			while (fieldNames.hasNext()) {
				String fieldName = fieldNames.next();
				Currency currency = Currency.getByAcronym(fieldName);
				JsonNode jsonNode = jsonRootNode.get(fieldName);
				JsonCurrencyAvailable jsonCurrencyBalance = new JsonToClassObjectConverter<JsonCurrencyAvailable>(
						JsonCurrencyAvailable.class).convertTo(jsonNode.toString());
				jsonWithdrawalLimits.put(currency, jsonCurrencyBalance);
			}
		}

		return jsonWithdrawalLimits;
	}

}
