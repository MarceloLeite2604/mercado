package org.marceloleite.mercado.jsonmodel.api.negotiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.jsonmodel.api.data.JsonSystemMessage;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "messages" })
public class JsonListSystemMessagesResponse {

	@JsonProperty("messages")
	private List<JsonSystemMessage> messages = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("messages")
	public List<JsonSystemMessage> getMessages() {
		return messages;
	}

	@JsonProperty("messages")
	public void setMessages(List<JsonSystemMessage> messages) {
		this.messages = messages;
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
