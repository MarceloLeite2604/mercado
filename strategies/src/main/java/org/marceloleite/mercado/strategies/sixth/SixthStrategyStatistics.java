package org.marceloleite.mercado.strategies.sixth;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.cdi.MercadoApplicationContextAware;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.Statistics;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.util.CollectionUtils;

public class SixthStrategyStatistics {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategyStatistics.class);

	private int circularArraySize;

	private int nextValueSteps;

	private Statistics lastPriceStatistics;

	private Statistics averagePriceStatistics;

	private TemporalTickerDAO temporalTickerDAO;

	public static Builder builder() {
		return new Builder();
	}

	private SixthStrategyStatistics() {
	}

	private SixthStrategyStatistics(Builder builder) {
		this.circularArraySize = builder.circularArraySize;
		this.nextValueSteps = builder.nextValueSteps;
		lastPriceStatistics = new Statistics(circularArraySize, nextValueSteps);
		averagePriceStatistics = new Statistics(circularArraySize, nextValueSteps);
		this.temporalTickerDAO = MercadoApplicationContextAware.getBean(TemporalTickerDAO.class,
				"TemporalTickerDatabaseDAO");
	}

	public Integer getCircularArraySize() {
		return circularArraySize;
	}

	public Integer getNextValueSteps() {
		return nextValueSteps;
	}

	public Statistics getLastPriceStatistics() {
		return lastPriceStatistics;
	}

	public Statistics getAveragePriceStatistics() {
		return averagePriceStatistics;
	}

	public void addInformation(TemporalTicker temporalTicker, TimeInterval timeInterval, Currency currency) {
		addValueOnLastPriceStatistics(temporalTicker);
		addValueOnAveragePriceStatistics();
		if (lastPriceStatisticsIsNotFilled()) {
			fillLastPriceStatisticsCircularArray(timeInterval, currency);
		}
	}

	private boolean lastPriceStatisticsIsNotFilled() {
		return !lastPriceStatistics.getCircularArray()
				.isFilled();
	}

	private void addValueOnLastPriceStatistics(TemporalTicker temporalTicker) {
		lastPriceStatistics.add(temporalTicker.getCurrentOrPreviousLast()
				.doubleValue());
	}

	private void addValueOnAveragePriceStatistics() {
		averagePriceStatistics.add(lastPriceStatistics.getAverage());
	}

	private void fillLastPriceStatisticsCircularArray(TimeInterval timeInterval, Currency currency) {
		LOGGER.debug("Filling last price statistics circular array.");
		CircularArray<Double> circularArray = lastPriceStatistics.getCircularArray();
		Duration stepTime = timeInterval.getDuration();
		ZonedDateTime endTime = timeInterval.getStart();
		Duration duration = stepTime.multipliedBy(circularArray.getVacantPositions());
		TimeInterval timeIntervalToRetrieve = new TimeInterval(duration, endTime);

		List<TemporalTicker> temporalTickersRetrieved = temporalTickerDAO
				.findByCurrencyAndDurationAndStartBetween(currency, timeInterval.getDuration(), timeIntervalToRetrieve);
		if (temporalTickersRetrieved.size() != circularArray.getVacantPositions()) {
			TimeDivisionController timeDivisionController = new TimeDivisionController(timeIntervalToRetrieve,
					stepTime);
			temporalTickersRetrieved = new LinkedList<>();
			for (TimeInterval timeIntervalDivision : timeDivisionController.getTimeIntervals()) {
				TemporalTicker temporalTickers = temporalTickerDAO.findByCurrencyAndStartAndEnd(currency,
						timeIntervalDivision.getStart(), timeIntervalDivision.getEnd());
				temporalTickersRetrieved.add(temporalTickers);
			}

			if (temporalTickersRetrieved.size() != circularArray.getVacantPositions()) {
				throw new RuntimeException("Error while filling last price statistics circular array.");
			}
		}
		if (CollectionUtils.isEmpty(temporalTickersRetrieved)) {
			TimeDivisionController timeDivisionController = new TimeDivisionController(timeIntervalToRetrieve,
					stepTime);
			temporalTickersRetrieved = new ArrayList<>();
			for (TimeInterval retrievalTimeInterval : timeDivisionController.getTimeIntervals()) {
				TemporalTicker temporalTickerRetrieved = temporalTickerDAO.findByCurrencyAndStartAndEnd(currency,
						retrievalTimeInterval.getStart(), retrievalTimeInterval.getEnd());
				temporalTickersRetrieved.add(temporalTickerRetrieved);
			}
		}
		temporalTickersRetrieved.forEach(
				temporalTickerRetrieved -> lastPriceStatistics.add(temporalTickerRetrieved.getCurrentOrPreviousLast()
						.doubleValue()));
		LOGGER.debug("Filling concluded.");
	}

	public static class Builder {

		private int circularArraySize;

		private int nextValueSteps;

		private Builder() {
		};

		public Builder circularArraySize(int circularArraySize) {
			this.circularArraySize = circularArraySize;
			return this;
		}

		public Builder nextValueSteps(int nextValueSteps) {
			this.nextValueSteps = nextValueSteps;
			return this;
		}

		public SixthStrategyStatistics build() {
			return new SixthStrategyStatistics(this);
		}
	}

}
