package org.marceloleite.mercado.jsonmodel;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "response_data", "status_code", "error_message", "server_unix_timestamp" })
public class JsonTapiResponse {

	@JsonProperty("status_code")
	private Long statusCode;

	@JsonProperty("error_message")
	private String errorMessage;

	@JsonProperty("server_unix_timestamp")
	private String serverUnixTimestamp;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("status_code")
	public Long getStatusCode() {
		return statusCode;
	}

	@JsonProperty("status_code")
	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}

	@JsonProperty("error_message")
	public String getErrorMessage() {
		return errorMessage;
	}

	@JsonProperty("error_message")
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@JsonProperty("server_unix_timestamp")
	public String getServerUnixTimestamp() {
		return serverUnixTimestamp;
	}

	@JsonProperty("server_unix_timestamp")
	public void setServerUnixTimestamp(String serverUnixTimestamp) {
		this.serverUnixTimestamp = serverUnixTimestamp;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}