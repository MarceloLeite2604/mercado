package org.marceloleite.mercado.consultant.thread;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.jboss.logging.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.consultant.thread.properties.BackwardConsultantPropertiesRetriever;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class BackwardConsultantThread extends AbstractConsultantThread {
	
	private static final Logger LOGGER = Logger.getLogger(BackwardConsultantThread.class);

	private LocalDateTime lastExecution;

	public BackwardConsultantThread() {
		super(new BackwardConsultantPropertiesRetriever());
	}

	@Override
	public void run() {
		TradesRetriever tradesRetriever = new TradesRetriever();
		LocalDateTime end = getConsultantProperties().getEndTime();
		while (!finished()) {
			lastExecution = LocalDateTime.now();
			LocalDateTime start = end.minus(getConsultantProperties().getTradeRetrieveDuration());
			LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
			LOGGER.info("Retrieving trades from " + localDateTimeToStringConverter.convert(start) + " to "
					+ localDateTimeToStringConverter.convert(end));
			for (Currency currency : Currency.values()) {
				if (currency.isDigital()) {
					List<TradePO> trades = tradesRetriever.retrieve(currency, start, end);
					int totalTrades;
					if (trades != null) {
						totalTrades = trades.size();
					} else {
						totalTrades = 0;
					}
					LOGGER.info(totalTrades + " trade(s) retrieved for " + currency + " currency.");
				}
			}
			end = LocalDateTime.from(start);
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

	@Override
	protected boolean finished() {
		return false;
	}
}
