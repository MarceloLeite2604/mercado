package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

		Map<LocalDateTime, Map<Currency, TemporalTicker>> temporalTickersCurrencyByTime = retrieveTemporalTickersCurrencyByTime();
		
	}

	private Map<LocalDateTime, Map<Currency, TemporalTicker>> retrieveTemporalTickersCurrencyByTime() {
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
}
