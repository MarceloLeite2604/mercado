package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.additional.TemporalTickerGenerator;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class Simulator {

	private static final Duration DEFAULT_STEP_TIME = Duration.ofSeconds(30);

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	private House house;

	private List<Account> accounts;

	private Duration stepTime;

	private LocalDateTime startTime;

	private LocalDateTime stopTime;

	public Simulator() {
		this.stepTime = DEFAULT_STEP_TIME;
		this.house = new House();
		this.accounts = new ArrayList<>();
	}

	public Duration getStepTime() {
		return stepTime;
	}

	public void setStepTime(Duration stepTime) {
		this.stepTime = stepTime;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getStopTime() {
		return stopTime;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setStopTime(LocalDateTime endTime) {
		this.stopTime = endTime;
	}

	public void runSimulation() {
		LOGGER.traceEntry();
		checkSimulationRequirements();

		LOGGER.info("Running simulation.");

		TimeDivisionController timeDivisionController = new TimeDivisionController(startTime, stopTime, stepTime);

		for (long step = 0; step < timeDivisionController.getDivisions(); step++) {
			TimeInterval nextTimeInterval = timeDivisionController.getNextTimeInterval();
			Map<LocalDateTime, Map<Currency, TemporalTickerPO>> temporalTickersCurrencyByTime = retrieveTemporalTickersCurrencyByTime(
					nextTimeInterval);

			for (LocalDateTime stepTime : temporalTickersCurrencyByTime.keySet()) {
				Map<Currency, TemporalTickerPO> currenciesTemporalTickers = temporalTickersCurrencyByTime.get(stepTime);
				updateBasePrices(currenciesTemporalTickers);
			}
		}

		LOGGER.info("Simulation finished.");
	}

	private Map<LocalDateTime, Map<Currency, TemporalTickerPO>> retrieveTemporalTickersCurrencyByTime(
			TimeInterval timeInterval) {
		TemporalTickerGenerator temporalTickerGenerator = new TemporalTickerGenerator();
		Map<LocalDateTime, Map<Currency, TemporalTickerPO>> temporalTickersCurrencyByTime = new HashMap<>();

		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				TimeDivisionController timeDivisionController = new TimeDivisionController(timeInterval.getStart(),
						timeInterval.getEnd(), timeInterval.getDuration());
				List<TemporalTickerPO> temporalTickers = temporalTickerGenerator.generate(currency,
						timeDivisionController);
				for (TemporalTickerPO temporalTicker : temporalTickers) {
					Map<Currency, TemporalTickerPO> currencyMap = Optional
							.ofNullable(
									temporalTickersCurrencyByTime.get(temporalTicker.getTemporalTickerId().getEnd()))
							.orElse(new EnumMap<>(Currency.class));
					currencyMap.put(currency, temporalTicker);
					temporalTickersCurrencyByTime.put(temporalTicker.getTemporalTickerId().getEnd(), currencyMap);
				}
			}
		}
		return temporalTickersCurrencyByTime;
	}

	private void checkSimulationRequirements() {
		if (startTime == null) {
			throw new IllegalStateException("Simulation start time not defined.");
		}

		if (stopTime == null) {
			throw new IllegalStateException("Simulation stop time not defined.");
		}
	}

	private void updateBasePrices(Map<Currency, TemporalTickerPO> currenciesTemporalTickers) {
		if (accounts.size() > 0) {
			for (Account account : accounts) {
				updateBasePricesForAccount(currenciesTemporalTickers, account);
			}
		}
	}

	private void updateBasePricesForAccount(Map<Currency, TemporalTickerPO> currenciesTemporalTickers,
			Account account) {
		LocalDateTimeToStringConverter localDateTimeToString = new LocalDateTimeToStringConverter();
		boolean updateBasePrice = false;
		Map<Currency, CurrencyMonitoring> currenciesMonitoring = account.getCurrenciesMonitoring();
		Map<Currency, Double> basePrices = account.getBasePrices();
		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {

				if (currenciesMonitoring.containsKey(currency)) {
					TemporalTickerPO temporalTicker = currenciesTemporalTickers.get(currency);
					double currentPrice = temporalTicker.getLast();
					if (currentPrice != 0) {

						updateBasePrice = !basePrices.containsKey(currency);
						double basePrice = Optional.ofNullable(basePrices.get(currency)).orElse(currentPrice);

						double percentage = (currentPrice / basePrice) - 1.0;

						CurrencyMonitoring currencyMonitoring = currenciesMonitoring.get(currency);
						if (percentage >= currencyMonitoring.getIncreasePercentage()) {
							System.out.println(
									"[" + localDateTimeToString.convert(temporalTicker.getTemporalTickerId().getEnd())
											+ "] [" + account.getOwner() + "]: Price for " + currency
											+ " has increased by " + Math.abs(percentage * 100) + "%.");
							updateBasePrice = true;

						} else if (percentage <= -currencyMonitoring.getDecreasePercentage()) {
							System.out.println(
									"[" + localDateTimeToString.convert(temporalTicker.getTemporalTickerId().getEnd())
											+ "] [" + account.getOwner() + "]: Price for " + currency
											+ " has descreased by " + Math.abs(percentage * 100) + "%.");
							updateBasePrice = true;
						}

						if (updateBasePrice) {
							basePrices.put(currency, currentPrice);
						}
					}
				}
			}
		}
	}
}
