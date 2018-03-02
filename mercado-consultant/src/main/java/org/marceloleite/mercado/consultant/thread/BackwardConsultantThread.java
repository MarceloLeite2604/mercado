package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.consultant.thread.property.BackwardConsultantPropertiesRetriever;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class BackwardConsultantThread extends AbstractConsultantThread {

	private static final Logger LOGGER = LogManager.getLogger(BackwardConsultantThread.class);
	private static final String THREAD_NAME = "Backward consultant thread";
	

	private ZonedDateTime lastExecution;

	private ZonedDateTime lastEndTimeRetrieved;

	public BackwardConsultantThread() {
		super(new BackwardConsultantPropertiesRetriever());
	}

	@Override
	public void run() {
		Thread.currentThread().setName(THREAD_NAME);
		TradesRetriever tradesRetriever = new TradesRetriever();
		if (getConsultantProperties().getTradesSiteRetrieverStepDuration() != null) {
			tradesRetriever
					.setTradesSiteRetrieverStepDuration(getConsultantProperties().getTradesSiteRetrieverStepDuration());
		}
		ZonedDateTime start = getConsultantProperties().getStartTime();
		while (!finished()) {
			lastExecution = ZonedDateTimeUtils.now();
			ZonedDateTime end = start.minus(getConsultantProperties().getTradeRetrieveDuration());
			ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
			LOGGER.info("Retrieving trades from " + zonedDateTimeToStringConverter.convertTo(end) + " to "
					+ zonedDateTimeToStringConverter.convertTo(start) + ".");
			for (Currency currency : Currency.values()) {
				/* TODO: Watch out with BGOLD. */
				if (currency.isDigital() && currency != Currency.BGOLD) {
					List<Trade> trades = tradesRetriever.retrieve(currency, end, start,
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
			start = ZonedDateTime.from(end);
			lastEndTimeRetrieved = ZonedDateTime.from(end);
			waitTime();
		}
	}

	private void waitTime() {
		waitForTimeSlot();
	}

	private void waitForTimeSlot() {
		ZonedDateTime now = ZonedDateTimeUtils.now();
		if (Duration.between(lastExecution, now).getSeconds() < getConsultantProperties().getTimeInterval()
				.getSeconds()) {
			ZonedDateTime nextExecution = lastExecution.plus(getConsultantProperties().getTimeInterval());
			Duration waitTime = Duration.between(now, nextExecution);
			threadSleep(waitTime);
		}
	}

	@Override
	protected boolean finished() {
		return (lastEndTimeRetrieved != null && (lastEndTimeRetrieved.isBefore(getConsultantProperties().getEndTime())
				|| lastEndTimeRetrieved.isEqual(getConsultantProperties().getEndTime())));
	}
}
