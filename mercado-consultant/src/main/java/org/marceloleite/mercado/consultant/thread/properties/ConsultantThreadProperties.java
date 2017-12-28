package org.marceloleite.mercado.consultant.thread.properties;

import java.time.Duration;
import java.time.LocalDateTime;

public class ConsultantThreadProperties {

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private Duration tradeRetrieveDuration;

	private Duration timeInterval;

	public ConsultantThreadProperties(LocalDateTime startTime, LocalDateTime endTime, Duration tradeRetrieveDuration,
			Duration timeInterval) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.tradeRetrieveDuration = tradeRetrieveDuration;
		this.timeInterval = timeInterval;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Duration getTradeRetrieveDuration() {
		return tradeRetrieveDuration;
	}

	public void setTradeRetrieveDuration(Duration tradeRetrieveDuration) {
		this.tradeRetrieveDuration = tradeRetrieveDuration;
	}

	public Duration getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(Duration timeInterval) {
		this.timeInterval = timeInterval;
	}

}
