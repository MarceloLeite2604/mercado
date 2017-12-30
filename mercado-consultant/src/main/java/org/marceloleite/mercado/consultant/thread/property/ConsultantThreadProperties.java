package org.marceloleite.mercado.consultant.thread.property;

import java.time.Duration;
import java.time.LocalDateTime;

public class ConsultantThreadProperties {

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private Duration tradeRetrieveDuration;

	private Duration timeInterval;

	private boolean databaseValuesIgnored;

	private Duration tradesSiteRetrieverStepDuration;

	public ConsultantThreadProperties(LocalDateTime startTime, LocalDateTime endTime, Duration tradeRetrieveDuration,
			Duration timeInterval, boolean databaseValuesIgnored, Duration tradesSiteRetrieverStepDuration) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.tradeRetrieveDuration = tradeRetrieveDuration;
		this.timeInterval = timeInterval;
		this.databaseValuesIgnored = databaseValuesIgnored;
		this.tradesSiteRetrieverStepDuration = tradesSiteRetrieverStepDuration;
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

	public boolean isDatabaseValuesIgnored() {
		return databaseValuesIgnored;
	}

	public void setDatabaseValuesIgnored(boolean databaseValuesIgnored) {
		this.databaseValuesIgnored = databaseValuesIgnored;
	}

	public Duration getTradesSiteRetrieverStepDuration() {
		return tradesSiteRetrieverStepDuration;
	}

	public void setTradesSiteRetrieverStepDuration(Duration tradesSiteRetrieverStepDuration) {
		this.tradesSiteRetrieverStepDuration = tradesSiteRetrieverStepDuration;
	}

}
