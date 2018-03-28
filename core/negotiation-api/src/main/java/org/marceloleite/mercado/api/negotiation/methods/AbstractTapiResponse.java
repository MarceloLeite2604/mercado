package org.marceloleite.mercado.api.negotiation.methods;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.converter.json.JsonToClassObjectConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public abstract class AbstractTapiResponse<T, T2> {

	private long statusCode;

	private String errorMessage;

	private ZonedDateTime timestamp;

	private String responseData;

	private Class<?> jsonResponseDataClass;

	private T2 response;

	public AbstractTapiResponse(JsonTapiResponse jsonTapiResponse, Class<?> jsonResponseDataClass,
			Converter<T, T2> jsonTapiResponseConverter) {
		super();
		this.statusCode = jsonTapiResponse.getStatusCode();
		this.errorMessage = jsonTapiResponse.getErrorMessage();
		if (errorMessage != null) {
			throw new RuntimeException("Error while retrieving TAPI response: " + statusCode + " - " + errorMessage);
		}
		long longTimestamp = Long.parseLong(jsonTapiResponse.getServerUnixTimestamp());
		this.timestamp = new LongToZonedDateTimeConverter().convertTo(longTimestamp);
		this.responseData = jsonTapiResponse.getResponseData();
		this.jsonResponseDataClass = jsonResponseDataClass;
		T jsonResponseData = getJsonResponseData();
		this.response = jsonTapiResponseConverter.convertTo(jsonResponseData);
	}

	public long getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	protected String getResponseData() {
		return responseData;
	}

	public T2 getResponse() {
		return response;
	}

	protected T getJsonResponseData() {
		T result = null;
		if (responseData != null || !responseData.isEmpty()) {
			result = new JsonToClassObjectConverter<T>(jsonResponseDataClass).convertTo(responseData);
		}
		return result;
	}
}
