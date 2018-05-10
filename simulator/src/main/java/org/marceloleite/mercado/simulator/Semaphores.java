package org.marceloleite.mercado.simulator;

import java.util.concurrent.Semaphore;

import org.springframework.stereotype.Component;

@Component
public class Semaphores {

	private Semaphore updateSemaphore;

	private Semaphore runSimulationSemaphore;
	
	private Semaphore finishSemaphore;

	public Semaphores() {
		this.updateSemaphore = new Semaphore(1);
		this.runSimulationSemaphore = new Semaphore(0);
		this.finishSemaphore = new Semaphore(0);
	}

	public synchronized Semaphore getUpdateSemaphore() {
		return updateSemaphore;
	}

	public synchronized Semaphore getRunSimulationSemaphore() {
		return runSimulationSemaphore;
	}

	public Semaphore getFinishSemaphore() {
		return finishSemaphore;
	}
}
