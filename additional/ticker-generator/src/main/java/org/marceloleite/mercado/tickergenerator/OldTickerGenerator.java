package org.marceloleite.mercado.tickergenerator;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.DurationToStringConverter;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;
import org.marceloleite.mercado.retriever.checker.ValidDurationForTickerCheck;
import org.marceloleite.mercado.tickergenerator.property.TickerGeneratorPropertiesRetriever;

public class OldTickerGenerator {

	private static final Logger LOGGER = LogManager.getLogger(OldTickerGenerator.class);

	private TimeDivisionController timeDivisionController;

	private boolean ignoreTemporalTickersOnDatabase;

	public void generate() {

		retrieveProperties();
		checkTimeDivisionController(timeDivisionController);
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		LOGGER.info("Starting temporal tickers generator for period between " + timeDivisionController + ".");

		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			for (Currency currency : Currency.values()) {
				/* TODO: Watch out with BGold. */
				if (currency.isDigital() && currency != Currency.BGOLD) {
					LOGGER.info("Retrieving temporal ticker to " + currency + " currency for period between "
							+ timeInterval + ".");
					temporalTickerRetriever.retrieve(currency, timeInterval, ignoreTemporalTickersOnDatabase);

				}
			}
		}
		LOGGER.info("Finishing execution.");
	}

	public void retrieveProperties() {
		TickerGeneratorPropertiesRetriever tickerGeneratorPropertiesRetriever = new TickerGeneratorPropertiesRetriever();
		ZonedDateTime startTime = tickerGeneratorPropertiesRetriever.retrieveStartTime();
		ZonedDateTime endTime = tickerGeneratorPropertiesRetriever.retrieveEndTime();
		timeDivisionController = new TimeDivisionController(new TimeInterval(startTime, endTime), Duration.ofHours(1));
		ignoreTemporalTickersOnDatabase = tickerGeneratorPropertiesRetriever.retrieveIgnoreTemporalTickersOnDatabase();
	}

	private void checkTimeDivisionController(TimeDivisionController timeDivisionController) {
		ValidDurationForTickerCheck validDurationForTickerCheck = new ValidDurationForTickerCheck();
		Duration divisionDuration = timeDivisionController.getDivisionDuration();
		if (!validDurationForTickerCheck.check(divisionDuration)) {
			DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
			throw new RuntimeException(durationToStringConverter.convertTo(divisionDuration)
					+ " is not a valid duration to elaborate temporal tickers.");
		}
	}
}
