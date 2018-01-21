package org.marceloleite.mercado.simulator.strategy.first;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.OrderType;

public class BuySellStep {

	private static final Logger LOGGER = LogManager.getLogger(BuySellStep.class);

	private long buySteps;

	private long sellSteps;

	private long currentStep;

	public BuySellStep(long buySteps, long sellDivisions) {
		super();
		this.buySteps = buySteps;
		this.sellSteps = sellDivisions;
		this.currentStep = 0l;
	}
	
	public long getCurrentStep() {
		return currentStep;
	}

	public long updateStep(OrderType orderType) {
		Long result = null;
		switch (orderType) {
		case BUY:
			result = calculateNextBuyStep();
			break;
		case SELL:
			result = calculateNextSellStep();
			break;
		}
		LOGGER.debug("Next step is: " + result + ".");
		currentStep = result;
		return currentStep;
	}

	private long calculateNextBuyStep() {
		long result;
		if (currentStep <= 0l) {
			result = 1l;
		} else {
			if (currentStep < buySteps) {
				result = currentStep + 1;
			} else {
				result = currentStep;
			}
		}
		LOGGER.debug("Next buy step is " + result + ".");
		return result;
	}

	private long calculateNextSellStep() {
		long result;
		if (currentStep > 0l) {
			result = -1l;
		} else {
			if (currentStep > -sellSteps) {
				result = currentStep - 1;
			} else {
				result = currentStep;
			}
		}
		LOGGER.debug("Next sell step is " + result + ".");
		return result;
	}
}
