package org.marceloleite.mercado.consultant.thread.property;

import java.time.Duration;
import java.time.ZonedDateTime;

public class ConsultantThreadProperties {

	private ZonedDateTime startTime;

	private ZonedDateTime endTime;

	private Duration tradeRetrieveDuration;

	private Duration timeInterval;

	private boolean databaseValuesIgnored;

	private Duration tradesSiteRetrieverStepDuration;

	public ConsultantThreadProperties(ZonedDateTime startTime, ZonedDateTime endTime, Duration tradeRetrieveDuration,
			Duration timeInterval, boolean databaseValuesIgnored, Duration tradesSiteRetrieverStepDuration) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.tradeRetrieveDuration = tradeRetrieveDuration;
		this.timeInterval = timeInterval;
		this.databaseValuesIgnored = databaseValuesIgnored;
		this.tradesSiteRetrieverStepDuration = tradesSiteRetrieverStepDuration;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	public ZonedDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(ZonedDateTime endTime) {
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
