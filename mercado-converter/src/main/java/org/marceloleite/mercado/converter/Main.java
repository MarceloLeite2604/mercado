package org.marceloleite.mercado.converter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.converter.json.CurrencyJsonSerializer;
import org.marceloleite.mercado.converter.json.OrderbookConverter;
import org.marceloleite.mercado.converter.json.TickerConverter;
import org.marceloleite.mercado.converter.json.getaccountinfo.JsonBalanceDeserializer;
import org.marceloleite.mercado.converter.json.getaccountinfo.JsonWithdrawalLimitsDeserializer;
import org.marceloleite.mercado.databasemodel.Orderbook;
import org.marceloleite.mercado.databasemodel.TickerPO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrderbook;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTicker;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonAccountInfo;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonBalance;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonCurrencyAvailable;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonWithdrawalLimits;

public class Main {

	public static void main(String[] args) {
		// ticker();
		// jsonBalanceDeserializer();
		jsonGetAccountInfoDeserializer();
	}

	@SuppressWarnings("unused")
	private static void ticker() {
		JsonTicker jsonTicker = null;
		TickerPO ticker = new TickerConverter().convertTo(jsonTicker);
		System.out.println(new ObjectToJsonConverter().convertTo(ticker));
	}

	@SuppressWarnings("unused")
	private static void orderbook() {
		JsonOrderbook jsonOrderbook = null;
		Orderbook orderbook = new OrderbookConverter().convertTo(jsonOrderbook);
		System.out.println(new ObjectToJsonConverter().convertTo(orderbook));
	}

	@SuppressWarnings("unused")
	private static void jsonBalanceDeserializer() {
		JsonBalance jsonBalance = new JsonBalance();

		JsonCurrencyAvailable jsonCurrencyBalance = new JsonCurrencyAvailable(123.456, 234.567, null);
		jsonBalance.put(Currency.BITCOIN, jsonCurrencyBalance);
		jsonCurrencyBalance = new JsonCurrencyAvailable(567.123, 879.345, 3l);
		jsonBalance.put(Currency.BCASH, jsonCurrencyBalance);

		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter(JsonBalance.class);
		objectToJsonConverter.addSerializer(Currency.class, new CurrencyJsonSerializer());
		objectToJsonConverter.addDeserializer(JsonBalance.class, new JsonBalanceDeserializer());

		String string = "{\n" + "\"BCASH\" : {\n" + "\"available\" : 567.123,\n" + "\"total\" : 879.345,\n"
				+ "\"amount_open_ordes\" : 3\n" + "},\n" + "\"BITCOIN\" : {\n" + "\"available\" : 123.456,\n"
				+ "\"total\" : 234.567\n" + "}\n" + "}";

		JsonBalance deserializedJsonBalance = objectToJsonConverter.convertFromToObject(string, JsonBalance.class);
		System.out.println(new ObjectToJsonConverter().convertTo(deserializedJsonBalance));
	}

	@SuppressWarnings("unused")
	private static void jsonGetAccountInfoDeserializer() {
		String string = "{\n" + "\"balance\": {\n" + "\"brl\": {\n" + "\"available\": \"3000.00000\",\n"
				+ "\"total\": \"4900.00000\"\n" + "},\n" + "\"btc\": {\n" + "\"available\": \"10.00000000\",\n"
				+ "\"total\": \"11.00000000\",\n" + "\"amount_open_orders\": 3\n" + "},\n" + "\"ltc\": {\n"
				+ "\"available\": \"500.00000000\",\n" + "\"total\": \"500.00000000\",\n"
				+ "\"amount_open_orders\": 0\n" + "},\n" + "\"bch\": {\n" + "\"available\": \"5.00000000\",\n"
				+ "\"total\": \"6.00000000\",\n" + "\"amount_open_orders\": 1\n" + "}\n" + "},\n"
				+ "\"withdrawal_limits\": {\n" + "\"brl\": {\n" + "\"available\": \"988.00\",\n"
				+ "\"total\": \"20000.00\"\n" + "},\n" + "\"btc\": {\n" + "\"available\": \"3.76600000\",\n"
				+ "\"total\": \"5.00000000\"\n" + "},\n" + "\"ltc\": {\n" + "\"available\": \"500.00000000\",\n"
				+ "\"total\": \"500.00000000\"\n" + "},\n" + "\"bch\": {\n" + "\"available\": \"2.00000000\",\n"
				+ "\"total\": \"2.00000000\"\n" + "}\n" + "}\n" + "}\n";
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter(JsonAccountInfo.class);
		objectToJsonConverter.addDeserializer(JsonBalance.class,
				new JsonBalanceDeserializer());
		objectToJsonConverter.addDeserializer(JsonWithdrawalLimits.class,
				new JsonWithdrawalLimitsDeserializer());
		JsonAccountInfo jsonGetAccountInfo = objectToJsonConverter.convertFromToObject(string,
				JsonAccountInfo.class);
		System.out.println(new ObjectToJsonConverter().convertTo(jsonGetAccountInfo));
	}
}
