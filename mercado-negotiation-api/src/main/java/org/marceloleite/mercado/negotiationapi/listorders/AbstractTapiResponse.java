package org.marceloleite.mercado.negotiationapi.listorders;

import java.time.LocalDateTime;

public abstract class AbstractTapiResponse {

	private long statusCode;
	
	private LocalDateTime timestamp;
	
	public AbstractTapiResponse(long statusCode, LocalDateTime timestamp) {
		super();
		this.statusCode = statusCode;
		this.timestamp = timestamp;
	}

	public long getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(long statusCode) {
		this.statusCode = statusCode;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
