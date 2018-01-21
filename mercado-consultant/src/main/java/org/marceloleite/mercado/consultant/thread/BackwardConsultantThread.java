package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.consultant.thread.property.BackwardConsultantPropertiesRetriever;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class BackwardConsultantThread extends AbstractConsultantThread {

	private static final Logger LOGGER = LogManager.getLogger(BackwardConsultantThread.class);

	private LocalDateTime lastExecution;

	private LocalDateTime lastEndTimeRetrieved;

	public BackwardConsultantThread() {
		super(new BackwardConsultantPropertiesRetriever());
	}

	@Override
	public void run() {
		TradesRetriever tradesRetriever = new TradesRetriever();
		if (getConsultantProperties().getTradesSiteRetrieverStepDuration() != null) {
			tradesRetriever
					.setTradesSiteRetrieverStepDuration(getConsultantProperties().getTradesSiteRetrieverStepDuration());
		}
		LocalDateTime start = getConsultantProperties().getStartTime();
		while (!finished()) {
			lastExecution = LocalDateTime.now();
			LocalDateTime end = start.minus(getConsultantProperties().getTradeRetrieveDuration());
			LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
			LOGGER.info("Retrieving trades from " + localDateTimeToStringConverter.convertTo(end) + " to "
					+ localDateTimeToStringConverter.convertTo(start) + ".");
			for (Currency currency : Currency.values()) {
				/* TODO: Watch out with BGOLD. */
				if (currency.isDigital() && currency != Currency.BGOLD) {
					List<TradePO> trades = tradesRetriever.retrieve(currency, end, start,
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
			start = LocalDateTime.from(end);
			lastEndTimeRetrieved = LocalDateTime.from(end);
			waitTime();
		}
	}

	private void waitTime() {
		waitForTimeSlot();
	}

	private void waitForTimeSlot() {
		LocalDateTime now = LocalDateTime.now();
		if (Duration.between(lastExecution, now).getSeconds() < getConsultantProperties().getTimeInterval()
				.getSeconds()) {
			LocalDateTime nextExecution = lastExecution.plus(getConsultantProperties().getTimeInterval());
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
