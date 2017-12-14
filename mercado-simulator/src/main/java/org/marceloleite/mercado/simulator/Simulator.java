package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.marceloleite.mercado.commons.util.ObjectToJsonFormatter;
import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.modeler.persistence.TemporalTicker;
import org.marceloleite.mercado.nnew.TemporalTickerRetriever;

public class Simulator {

	private static final Duration DEFAULT_STEP_TIME = Duration.ofSeconds(30);

	private House house;

	private TemporalController<Deposit> depositsTemporalController;

	private TemporalController<BuyOrder> buyOrdersTemporalController;

	private Duration stepTime;

	private LocalDateTime startTime;

	private LocalDateTime stopTime;

	public Simulator() {
		this.stepTime = DEFAULT_STEP_TIME;
		this.house = new House();
		this.depositsTemporalController = new TemporalController<>();
		this.buyOrdersTemporalController = new TemporalController<>();
	}

	public void addDeposit(Deposit deposit, LocalDateTime time) {
		depositsTemporalController.add(time, deposit);
	}

	public void addBuyOrder(BuyOrder buyOrder, LocalDateTime time) {
		buyOrdersTemporalController.add(time, buyOrder);
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
			}
		}
		
		System.out.println("Simulation finished.");
	}

	private Map<LocalDateTime, Map<Currency, TemporalTicker>> retrieveTemporalTickersCurrencyByTime(
			LocalDateTime startTime, LocalDateTime stopTime, Duration stepTime) {
		TemporalTickerRetriever temporalTickerRetriever = new TemporalTickerRetriever();
		Map<LocalDateTime, Map<Currency, TemporalTicker>> temporalTickersCurrencyByTime = new HashMap<>();

		System.out.println("Retrieving temporal tickers.");
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
}
