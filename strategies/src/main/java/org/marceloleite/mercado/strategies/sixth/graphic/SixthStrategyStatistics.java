package org.marceloleite.mercado.strategies.sixth.graphic;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

public class SixthStrategyStatistics {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategyStatistics.class);

	private Integer circularArraySize;

	private Integer nextValueSteps;

	private Statistics lastPriceStatistics;

	private Statistics averagePriceStatistics;

	private TemporalTickerDAO temporalTickerDAO;

	public SixthStrategyStatistics() {
		this.temporalTickerDAO = MercadoApplicationContextAware.getBean(TemporalTickerDAO.class,
				"TemporalTickerDatabaseDAO");
	}

	public Integer getCircularArraySize() {
		return circularArraySize;
	}

	public void setCircularArraySize(Integer circularArraySize) {
		this.circularArraySize = circularArraySize;
	}

	public Integer getNextValueSteps() {
		return nextValueSteps;
	}

	public void setNextValueSteps(Integer nextValueSteps) {
		this.nextValueSteps = nextValueSteps;
	}

	public Statistics getLastPriceStatistics() {
		return lastPriceStatistics;
	}

	public Statistics getAveragePriceStatistics() {
		return averagePriceStatistics;
	}

	public void checkFieldsAndCreateStatistics() {
		if (circularArraySize != null && nextValueSteps != null) {
			lastPriceStatistics = new Statistics(circularArraySize, nextValueSteps);
			averagePriceStatistics = new Statistics(circularArraySize, nextValueSteps);
		}
	}

	public void addInformation(TemporalTicker temporalTicker, TimeInterval timeInterval,
			Currency currency) {
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
				.findByCurrencyAndDurationAndStartBetween(currency, duration, timeIntervalToRetrieve);
		if (temporalTickersRetrieved == null) {
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

}
