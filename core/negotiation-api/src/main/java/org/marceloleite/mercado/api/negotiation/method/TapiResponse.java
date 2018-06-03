package org.marceloleite.mercado.api.negotiation.method;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.json.deserializer.ZonedDateTimeFromEpochDeserializer;
import org.marceloleite.mercado.commons.json.serializer.ZonedDateTimeToEpochSerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TapiResponse<T> {
	
	@JsonProperty("status_code")
	private Long statusCode;
	
	@JsonProperty("error_message")
	private String errorMessage;
	
	@JsonProperty("server_unix_timestamp")
	@JsonDeserialize(using = ZonedDateTimeFromEpochDeserializer.class)
	@JsonSerialize(using = ZonedDateTimeToEpochSerializer.class)
	private ZonedDateTime timestamp;
	
	@JsonProperty("response_data")
	private T responseData;

	public Long getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public T getResponseData() {
		return responseData;
	}

	public void setResponseData(T responseData) {
		this.responseData = responseData;
	}
}
