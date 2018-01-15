package org.marceloleite.mercado.negotiationapi.model.listsystemmessages;

import java.time.LocalDateTime;

public class SystemMessage {
	
	private LocalDateTime time;
	
	private SystemMessageLevel systemMessageLevel;
	
	private long eventCode;
	
	private String messageContent;
	
	

	public SystemMessage(LocalDateTime time, SystemMessageLevel systemMessageLevel, long eventCode,
			String messageContent) {
		super();
		this.time = time;
		this.systemMessageLevel = systemMessageLevel;
		this.eventCode = eventCode;
		this.messageContent = messageContent;
	}
	
	public SystemMessage() {
		this(null, null, 0l, null);
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime msgDate) {
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
