package org.marceloleite.mercado.jsonmodel;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "msg_date", "level", "event_code", "msg_content" })
public class JsonSystemMessage {

	@JsonProperty("msg_date")
	private String msgDate;
	@JsonProperty("level")
	private String level;
	@JsonProperty("event_code")
	private Long eventCode;
	@JsonProperty("msg_content")
	private String msgContent;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("msg_date")
	public String getMsgDate() {
		return msgDate;
	}

	@JsonProperty("msg_date")
	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	@JsonProperty("level")
	public String getLevel() {
		return level;
	}

	@JsonProperty("level")
	public void setLevel(String level) {
		this.level = level;
	}

	@JsonProperty("event_code")
	public Long getEventCode() {
		return eventCode;
	}

	@JsonProperty("event_code")
	public void setEventCode(Long eventCode) {
		this.eventCode = eventCode;
	}

	@JsonProperty("msg_content")
	public String getMsgContent() {
		return msgContent;
	}

	@JsonProperty("msg_content")
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
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
