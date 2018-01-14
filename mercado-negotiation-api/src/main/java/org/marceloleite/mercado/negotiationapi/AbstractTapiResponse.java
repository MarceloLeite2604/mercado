package org.marceloleite.mercado.negotiationapi;

import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.converter.json.JsonToClassObjectConverter;
import org.marceloleite.mercado.jsonmodel.JsonTapiResponse;

public abstract class AbstractTapiResponse<T, T2> {

	private long statusCode;

	private String errorMessage;

	private LocalDateTime timestamp;

	private String responseData;

	public AbstractTapiResponse(JsonTapiResponse jsonTapiResponse) {
		super();
		this.statusCode = jsonTapiResponse.getStatusCode();
		this.errorMessage = jsonTapiResponse.getErrorMessage();
		long longTimestamp = Long.parseLong(jsonTapiResponse.getServerUnixTimestamp());
		this.timestamp = new LongToLocalDateTimeConverter().convertTo(longTimestamp);
		this.responseData = jsonTapiResponse.getResponseData();
	}

	public long getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(long statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	protected T getJsonResponseData() {
		T result = null;
		if (responseData != null || !responseData.isEmpty()) {
			result = new JsonToClassObjectConverter<T>(getJsonResponseDataClass()).convertTo(responseData);
		}
		return result;
	}

	protected T2 getFormattedResponseData() {
		return getConverter().convertTo(getJsonResponseData());
	}

	protected abstract Class<?> getJsonResponseDataClass();

	protected abstract Converter<T, T2> getConverter();
}
