package org.marceloleite.mercado.simulator;

import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class HouseSimulationThread extends Thread {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(HouseSimulationThread.class);

	private static final String THREAD_NAME = "House Simulation";

	private House house;
	private Map<TimeInterval, Map<Currency, TemporalTickerPO>> temporalTickersPOByTimeInterval;
	private Boolean finished;
	private Semaphore semaphore;

	public HouseSimulationThread(House house, Semaphore semaphore) {
		super();
		this.house = house;
		this.temporalTickersPOByTimeInterval = null;
		this.finished = false;
		this.semaphore = semaphore;
	}

	public void setTemporalTickersPOByTimeInterval(
			Map<TimeInterval, Map<Currency, TemporalTickerPO>> temporalTickersPOByTimeInterval) {
		this.temporalTickersPOByTimeInterval = temporalTickersPOByTimeInterval;
	}

	@Override
	public void run() {
		currentThread().setName(THREAD_NAME);

		while (!isFinished()) {
			aquireSemaphore();
			if (!isFinished()) {
				house.executeTemporalEvents(temporalTickersPOByTimeInterval);
			}
			semaphore.release();
		}
	}

	private void aquireSemaphore() {
		try {
			semaphore.acquire();
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
