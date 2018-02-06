package org.marceloleite.mercado.simulator;

import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class HouseSimulationThread extends Thread {
	
	private House house;
	private Map<TimeInterval, Map<Currency, TemporalTickerPO>> temporalTickersPOByTimeInterval;
	
	public HouseSimulationThread(House house, Map<TimeInterval, Map<Currency, TemporalTickerPO>> temporalTickersPOByTimeInterval) {
		super();
		this.house = house;
		this.temporalTickersPOByTimeInterval = temporalTickersPOByTimeInterval;
	}

	@Override
	public void run() {
		house.executeTemporalEvents(temporalTickersPOByTimeInterval);
	}

	
}
