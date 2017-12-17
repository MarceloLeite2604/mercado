package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.modeler.persistence.model.TemporalTicker;
import org.marceloleite.mercado.nnew.TemporalTickerRetriever;

public class Simulator {

	private static final Duration DEFAULT_STEP_TIME = Duration.ofSeconds(30);

	private House house;

	private List<Account> accounts;

	private Duration stepTime;

	private LocalDateTime startTime;

	private LocalDateTime stopTime;

	public Simulator() {
		this.stepTime = DEFAULT_STEP_TIME;
		this.house = new House();
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
		checkSimulationRequirements();

		System.out.println("Running simulation.");

		long totalSteps = calculateSteps(startTime, stopTime, stepTime);
		LocalDateTime startStepTime = startTime;
		LocalDateTime stopStepTime;

		for (long step = 0; step < totalSteps; step++) {
			Duration stepDuration = calculateStepDuration(startStepTime, stopTime, stepTime);
			stopStepTime = startStepTime.plus(stepDuration);
			Map<LocalDateTime, Map<Currency, TemporalTicker>> temporalTickersCurrencyByTime = retrieveTemporalTickersCurrencyByTime(
					startStepTime, stopStepTime, stepDuration);
			startStepTime = stopStepTime;

			Set<LocalDateTime> stepTimes = temporalTickersCurrencyByTime.keySet();
			for (LocalDateTime stepTime : stepTimes) {
				Map<Currency, TemporalTicker> currenciesTemporalTickers = temporalTickersCurrencyByTime.get(stepTime);

				updateBasePrices(currenciesTemporalTickers);
			}
		}

		System.out.println("Simulation finished.");
	}

	private Map<LocalDateTime, Map<Currency, TemporalTicker>> retrieveTemporalTickersCurrencyByTime(
			LocalDateTime startTime, LocalDateTime stopTime, Duration stepTime) {
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		Map<LocalDateTime, Map<Currency, TemporalTicker>> temporalTickersCurrencyByTime = new HashMap<>();

		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				List<TemporalTicker> temporalTickers = temporalTickerRetriever.retrieve(currency, startTime, stopTime,
						stepTime);
				for (TemporalTicker temporalTicker : temporalTickers) {
					Map<Currency, TemporalTicker> currencyMap = Optional
						.ofNullable(temporalTickersCurrencyByTime.get(temporalTicker.getTo()))
						.orElse(new EnumMap<>(Currency.class));
					currencyMap.put(currency, temporalTicker);
					temporalTickersCurrencyByTime.put(temporalTicker.getTo(), currencyMap);
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

	private long calculateSteps(LocalDateTime from, LocalDateTime to, Duration stepDuration) {
		Duration timeDuration = Duration.between(from, to);
		long totalSteps = (long) Math.ceil((double) timeDuration.getSeconds() / (double) stepDuration.getSeconds());
		totalSteps = (totalSteps == 0 ? 1 : totalSteps);
		return totalSteps;
	}

	private Duration calculateStepDuration(LocalDateTime from, LocalDateTime to, Duration stepDuration) {
		Duration remainingDuration = Duration.between(from, to);
		if (stepDuration.compareTo(remainingDuration) > 0) {
			return remainingDuration;
		} else {
			return stepDuration;
		}
	}

	private void updateBasePrices(Map<Currency, TemporalTicker> currenciesTemporalTickers) {
		for (Account account : accounts) {
			updateBasePricesForAccount(currenciesTemporalTickers, account);
		}
	}

	private void updateBasePricesForAccount(Map<Currency, TemporalTicker> currenciesTemporalTickers, Account account) {
		LocalDateTimeToStringConverter localDateTimeToString = new LocalDateTimeToStringConverter();
		boolean updateBasePrice = false;
		Map<Currency, CurrencyMonitoring> currenciesMonitoring = account.getCurrenciesMonitoring();
		Map<Currency, Double> basePrices = account.getBasePrices();
		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {

				if (currenciesMonitoring.containsKey(currency)) {
					TemporalTicker temporalTicker = currenciesTemporalTickers.get(currency);
					double currentPrice = temporalTicker.getLast();
					if (currentPrice != 0) {

						updateBasePrice = !basePrices.containsKey(currency);
						double basePrice = Optional.ofNullable(basePrices.get(currency))
							.orElse(currentPrice);

						double percentage = (currentPrice / basePrice) - 1.0;

						CurrencyMonitoring currencyMonitoring = currenciesMonitoring.get(currency);
						if (percentage >= currencyMonitoring.getIncreasePercentage()) {
							System.out.println("[" + localDateTimeToString.format(temporalTicker.getTo()) + "] ["
									+ account.getOwner() + "]: Price for " + currency + " has increased by "
									+ Math.abs(percentage * 100) + "%.");
							updateBasePrice = true;

						} else if (percentage <= -currencyMonitoring.getDecreasePercentage()) {
							System.out.println("[" + localDateTimeToString.format(temporalTicker.getTo()) + "] ["
									+ account.getOwner() + "]: Price for " + currency + " has descreased by "
									+ Math.abs(percentage * 100) + "%.");
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
