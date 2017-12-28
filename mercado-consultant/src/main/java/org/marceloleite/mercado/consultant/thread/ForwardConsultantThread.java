package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;
import java.time.LocalDateTime;

import org.jboss.logging.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.consultant.thread.properties.ForwardConsultantPropertiesRetriever;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class ForwardConsultantThread extends AbstractConsultantThread {
	
	private static final Logger LOGGER = Logger.getLogger(ForwardConsultantThread.class);

	private LocalDateTime lastExecution;
	
	private LocalDateTime lastTimeRetrieved;

	public ForwardConsultantThread() {
		super(new ForwardConsultantPropertiesRetriever());
	}

	@Override
	public void run() {
		TradesRetriever tradesRetriever = new TradesRetriever();
		while (!finished()) {
			lastExecution = LocalDateTime.now();
			LocalDateTime end = lastTimeRetrieved.plus(getConsultantProperties().getTradeRetrieveDuration());
			if (end.isAfter(LocalDateTime.now())) {
				end = LocalDateTime.now();
				lastTimeRetrieved = end.minus(getConsultantProperties().getTradeRetrieveDuration());
			}
			LOGGER.info("Start time: " + lastTimeRetrieved);
			LOGGER.info("End time: " + end);
			for (Currency currency : Currency.values()) {
				if (currency.isDigital()) {
					tradesRetriever.retrieve(currency, lastTimeRetrieved, end);
				}
			}
			lastTimeRetrieved = LocalDateTime.from(end);
			waitTime();
		}
	}

	private void waitTime() {
		waitForTimeSlot();
		waitForNextExecution();
	}

	private void waitForTimeSlot() {
		LocalDateTime now = LocalDateTime.now();
		if (Duration.between(lastExecution, now).getSeconds() < getConsultantProperties().getTimeInterval().getSeconds()) {
			LocalDateTime nextExecution = lastExecution.plus(getConsultantProperties().getTimeInterval());
			Duration waitTime = Duration.between(now, nextExecution);
			threadSleep(waitTime);
		}
	}

	private void waitForNextExecution() {
		LocalDateTime nextExecution = lastExecution.plus(getConsultantProperties().getTimeInterval());
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(nextExecution)) {
			Duration duration = Duration.between(now, nextExecution);
			threadSleep(duration);
		}
	}

	private void threadSleep(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private boolean finished() {
		return false;
	}
}
