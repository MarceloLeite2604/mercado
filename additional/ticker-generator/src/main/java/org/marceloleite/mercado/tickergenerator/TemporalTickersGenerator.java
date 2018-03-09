package org.marceloleite.mercado.tickergenerator;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.tickergenerator.property.TickerGeneratorPropertiesRetriever;

public class TemporalTickersGenerator {

	private List<TickerGeneratorThread> tickerGeneratorThreads;
	private boolean ignoreTemporalTickersOnDatabase;
	private ZonedDateTime startTime;
	private ZonedDateTime endTime;
	private Duration timeInterval;
	
	private static final String THREAD_NAME_TEMPLATE = "TTG %s";
	
	public void generate() {
		retrieveProperties();
		createThreads();
		for (TickerGeneratorThread tickerGeneratorThread : tickerGeneratorThreads) {
			tickerGeneratorThread.start();
		}
		
		for (TickerGeneratorThread tickerGeneratorThread : tickerGeneratorThreads) {
			try {
				tickerGeneratorThread.join();
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}

	private void createThreads() {
		boolean createOneMinuteThread = ((startTime.getSecond() % 60 == 0) && (endTime == null || endTime.getSecond() % 60 == 0));
		boolean createFiveMinutesThread = ((startTime.getMinute() % 5 == 0) && (endTime == null ||endTime.getMinute() % 5 == 0));
		boolean createTenMinutesThread = ((startTime.getMinute() % 10 == 0) && (endTime == null ||endTime.getMinute() % 10 == 0));
		boolean createFifteenMinutesThread = ((startTime.getMinute() % 15 == 0) && (endTime == null ||endTime.getMinute() % 15 == 0));
		boolean createThirtyMinutesThread = ((startTime.getMinute() % 30 == 0) && (endTime == null ||endTime.getMinute() % 30 == 0));
		boolean hasMinutes = (startTime.getMinute() != 0) || (endTime == null ||endTime.getMinute() != 0);
		boolean hasSeconds = (startTime.getSecond() != 0) || (endTime == null ||endTime.getSecond() != 0);
		boolean createOneHourThread = !hasMinutes && !hasSeconds;

		tickerGeneratorThreads = new ArrayList<>();
		
		if (createOneMinuteThread) {
			createThread(Duration.ofMinutes(1), String.format(THREAD_NAME_TEMPLATE, "1 min."));
		}
		
		if (createFiveMinutesThread) {
			createThread(Duration.ofMinutes(5), String.format(THREAD_NAME_TEMPLATE, "5 mins."));
		}
		
		if (createTenMinutesThread) {
			createThread(Duration.ofMinutes(10), String.format(THREAD_NAME_TEMPLATE, "10 mins."));
		}
		
		if (createFifteenMinutesThread) {
			createThread(Duration.ofMinutes(15), String.format(THREAD_NAME_TEMPLATE, "15 mins."));
		}
		
		if (createThirtyMinutesThread) {
			createThread(Duration.ofMinutes(30), String.format(THREAD_NAME_TEMPLATE, "30 mins."));
		}
		
		if (createOneHourThread) {
			createThread(Duration.ofHours(1), String.format(THREAD_NAME_TEMPLATE, "1 hour"));
		}
	}

	private void createThread(Duration duration, String threadName) {
		TickerGeneratorThread tickerGeneratorThread = new TickerGeneratorThread(startTime, endTime, duration, timeInterval, ignoreTemporalTickersOnDatabase);
		tickerGeneratorThread.setThreadName(threadName);
		tickerGeneratorThreads.add(tickerGeneratorThread);
	}

	private void retrieveProperties() {
		TickerGeneratorPropertiesRetriever tickerGeneratorPropertiesRetriever = new TickerGeneratorPropertiesRetriever();

		ignoreTemporalTickersOnDatabase = tickerGeneratorPropertiesRetriever.retrieveIgnoreTemporalTickersOnDatabase();
		startTime = tickerGeneratorPropertiesRetriever.retrieveStartTime();
		endTime = tickerGeneratorPropertiesRetriever.retrieveEndTime();
		timeInterval = Duration.ofSeconds(0);
	}
}
