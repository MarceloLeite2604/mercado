package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.TimeDivisionControllerToStringConverter;
import org.marceloleite.mercado.commons.util.converter.TimeIntervalToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.property.SimulatorPropertiesRetriever;

public class Simulator {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	private House house;

	private TimeDivisionController timeDivisionController;

	public Simulator() {
		this.house = new House();
	}

	private void configure() {
		SimulatorPropertiesRetriever simulatorPropertiesRetriever = new SimulatorPropertiesRetriever();
		LocalDateTime startTime = simulatorPropertiesRetriever.retrieveStartTime();
		LocalDateTime endTime = simulatorPropertiesRetriever.retrieveEndTime();
		Duration stepTime = simulatorPropertiesRetriever.retrieveStepDurationTime();
		timeDivisionController = new TimeDivisionController(startTime, endTime, stepTime);
		this.house = new House();
	}

	public void runSimulation() {
		LOGGER.traceEntry();

		configure();
		logSimulationStart();

		logAccountsBalance(false);

		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			logSimulationStep(timeInterval);
			house.executeTemporalEvents(timeInterval);
		}

		logAccountsBalance(true);

		LOGGER.info("Simulation finished.");
	}

	private void logAccountsBalance(boolean printTotalWorth) {
		for (Account account : house.getAccounts()) {
			LOGGER.info("Account \"" + account.getOwner() + "\":");
			Balance balance = account.getBalance();

			for (CurrencyAmount currencyAmount : balance.values()) {
				LOGGER.info("\t" + currencyAmount);
			}

			if (printTotalWorth) {
				logTotalWorth(account);
			}
		}
	}

	private void logTotalWorth(Account account) {
		CurrencyAmount totalRealAmount = new CurrencyAmount(Currency.REAL, 0.0);
		Balance balance = account.getBalance();

		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				TemporalTickerPO temporalTickerPO = house.getTemporalTickers().get(currency);
				if ( temporalTickerPO != null ) {
					CurrencyAmount currencyAmount = balance.get(currency);
					if (currencyAmount != null) {
						totalRealAmount.setAmount(
								totalRealAmount.getAmount() + (currencyAmount.getAmount() * temporalTickerPO.getAverage()));
					}
				}
			} else {
				CurrencyAmount currencyAmount = balance.get(currency);
				if (currencyAmount != null) {
					totalRealAmount.setAmount(
							totalRealAmount.getAmount() + (currencyAmount.getAmount() ));
				}
			}
		}
		LOGGER.info("\tTotal in " + Currency.REAL + ": " + totalRealAmount);
	}

	private void logSimulationStep(TimeInterval timeInterval) {
		TimeIntervalToStringConverter timeIntervalToStringConverter = new TimeIntervalToStringConverter();
		LOGGER.debug("Advancing to step time " + timeIntervalToStringConverter.convertTo(timeInterval) + ".");
	}

	private void logSimulationStart() {
		TimeDivisionControllerToStringConverter timeDivisionControllerToStringConverter = new TimeDivisionControllerToStringConverter();
		LOGGER.info("Starting simulation from "
				+ timeDivisionControllerToStringConverter.convertTo(timeDivisionController) + ".");
	}
}
