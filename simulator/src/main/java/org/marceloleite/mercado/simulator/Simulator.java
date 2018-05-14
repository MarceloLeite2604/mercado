package org.marceloleite.mercado.simulator;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	private static final int THREAD_FINISH_WAIT_TIME_SECONDS = 10;

	@Inject
	private Semaphores semaphores;

	@Inject
	private SimulatorHouseThread simulatorHouseThread;

	@Inject
	private SimulatorLogger simulatorLogger;

	@Inject
	private SimulatorConfigurator simulatorConfigurator;

	@Inject
	private TaskExecutor taskExecutor;

	@Inject
	private TemporalTickerRetriever temporalTickerRetriever;

	public void run() {
		try {
			execute();
		} catch (Exception exception) {
			abort();
			throw new RuntimeException("Error while running simumlation.", exception);
		} finally {
			// executorService.shutdown();
		}
	}

	private void execute() throws InterruptedException, ExecutionException {
		startExecution();
		for (TimeInterval stepTimeInterval : simulatorConfigurator.getTimeDivisionController()
				.getTimeIntervals()) {
			LOGGER.info(stepTimeInterval);
			TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval = retrieveTemporalTickers(
					stepTimeInterval);
			updateHouseThread(simulatorHouseThread, temporalTickersByTimeInterval);
		}
		finishExecution();
	}

	private void startExecution() {
		simulatorLogger.logStart();
		taskExecutor.execute(simulatorHouseThread);
	}

	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> retrieveTemporalTickers(TimeInterval timeInterval)
			throws InterruptedException, ExecutionException {
		TimeDivisionController timeDivisionController = new TimeDivisionController(timeInterval,
				simulatorConfigurator.getStepDuration());
		// return taskExecutor.submit(new
		// TemporalTickerRetrieverCallable(timeDivisionController))
		// .get();
		return createCompletableFuture(timeDivisionController).get();
	}

	@Async
	@Transactional
	public CompletableFuture<TreeMap<TimeInterval, Map<Currency, TemporalTicker>>> createCompletableFuture(
			TimeDivisionController timeDivisionController) {
		return CompletableFuture
				.completedFuture(temporalTickerRetriever.retrieveTemporalTickers(timeDivisionController));
	}

	private void abort() {
		simulatorHouseThread.setFinished(true);
		semaphores.getRunSimulationSemaphore()
				.release();
	}

	private void finishExecution() {
		try {
			semaphores.getUpdateSemaphore()
					.acquire();
			simulatorHouseThread.setFinished(true);
			semaphores.getRunSimulationSemaphore()
					.release();
			semaphores.getFinishSemaphore()
					.tryAcquire(THREAD_FINISH_WAIT_TIME_SECONDS, TimeUnit.SECONDS);
			simulatorLogger.logAccountsBalance(true);
			LOGGER.info("Simulation finished.");
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void updateHouseThread(SimulatorHouseThread houseSimulationThread,
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
}
