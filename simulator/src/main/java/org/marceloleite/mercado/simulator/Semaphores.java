package org.marceloleite.mercado.simulator;

import java.util.concurrent.Semaphore;

public class Semaphores {

	private static Semaphores instance;

	private Semaphore updateSemaphore;

	private Semaphore runSimulationSemaphore;

	private Semaphores() {
		this.updateSemaphore = new Semaphore(1);
		this.runSimulationSemaphore = new Semaphore(0);
	}

	public synchronized Semaphore getUpdateSemaphore() {
		return updateSemaphore;
	}

	public synchronized Semaphore getRunSimulationSemaphore() {
		return runSimulationSemaphore;
	}

	public static synchronized Semaphores getInstance() {
		if (instance == null) {
			instance = new Semaphores();
		}
		return instance;
	}
}
