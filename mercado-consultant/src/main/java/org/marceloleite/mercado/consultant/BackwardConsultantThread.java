package org.marceloleite.mercado.consultant;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class BackwardConsultantThread extends Thread {

	private LocalDateTime lastTimeRetrieved;

	private Duration tradeRetrieveDuration;

	private Duration timeInterval;

	private LocalDateTime lastExecution;

	public BackwardConsultantThread(LocalDateTime lastTimeRetrieved, Duration tradeRetrieveDuration, Duration timeInterval) {
		super();
		this.lastTimeRetrieved = lastTimeRetrieved;
		this.tradeRetrieveDuration = tradeRetrieveDuration;
		this.timeInterval = timeInterval;
	}

	@Override
	public void run() {
		TradesRetriever tradesRetriever = new TradesRetriever();
		while (true) {
			lastExecution = LocalDateTime.now();
			LocalDateTime start = lastTimeRetrieved.minus(tradeRetrieveDuration);
			System.out.println("Start time: " + start);
			System.out.println("End time: " + lastTimeRetrieved);
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
		/*waitForNextExecution();*/
	}

	private void waitForTimeSlot() {
		LocalDateTime now = LocalDateTime.now();
		if (Duration.between(lastExecution, now).getSeconds() < timeInterval.getSeconds()) {
			LocalDateTime nextExecution = lastExecution.plus(timeInterval);
			Duration waitTime = Duration.between(now, nextExecution);
			threadSleep(waitTime);
		}
	}

	/*private void waitForNextExecution() {
		LocalDateTime nextExecution = lastExecution.plus(timeInterval);
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(nextExecution)) {
			Duration duration = Duration.between(now, nextExecution);
			threadSleep(duration);
		}
	}*/

	private void threadSleep(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
