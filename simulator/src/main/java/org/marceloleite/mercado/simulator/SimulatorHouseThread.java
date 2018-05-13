package org.marceloleite.mercado.simulator;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.TemporalTicker;

public class SimulatorHouseThread extends Thread {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(SimulatorHouseThread.class);

	private static final String THREAD_NAME = "Simulation House";

	private SimulationHouse house;
	
	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval;
	
	private Boolean finished;
	
	@Inject
	private Semaphores semaphores; 
	
	public SimulatorHouseThread(SimulationHouse house) {
		super();
		this.house = house;
		this.temporalTickersDataModelsByTimeInterval = null;
		this.finished = false;
	}

	public void setTemporalTickers(
			TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval) {
		this.temporalTickersDataModelsByTimeInterval = temporalTickersDataModelsByTimeInterval;
	}

	@Override
	public void run() {
		currentThread().setName(THREAD_NAME);

		house.beforeStart();
		
		while (!isFinished()) {
			aquireSemaphore();
			if (!isFinished()) {
				house.process(temporalTickersDataModelsByTimeInterval);
			}
			semaphores.getUpdateSemaphore().release();
		}
		house.afterFinish();
		semaphores.getFinishSemaphore().release();
	}

	private void aquireSemaphore() {
		try {
			semaphores.getRunSimulationSemaphore().acquire();
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
