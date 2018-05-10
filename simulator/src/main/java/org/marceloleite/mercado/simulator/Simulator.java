package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.site.siteretriever.trade.TradeSiteRetriever;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Wallet;
import org.marceloleite.mercado.simulator.property.SimulatorPropertiesRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);
	
	private static final int THREAD_FINISH_WAIT_TIME_SECONDS = 10;

	@Inject
	private SimulatorPropertiesRetriever simulatorPropertiesRetriever;

	@Inject
	private SimulationHouse house;

	@Inject
	private TemporalTickerRetriever temporalTickerRetriever;

	@Inject
	private Semaphores semaphores;

	@Inject
	private HouseSimulationThread houseSimulationThread;

	private TimeDivisionController timeDivisionController;

	private Duration stepDuration;

	private void configure() {
		ZonedDateTime startTime = simulatorPropertiesRetriever.retrieveStartTime();
		ZonedDateTime endTime = simulatorPropertiesRetriever.retrieveEndTime();
		this.stepDuration = simulatorPropertiesRetriever.retrieveStepDuration();
		Duration retrievingDuration = simulatorPropertiesRetriever.retrieveRetrievingDuration();
		timeDivisionController = new TimeDivisionController(startTime, endTime, retrievingDuration);
		TradeSiteRetriever.setConfiguredStepDuration(simulatorPropertiesRetriever.retrieveTradeSiteDurationStep());
	}

	public void runSimulation() {
		configure();
		logSimulationStart();
		ExecutorService executorService = createExecutorService();
		try {
			executorService.execute(houseSimulationThread);

			TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval = null;
			for (TimeInterval stepTimeInterval : timeDivisionController.geTimeIntervals()) {
				LOGGER.info(stepTimeInterval);
				TimeDivisionController retrievalTimeDivisionController = new TimeDivisionController(stepTimeInterval,
						stepDuration);
				CompletableFuture<TreeMap<TimeInterval, Map<Currency, TemporalTicker>>> completableFuture = retrieveTemporalTickers(
						retrievalTimeDivisionController);
				CompletableFuture.allOf(completableFuture);
				temporalTickersByTimeInterval = completableFuture.get();
				updateHouseThread(houseSimulationThread, temporalTickersByTimeInterval);
			}
			finishExecution();
			logAccountsBalance(true);
			LOGGER.info("Simulation finished.");
		} catch (InterruptedException | ExecutionException exception) {
			abortExecution();
			throw new RuntimeException(exception);
		} finally {
			executorService.shutdown();
		}
	}

	private ExecutorService createExecutorService() {
		ExecutorService executorService = Executors
				.newFixedThreadPool(simulatorPropertiesRetriever.retrieveThreadPoolSize());
		return executorService;
	}

	private void abortExecution() {
		houseSimulationThread.setFinished(true);
		semaphores.getRunSimulationSemaphore()
				.release();
	}

	private void finishExecution() {
		try {
			semaphores.getUpdateSemaphore()
					.acquire();
			houseSimulationThread.setFinished(true);
			semaphores.getRunSimulationSemaphore()
					.release();
			semaphores.getFinishSemaphore().tryAcquire(THREAD_FINISH_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void updateHouseThread(HouseSimulationThread houseSimulationThread,
			TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval) {
		try {
			semaphores.getUpdateSemaphore()
					.acquire();
			houseSimulationThread.setTemporalTickers(temporalTickersDataModelsByTimeInterval);
			semaphores.getRunSimulationSemaphore()
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
			Balance balance = ((Wallet) account.getWallet()).getBalanceFor(currency);
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
		logAccountsBalance(false);
	}

	@Async
	public CompletableFuture<TreeMap<TimeInterval, Map<Currency, TemporalTicker>>> retrieveTemporalTickers(
			TimeDivisionController timeDivisionController) {
		return CompletableFuture
				.completedFuture(temporalTickerRetriever.retrieveTemporalTickers(timeDivisionController));
	}

	@Bean
	public HouseSimulationThread createHouseSimulationThread() {
		return new HouseSimulationThread(house);
	}
}
