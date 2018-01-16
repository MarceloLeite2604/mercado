package org.marceloleite.mercado.converter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.converter.json.CurrencyJsonSerializer;
import org.marceloleite.mercado.converter.json.JsonToClassObjectConverter;
import org.marceloleite.mercado.converter.json.api.data.OrderbookConverter;
import org.marceloleite.mercado.converter.json.api.data.TickerConverter;
import org.marceloleite.mercado.converter.json.api.negotiation.getaccountinfo.JsonBalanceDeserializer;
import org.marceloleite.mercado.converter.json.api.negotiation.getaccountinfo.JsonWithdrawalLimitsDeserializer;
import org.marceloleite.mercado.databasemodel.Orderbook;
import org.marceloleite.mercado.databasemodel.TickerPO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrderbook;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTicker;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonGetOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonAccountInfo;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonBalance;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonCurrencyAvailable;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonWithdrawalLimits;

public class Main {

	public static void main(String[] args) {
		// ticker();
		// jsonBalanceDeserializer();
		//jsonGetAccountInfoDeserializer();
		jsonGetOrderResponseConverter();
	}

	@SuppressWarnings("unused")
	private static void jsonGetOrderResponseConverter() {
		String string = "{\"order\":{\n" + "\"order_id\":1024453,\n" + "\"coin_pair\":\"BRLBCH\",\n"
				+ "\"order_type\":1,\n" + "\"status\":4,\n" + "\"has_fills\":true,\n" + "\"quantity\":\"0.01160766\",\n"
				+ "\"limit_price\":\"8615.00078\",\n" + "\"executed_quantity\":\"0.01160766\",\n"
				+ "\"executed_price_avg\":\"8615.00078\",\n" + "\"fee\":\"0.00008125\",\n"
				+ "\"created_timestamp\":\"1515266654\",\n" + "\"updated_timestamp\":\"1515266656\",\n"
				+ "\"operations\":[\n" + "{\n" + "\"operation_id\":418231,\n" + "\"quantity\":\"0.01160766\",\n"
				+ "\"price\":\"8615.00000\",\n" + "\"fee_rate\":\"0.70\",\n" + "\"executed_timestamp\":\"1515266656\"\n"
				+ "}\n" + "]\n" + "}\n}";
		
		JsonGetOrderResponse jsonGetOrderResponse = (JsonGetOrderResponse)new JsonToClassObjectConverter<>(JsonGetOrderResponse.class).convertTo(string);
		System.out.println(new ObjectToJsonConverter().convertTo(jsonGetOrderResponse));

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
		// String string = "{\n" + "\"balance\": {\n" + "\"brl\": {\n" + "\"available\":
		// \"3000.00000\",\n"
		// + "\"total\": \"4900.00000\"\n" + "},\n" + "\"btc\": {\n" + "\"available\":
		// \"10.00000000\",\n"
		// + "\"total\": \"11.00000000\",\n" + "\"amount_open_orders\": 3\n" + "},\n" +
		// "\"ltc\": {\n"
		// + "\"available\": \"500.00000000\",\n" + "\"total\": \"500.00000000\",\n"
		// + "\"amount_open_orders\": 0\n" + "},\n" + "\"bch\": {\n" + "\"available\":
		// \"5.00000000\",\n"
		// + "\"total\": \"6.00000000\",\n" + "\"amount_open_orders\": 1\n" + "}\n" +
		// "},\n"
		// + "\"withdrawal_limits\": {\n" + "\"brl\": {\n" + "\"available\":
		// \"988.00\",\n"
		// + "\"total\": \"20000.00\"\n" + "},\n" + "\"btc\": {\n" + "\"available\":
		// \"3.76600000\",\n"
		// + "\"total\": \"5.00000000\"\n" + "},\n" + "\"ltc\": {\n" + "\"available\":
		// \"500.00000000\",\n"
		// + "\"total\": \"500.00000000\"\n" + "},\n" + "\"bch\": {\n" + "\"available\":
		// \"2.00000000\",\n"
		// + "\"total\": \"2.00000000\"\n" + "}\n" + "}\n" + "}\n";
		String string = "{\n" + "\"balance\" : {\n" + "\"brl\" : {\n" + "\"available\" : \"58.43539\",\n"
				+ "\"total\" : \"58.43539\"\n" + "},\n" + "\"btc\" : {\n" + "\"available\" : \"0.03499164\",\n"
				+ "\"total\" : \"0.03499164\",\n" + "\"amount_open_orders\" : 0\n" + "},\n" + "\"ltc\" : {\n"
				+ "\"available\" : \"1.52655411\",\n" + "\"total\" : \"1.52655411\",\n" + "\"amount_open_orders\" : 0\n"
				+ "},\n" + "\"bch\" : {\n" + "\"available\" : \"0.05967185\",\n" + "\"total\" : \"0.05967185\",\n"
				+ "\"amount_open_orders\" : 0\n" + "},\n" + "\"btg\" : {\n" + "\"available\" : \"0.01470677\",\n"
				+ "\"total\" : \"0.01470677\",\n" + "\"amount_open_orders\" : 0\n" + "}\n" + "},\n"
				+ "\"withdrawal_limits\" : {\n" + "\"brl\" : {\n" + "\"available\" : \"20000.00\",\n"
				+ "\"total\" : \"20000.00\"\n" + "},\n" + "\"btc\" : {\n" + "\"available\" : \"25.00000000\",\n"
				+ "\"total\" : \"25.00000000\"\n" + "},\n" + "\"ltc\" : {\n" + "\"available\" : \"500.00000000\",\n"
				+ "\"total\" : \"500.00000000\"\n" + "},\n" + "\"bch\" : {\n" + "\"available\" : \"25.00000000\",\n"
				+ "\"total\" : \"25.00000000\"\n" + "},\n" + "\"btg\" : {\n" + "\"available\" : 0,\n"
				+ "\"total\" : 0\n" + "}\n" + "}\n" + "}\n";
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter(JsonAccountInfo.class);
		objectToJsonConverter.addDeserializer(JsonBalance.class, new JsonBalanceDeserializer());
		objectToJsonConverter.addDeserializer(JsonWithdrawalLimits.class, new JsonWithdrawalLimitsDeserializer());
		JsonAccountInfo jsonGetAccountInfo = objectToJsonConverter.convertFromToObject(string, JsonAccountInfo.class);
		System.out.println(new ObjectToJsonConverter().convertTo(jsonGetAccountInfo));
	}
}
