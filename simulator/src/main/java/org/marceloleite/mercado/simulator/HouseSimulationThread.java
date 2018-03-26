package org.marceloleite.mercado.simulator;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.data.TemporalTicker;

public class HouseSimulationThread extends Thread {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(HouseSimulationThread.class);

	private static final String THREAD_NAME = "House Simulation";

	private SimulationHouse house;
	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval;
	private Boolean finished;
	private Semaphore updateSemaphore;
	private Semaphore runSimulationSemaphore;

	public HouseSimulationThread(SimulationHouse house, Semaphore updateSemaphore, Semaphore runSimulationSemaphore) {
		super();
		this.house = house;
		this.temporalTickersDataModelsByTimeInterval = null;
		this.finished = false;
		this.updateSemaphore = updateSemaphore;
		this.runSimulationSemaphore = runSimulationSemaphore;
	}

	public void setTemporalTickersPOByTimeInterval(
			TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval) {
		this.temporalTickersDataModelsByTimeInterval = temporalTickersDataModelsByTimeInterval;
	}

	@Override
	public void run() {
		currentThread().setName(THREAD_NAME);

		while (!isFinished()) {
			aquireSemaphore();
			if (!isFinished()) {
				house.executeTemporalEvents(temporalTickersDataModelsByTimeInterval);
			}
			updateSemaphore.release();
		}
	}

	private void aquireSemaphore() {
		try {
			runSimulationSemaphore.acquire();
		} catch (InterruptedException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void setFinished(boolean finished) {
		synchronized (this.finished) {
			this.finished = finished;
		}
	}

	public Boolean isFinished() {
		synchronized (this.finished) {
			return finished;
		}
	}
}
