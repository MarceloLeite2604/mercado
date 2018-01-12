package org.marceloleite.mercado.negotiationapi.listorders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.converter.json.JsonListOrdersResponseToListOrders;
import org.marceloleite.mercado.jsonmodel.JsonListOrdersResponse;
import org.marceloleite.mercado.jsonmodel.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.AbstractTapiMethod;
import org.marceloleite.mercado.negotiationapi.HttpConnection;
import org.marceloleite.mercado.negotiationapi.TapiMethod;
import org.marceloleite.mercado.negotiationapi.TapiMethodParameters;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.Order;
import org.marceloleite.mercado.negotiationapi.model.OrderStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListOrdersMethod extends AbstractTapiMethod {

	private static final Logger LOGGER = LogManager.getLogger(ListOrdersMethod.class);

	public ListOrdersMethodResponse execute(CurrencyPair coinPair) {
		return execute(coinPair, null, null, null, null, null, null);
	}

	public ListOrdersMethodResponse execute(CurrencyPair coinPair, OrderType orderType, List<OrderStatus> orderStatusList,
			Boolean hasFills, Long fromId, Long toId, LocalDateTime toTimestamp) {
		try {
			HttpConnection httpConnection = new HttpConnection();
			UrlGenerator urlGenerator = new UrlGenerator();

			TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters(coinPair, orderType,
					orderStatusList, hasFills, fromId, toId, toTimestamp);
			URL url = urlGenerator.generate(generateAddress(), tapiMethodParameters);
			LOGGER.debug("URL generated is: \"" + url + "\".");
			HttpsURLConnection httpsUrlConnection = httpConnection.createHttpsUrlConnection(url);

			httpsUrlConnection.connect();
			sendHttpUrlConnectionProperties(httpsUrlConnection, tapiMethodParameters);
			String response = readHttpUrlConnectionResponse(httpsUrlConnection);
			LOGGER.debug("Response received: " + response);
			return createListOrdersMethodResponse(response);
			
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	private ListOrdersMethodResponse createListOrdersMethodResponse(String response) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonTapiResponse jsonTapiResponse = objectMapper.readValue(response, JsonTapiResponse.class);
		List<Order> orders = null;
		Long statusCode = jsonTapiResponse.getStatusCode();
		long longTimestamp = Long.parseLong(jsonTapiResponse.getServerUnixTimestamp());
		LocalDateTime timestamp = new LongToLocalDateTimeConverter().convertTo(longTimestamp);
		if (statusCode == STATUS_OK) {
			Object object = jsonTapiResponse.getAdditionalProperties().get("response_data");
			String convertTo = new ObjectToJsonConverter().convertTo(((LinkedHashMap<?, ?>)object));
			JsonListOrdersResponse jsonListOrdersResponse = objectMapper
					.readValue(convertTo, JsonListOrdersResponse.class);
			orders = new JsonListOrdersResponseToListOrders().convertTo(jsonListOrdersResponse);
		}
		return new ListOrdersMethodResponse(statusCode, timestamp, orders);
	}

	private void sendHttpUrlConnectionProperties(HttpsURLConnection httpsUrlConnection,
			TapiMethodParameters tapiMethodParameters) throws IOException {
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsUrlConnection.getOutputStream());
		outputStreamWriter.write(tapiMethodParameters.toUrlParametersString());
		outputStreamWriter.flush();
		outputStreamWriter.close();
	}

	private String readHttpUrlConnectionResponse(HttpsURLConnection httpsUrlConnection) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsUrlConnection.getInputStream()));
		StringBuffer stringBuffer = new StringBuffer();
		String buffer;
		while ((buffer = bufferedReader.readLine()) != null) {
			stringBuffer.append(buffer + "\n");
		}
		return stringBuffer.toString();
	}

	@Override
	protected TapiMethod getTapiMethod() {
		return TapiMethod.LIST_ORDER;
	}

	private TapiMethodParameters generateTapiMethodParameters(CurrencyPair coinPair, OrderType orderType,
			List<OrderStatus> orderStatusList, Boolean hasFills, Long fromId, Long toId, LocalDateTime toTimestamp) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters();
		tapiMethodParameters.put(ListOrdersParameters.COIN_PAIR.toString(), coinPair);

		Long orderTypeValue;
		if (orderType == null) {
			orderTypeValue = null;
		} else {
			orderTypeValue = orderType.getValue();
		}
		tapiMethodParameters.put(ListOrdersParameters.ORDER_TYPE.toString(), orderTypeValue);

		tapiMethodParameters.put(ListOrdersParameters.STATUS_LIST.toString(),
				generateOrderStatusListParameterValue(orderStatusList));
		tapiMethodParameters.put(ListOrdersParameters.HAS_FILLS.toString(), hasFills);
		tapiMethodParameters.put(ListOrdersParameters.FROM_ID.toString(), fromId);
		tapiMethodParameters.put(ListOrdersParameters.TO_ID.toString(), toId);
		tapiMethodParameters.put(ListOrdersParameters.TO_ID.toString(), toTimestamp);

		return tapiMethodParameters;
	}

	private String generateOrderStatusListParameterValue(List<OrderStatus> orderStatusList) {
		String result = null;
		if (orderStatusList != null && !orderStatusList.isEmpty()) {
			result = "[";
			for (OrderStatus orderStatus : orderStatusList) {
				result += orderStatus.getValue() + ",";
			}
			result = result.substring(0, result.length() - 1);
			result = "]";
		}
		return result;
	}
}
