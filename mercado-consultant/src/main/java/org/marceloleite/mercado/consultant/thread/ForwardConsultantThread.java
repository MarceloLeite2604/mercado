package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.jboss.logging.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.consultant.thread.property.ForwardConsultantPropertiesRetriever;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class ForwardConsultantThread extends AbstractConsultantThread {

	private static final Logger LOGGER = Logger.getLogger(ForwardConsultantThread.class);

	private LocalDateTime lastExecution;

	public ForwardConsultantThread() {
		super(new ForwardConsultantPropertiesRetriever());
	}

	@Override
	public void run() {
		TradesRetriever tradesRetriever = new TradesRetriever();
		LocalDateTime start = getConsultantProperties().getStartTime();
		while (!finished()) {
			lastExecution = LocalDateTime.now();
			LocalDateTime end = start.plus(getConsultantProperties().getTradeRetrieveDuration());
			if (end.isAfter(LocalDateTime.now())) {
				end = LocalDateTime.now();
				start = end.minus(getConsultantProperties().getTradeRetrieveDuration());
			}
			LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
			LOGGER.info("Retrieving trades from " + localDateTimeToStringConverter.convertTo(start) + " to "
					+ localDateTimeToStringConverter.convertTo(end));
			for (Currency currency : Currency.values()) {
				if (currency.isDigital()) {

					List<TradePO> trades = tradesRetriever.retrieve(currency, start, end, getConsultantProperties().isDatabaseValuesIgnored());
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
			waitTime();
		}
	}

	private void waitTime() {
		waitForTimeSlot();
		waitForNextExecution();
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

	private void waitForNextExecution() {
		LocalDateTime nextExecution = lastExecution.plus(getConsultantProperties().getTimeInterval());
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(nextExecution)) {
			Duration duration = Duration.between(now, nextExecution);
			threadSleep(duration);
		}
	}

	protected boolean finished() {
		return false;
	}
}
