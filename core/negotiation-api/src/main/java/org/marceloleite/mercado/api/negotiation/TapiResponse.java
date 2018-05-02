package org.marceloleite.mercado.api.negotiation;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TapiResponse<T> {
	
	@JsonProperty("status_code")
	private Long statusCode;
	
	@JsonProperty("error_message")
	private String errorMessage;
	
	@JsonProperty("server_unix_timestamp")
	private LocalDateTime timestamp;
	
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

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public T getResponseData() {
		return responseData;
	}

	public void setResponseData(T responseData) {
		this.responseData = responseData;
	}
}
