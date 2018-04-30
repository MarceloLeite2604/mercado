package org.marceloleite.mercado.simulator;

import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.TemporalTicker;

public class HouseSimulationThread extends Thread {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(HouseSimulationThread.class);

	private static final String THREAD_NAME = "House Simulation";

	private SimulationHouse house;
	
	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersDataModelsByTimeInterval;
	
	private Boolean finished;
	
	public HouseSimulationThread(SimulationHouse house) {
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
			Semaphores.getInstance().getUpdateSemaphore().release();
		}
		house.afterFinish();
	}

	private void aquireSemaphore() {
		try {
			Semaphores.getInstance().getRunSimulationSemaphore().acquire();
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
