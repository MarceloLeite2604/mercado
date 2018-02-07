package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.property.SimulatorPropertiesRetriever;

public class Simulator {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	private House house;

	private TimeDivisionController timeDivisionController;

	private Duration stepDuration;

	public Simulator() {
		this.house = new House();
	}

	private void configure() {
		SimulatorPropertiesRetriever simulatorPropertiesRetriever = new SimulatorPropertiesRetriever();
		ZonedDateTime startTime = simulatorPropertiesRetriever.retrieveStartTime();
		ZonedDateTime endTime = simulatorPropertiesRetriever.retrieveEndTime();
		this.stepDuration = simulatorPropertiesRetriever.retrieveStepDurationTime();
		Duration retrievingDuration = simulatorPropertiesRetriever.retrieveRetrievingDurationTime();
		timeDivisionController = new TimeDivisionController(startTime, endTime, retrievingDuration);
		this.house = new House();
	}

	public void runSimulation() {
		LOGGER.traceEntry();

		configure();
		logSimulationStart();

		logAccountsBalance(false);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		try {

			boolean firstExecution = true;
			Future<Map<TimeInterval, Map<Currency, TemporalTickerPO>>> future;
			Semaphore semaphore = new Semaphore(1);
			semaphore.acquire();
			HouseSimulationThread houseSimulationThread = new HouseSimulationThread(house, semaphore);
			executor.execute(houseSimulationThread);

			Map<TimeInterval, Map<Currency, TemporalTickerPO>> temporalTickersPOByTimeInterval = null;
			for (TimeInterval stepTimeInterval : timeDivisionController.geTimeIntervals()) {
				logSimulationStep(stepTimeInterval);
				TimeDivisionController timeDivisionController = new TimeDivisionController(stepTimeInterval,
						stepDuration);
				future = executor.submit(new TemporalTickerRetrieverCallable(timeDivisionController));
				if (firstExecution) {
					temporalTickersPOByTimeInterval = future.get();
					houseSimulationThread.setTemporalTickersPOByTimeInterval(temporalTickersPOByTimeInterval);
					semaphore.release();
				} else {
					semaphore.acquire();
					houseSimulationThread.setTemporalTickersPOByTimeInterval(temporalTickersPOByTimeInterval);
					semaphore.release();
					;
					temporalTickersPOByTimeInterval = future.get();
				}
			}
			houseSimulationThread.setFinished(true);
			logAccountsBalance(true);
			LOGGER.info("Simulation finished.");
		} catch (InterruptedException | ExecutionException exception) {
			throw new RuntimeException(exception);
		} finally {
			executor.shutdown();
		}
	}

	private void logAccountsBalance(boolean printTotalWorth) {
		for (Account account : house.getAccounts()) {
			LOGGER.info("Account \"" + account.getOwner() + "\":");
			Balance balance = account.getBalance();

			for (CurrencyAmount currencyAmount : balance.values()) {
				LOGGER.info("\t" + currencyAmount);
			}

			if (printTotalWorth) {
				logTotalWorth(account);
			}
		}
	}

	private void logTotalWorth(Account account) {
		CurrencyAmount totalRealAmount = new CurrencyAmount(Currency.REAL, 0.0);
		Balance balance = account.getBalance();

		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				TemporalTickerPO temporalTickerPO = house.getTemporalTickers().get(currency);
				if (temporalTickerPO != null) {
					CurrencyAmount currencyAmount = balance.get(currency);
					if (currencyAmount != null) {
						double last = temporalTickerPO.getLast();
						if (last == 0.0) {
							last = temporalTickerPO.getPreviousLast();
						}
						totalRealAmount.setAmount(totalRealAmount.getAmount() + (currencyAmount.getAmount() * last));
					}
				}
			} else {
				CurrencyAmount currencyAmount = balance.get(currency);
				if (currencyAmount != null) {
					totalRealAmount.setAmount(totalRealAmount.getAmount() + (currencyAmount.getAmount()));
				}
			}
		}
		LOGGER.info("\tTotal in " + Currency.REAL + ": " + totalRealAmount);
	}

	private void logSimulationStart() {
		LOGGER.info("Starting simulation from " + timeDivisionController + ".");
	}

	private void logSimulationStep(TimeInterval timeInterval) {
		LOGGER.debug("Advancing to step time " + timeInterval + ".");
	}
}
