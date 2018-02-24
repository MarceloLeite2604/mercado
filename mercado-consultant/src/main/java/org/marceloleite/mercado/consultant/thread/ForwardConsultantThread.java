package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import org.jboss.logging.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.consultant.thread.property.ForwardConsultantPropertiesRetriever;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class ForwardConsultantThread extends AbstractConsultantThread {

	private static final Logger LOGGER = Logger.getLogger(ForwardConsultantThread.class);
	private static final String THREAD_NAME = "Forward consultant thread";
	private TimeInterval timeIntervalToRetrieve;

	public ForwardConsultantThread() {
		super(new ForwardConsultantPropertiesRetriever());
	}

	@Override
	public void run() {
		Thread.currentThread().setName(THREAD_NAME);
		TradesRetriever tradesRetriever = new TradesRetriever();
		if (getConsultantProperties().getTradesSiteRetrieverStepDuration() != null) {
			tradesRetriever
					.setTradesSiteRetrieverStepDuration(getConsultantProperties().getTradesSiteRetrieverStepDuration());
		}
		timeIntervalToRetrieve = null;
		do {
			timeIntervalToRetrieve = calculateTimeIntervalToRetrieve(timeIntervalToRetrieve);
			ZonedDateTime lastExecution = ZonedDateTimeUtils.now();
			LOGGER.info("Retrieving trades from " + timeIntervalToRetrieve.toString() + ".");
			for (Currency currency : Currency.values()) {
				/* TODO: Watch out with BGOLD. */
				if (currency.isDigital() && currency != Currency.BGOLD) {

					List<TradeDataModel> trades = tradesRetriever.retrieve(currency, timeIntervalToRetrieve,
							getConsultantProperties().isDatabaseValuesIgnored());
					int totalTrades;
					if (trades != null) {
						totalTrades = trades.size();
					} else {
						totalTrades = 0;
					}
					LOGGER.info(totalTrades + " trade(s) retrieved for " + currency + " currency.");
				}
			}
			waitTime(lastExecution, timeIntervalToRetrieve);
		} while (!finished());
	}

	private TimeInterval calculateTimeIntervalToRetrieve(TimeInterval previousTimeIntervalRetrieved) {
		ZonedDateTime start;
		if (previousTimeIntervalRetrieved == null) {
			start = getConsultantProperties().getStartTime();
		} else {
			start = ZonedDateTime.from(previousTimeIntervalRetrieved.getEnd());
		}

		ZonedDateTime end = start.plus(getConsultantProperties().getTradeRetrieveDuration());
		ZonedDateTime now = ZonedDateTimeUtils.now();
		if (end.isAfter(now)) {
			end = now;
		}
		return new TimeInterval(start, end);
	}

	private void waitTime(ZonedDateTime lastExecution, TimeInterval previousTimeIntervalRetrieved) {
		waitTimeInterval(lastExecution);
		waitForTradeRetrieveDuration(previousTimeIntervalRetrieved);
	}

	private void waitTimeInterval(ZonedDateTime lastExecution) {
		ZonedDateTime now = ZonedDateTimeUtils.now();
		if (Duration.between(lastExecution, now).getSeconds() < getConsultantProperties().getTimeInterval()
				.getSeconds()) {
			ZonedDateTime nextExecution = lastExecution.plus(getConsultantProperties().getTimeInterval());
			Duration waitTime = Duration.between(now, nextExecution);
			threadSleep(waitTime);
		}
	}

	private void waitForTradeRetrieveDuration(TimeInterval previousTimeIntervalRetrieved) {
		ZonedDateTime nextEndTime = previousTimeIntervalRetrieved.getEnd()
				.plus(getConsultantProperties().getTradeRetrieveDuration());
		ZonedDateTime now = ZonedDateTimeUtils.now();
		if (now.isBefore(nextEndTime)) {
			threadSleep(Duration.between(now, nextEndTime));
		}
	}

	protected boolean finished() {
		ZonedDateTime endTime = getConsultantProperties().getEndTime();
		if (endTime == null) {
			return false;
		} else {
			if (timeIntervalToRetrieve == null) {
				return true;
			} else {
				return (timeIntervalToRetrieve.getStart().isAfter(endTime));
			}
		}
	}
}
