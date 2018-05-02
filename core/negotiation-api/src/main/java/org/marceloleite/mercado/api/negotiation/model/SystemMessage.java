package org.marceloleite.mercado.api.negotiation.model;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.json.deserializer.CurrencyDeserializer;
import org.marceloleite.mercado.commons.json.serializer.CurrencySerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SystemMessage {

	@JsonProperty("msg_date")
	private ZonedDateTime time;

	@JsonProperty("level")
	@JsonSerialize(using = CurrencySerializer.class)
	@JsonDeserialize(using = CurrencyDeserializer.class)
	private SystemMessageLevel systemMessageLevel;

	@JsonProperty("event_code")
	private Long eventCode;

	@JsonProperty("msg_content")
	private String messageContent;

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime msgDate) {
		this.time = msgDate;
	}

	public SystemMessageLevel getSystemMessageLevel() {
		return systemMessageLevel;
	}

	public void setSystemMessageLevel(SystemMessageLevel systemMessageLevel) {
		this.systemMessageLevel = systemMessageLevel;
	}

	public long getEventCode() {
		return eventCode;
	}

	public void setEventCode(long eventCode) {
		this.eventCode = eventCode;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
}
