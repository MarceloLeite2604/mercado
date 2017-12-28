package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;
import java.time.LocalDateTime;

import org.jboss.logging.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.consultant.thread.properties.BackwardConsultantPropertiesRetriever;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class BackwardConsultantThread extends AbstractConsultantThread {
	
	private static final Logger LOGGER = Logger.getLogger(BackwardConsultantThread.class);

	private LocalDateTime lastExecution;

	private LocalDateTime lastTimeRetrieved;

	public BackwardConsultantThread() {
		super(new BackwardConsultantPropertiesRetriever());
	}

	@Override
	public void run() {
		TradesRetriever tradesRetriever = new TradesRetriever();
		while (true) {
			lastExecution = LocalDateTime.now();
			LocalDateTime start = lastTimeRetrieved.minus(getConsultantProperties().getTradeRetrieveDuration());
			LOGGER.info("Start time: " + start);
			LOGGER.info("End time: " + lastTimeRetrieved);
			for (Currency currency : Currency.values()) {
				if (currency.isDigital()) {
					tradesRetriever.retrieve(currency, start, lastTimeRetrieved);
				}
			}
			lastTimeRetrieved = LocalDateTime.from(start);
			waitTime();
		}
	}

	private void waitTime() {
		waitForTimeSlot();
	}

	private void waitForTimeSlot() {
		LocalDateTime now = LocalDateTime.now();
		if (Duration.between(lastExecution, now).getSeconds() < getConsultantProperties().getTimeInterval().getSeconds()) {
			LocalDateTime nextExecution = lastExecution.plus(getConsultantProperties().getTimeInterval());
			Duration waitTime = Duration.between(now, nextExecution);
			threadSleep(waitTime);
		}
	}

	private void threadSleep(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
