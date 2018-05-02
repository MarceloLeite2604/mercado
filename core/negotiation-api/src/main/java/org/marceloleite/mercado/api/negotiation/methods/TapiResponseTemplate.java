package org.marceloleite.mercado.api.negotiation.methods;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.api.negotiation.methods.response.NapiResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;

public abstract class TapiResponseTemplate<T, T2> {

	private long statusCode;

	private String errorMessage;

	private ZonedDateTime timestamp;

	private String responseData;

	private T2 response;

	public TapiResponseTemplate(NapiResponse napiResponse, Converter<T, T2> converter) {
		super();
		this.statusCode = napiResponse.getStatusCode();
		this.errorMessage = napiResponse.getErrorMessage();
		if (errorMessage != null) {
			throw new RuntimeException("Error while retrieving TAPI response: " + statusCode + " - " + errorMessage);
		}
		long longTimestamp = Long.parseLong(napiResponse.getServerUnixTimestamp());
		this.timestamp = LongToZonedDateTimeConverter.getInstance().convertTo(longTimestamp);
		this.responseData = napiResponse.getResponseData();
		T jsonResponseData = getJsonResponseData();
		this.response = converter.convertTo(jsonResponseData);
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
			result = new ObjectToJsonConverter().convertFromToObject(responseData, result);
		}
		return result;
	}
}
