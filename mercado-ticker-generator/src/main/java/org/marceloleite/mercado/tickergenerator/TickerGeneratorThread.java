package org.marceloleite.mercado.tickergenerator;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;

public class TickerGeneratorThread extends Thread {

	private static final Logger LOGGER = LogManager.getLogger(TickerGeneratorThread.class);
	private static final String DEFAULT_THREAD_NAME = "Ticker Generator";

	private ZonedDateTime startTime;

	private ZonedDateTime endTime;

	private Duration tickerGeneratorDuration;

	private Duration timeInterval;

	private boolean ignoreTemporalTickersOnDatabase;
	
	private String threadName;

	public TickerGeneratorThread(ZonedDateTime startTime, ZonedDateTime endTime, Duration tickerGeneratorDuration,
			Duration timeInterval, boolean ignoreTemporalTickersOnDatabase) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.tickerGeneratorDuration = tickerGeneratorDuration;
		this.timeInterval = timeInterval;
		this.ignoreTemporalTickersOnDatabase = ignoreTemporalTickersOnDatabase;
		this.threadName = DEFAULT_THREAD_NAME;
	}
	
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public void run() {
		Thread.currentThread().setName(threadName);
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		TimeInterval timeInterval = null;
		ZonedDateTime lastExecution = null;
		while (!finished(timeInterval)) {
			timeInterval = calculateTimeInterval(timeInterval);
			lastExecution = ZonedDateTimeUtils.now();
			for (Currency currency : Currency.values()) {
				/* TODO: Watch out with BGold. */
				if (currency.isDigital() && currency != Currency.BGOLD) {
					LOGGER.info("Retrieving temporal ticker to " + currency + " currency for period between "
							+ timeInterval + ".");
					temporalTickerRetriever.retrieve(currency, timeInterval, ignoreTemporalTickersOnDatabase);
				}
			}
			waitTime(lastExecution, timeInterval);
		}
		LOGGER.info("Finishing execution.");
	}

	private TimeInterval calculateTimeInterval(TimeInterval previousTimeInterval) {
		if (previousTimeInterval == null) {
			return new TimeInterval(startTime, tickerGeneratorDuration);
		} else {
			ZonedDateTime startTime = ZonedDateTime.from(previousTimeInterval.getEnd());
			ZonedDateTime endTime = startTime.plus(tickerGeneratorDuration);
			ZonedDateTime now = ZonedDateTimeUtils.now();
			if (endTime.isAfter(now)) {
				endTime = now;
			}
			return new TimeInterval(startTime, endTime);
		}
	}

	private boolean finished(TimeInterval lastTimeIntervalGenerated) {
		if ( endTime == null || lastTimeIntervalGenerated == null) {
			return false;
		} else {
			return (lastTimeIntervalGenerated.getEnd().isAfter(endTime) || lastTimeIntervalGenerated.equals(endTime));
		}
	}

	private void waitTime(ZonedDateTime lastExecution, TimeInterval previousTimeIntervalGenerated) {
		waitTimeInterval(lastExecution);
		waitForTradeRetrieveDuration(previousTimeIntervalGenerated);
	}

	private void waitTimeInterval(ZonedDateTime lastExecution) {
		ZonedDateTime now = ZonedDateTimeUtils.now();
		if (Duration.between(lastExecution, now).getSeconds() < timeInterval.getSeconds()) {
			ZonedDateTime nextExecution = lastExecution.plus(timeInterval);
			Duration waitTime = Duration.between(now, nextExecution);
			threadSleep(waitTime);
		}
	}

	private void waitForTradeRetrieveDuration(TimeInterval previousTimeIntervalRetrieved) {
		ZonedDateTime nextEndTime = previousTimeIntervalRetrieved.getEnd()
				.plus(tickerGeneratorDuration);
		ZonedDateTime now = ZonedDateTimeUtils.now();
		if (now.isBefore(nextEndTime)) {
			threadSleep(Duration.between(now, nextEndTime));
		}
	}
	
	private void threadSleep(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
	}
}
