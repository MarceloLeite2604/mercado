package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Wallet;
import org.marceloleite.mercado.simulator.property.SimulatorPropertiesRetriever;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	@Inject
	private SimulatorPropertiesRetriever simulatorPropertiesRetriever;

	@Inject
	private SimulationHouse house;

	private TimeDivisionController timeDivisionController;

	private Duration stepDuration;

	private void configure() {
		ZonedDateTime startTime = simulatorPropertiesRetriever.retrieveStartTime();
		ZonedDateTime endTime = simulatorPropertiesRetriever.retrieveEndTime();
		this.stepDuration = simulatorPropertiesRetriever.retrieveStepDuration();
		Duration retrievingDuration = simulatorPropertiesRetriever.retrieveRetrievingDuration();
		timeDivisionController = new TimeDivisionController(startTime, endTime, retrievingDuration);
	}

	public void runSimulation() {
		LOGGER.traceEntry();

		configure();
		logSimulationStart();

		logAccountsBalance(false);

		// ExecutorService executor = Executors.newFixedThreadPool(2);
		// try {
		// Future<TreeMap<TimeInterval, Map<Currency, TemporalTicker>>> future;
		// HouseSimulationThread houseSimulationThread = new
		// HouseSimulationThread(house);
		// executor.execute(houseSimulationThread);
		//
		// TreeMap<TimeInterval, Map<Currency, TemporalTicker>>
		// temporalTickersByTimeInterval = null;
		// for (TimeInterval stepTimeInterval :
		// timeDivisionController.geTimeIntervals()) {
		// LOGGER.info(stepTimeInterval);
		// TimeDivisionController timeDivisionController = new
		// TimeDivisionController(stepTimeInterval,
		// stepDuration);
		// future = executor.submit(new
		// TemporalTickerRetrieverCallable(timeDivisionController));
		//
		// temporalTickersByTimeInterval = future.get();
		// updateHouseThread(houseSimulationThread, temporalTickersByTimeInterval);
		//
		// }
		// finishExecution(houseSimulationThread);
		// logAccountsBalance(true);
		// LOGGER.info("Simulation finished.");
		// } catch (InterruptedException | ExecutionException exception) {
		// throw new RuntimeException(exception);
		// } finally {
		// executor.shutdown();
		// }
	}

	private void finishExecution(HouseSimulationThread houseSimulationThread) {
		try {
			Semaphores.getInstance()
					.getUpdateSemaphore()
					.acquire();
			houseSimulationThread.setFinished(true);
			Semaphores.getInstance()
					.getRunSimulationSemaphore()
					.release();
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void updateHouseThread(HouseSimulationThread houseSimulationThread,
			TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval) {
		try {
			Semaphores.getInstance()
					.getUpdateSemaphore()
					.acquire();
			houseSimulationThread.setTemporalTickers(temporalTickersDataModelsByTimeInterval);
			Semaphores.getInstance()
					.getRunSimulationSemaphore()
					.release();
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void logAccountsBalance(boolean printTotalWorth) {
		for (Account account : house.getAccounts()) {
			LOGGER.info("Account \"" + account.getOwner() + "\":");
			account.getWallet()
					.forEach(balance -> LOGGER.info("\t" + balance));

			if (printTotalWorth) {
				logTotalWorth(account);
			}
		}
	}

	private void logTotalWorth(Account account) {
		CurrencyAmount totalRealAmount = new CurrencyAmount(Currency.REAL, 0.0);
		for (Currency currency : Currency.values()) {
			Balance balance = ((Wallet)account.getWallet())
					.getBalanceFor(currency);
			if (currency.isDigital()) {
				TemporalTicker temporalTicker = house.getTemporalTickerFor(currency);
				if (temporalTicker != null) {
					totalRealAmount.setAmount(totalRealAmount.getAmount()
							.add(balance.getAmount()
									.multiply(temporalTicker.getCurrentOrPreviousLast())));
				}
			} else {
				totalRealAmount.setAmount(totalRealAmount.getAmount()
						.add(balance.getAmount()));
			}
		}
		LOGGER.info("\tTotal in " + Currency.REAL + ": " + totalRealAmount);
	}

	private void logSimulationStart() {
		LOGGER.info("Starting simulation from " + timeDivisionController + ".");
	}
}
