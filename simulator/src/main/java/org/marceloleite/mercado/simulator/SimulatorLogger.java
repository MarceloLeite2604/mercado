package org.marceloleite.mercado.simulator;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.stereotype.Component;

@Component
public class SimulatorLogger {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	@Inject
	private SimulationHouse simulationHouse;

	@Inject
	private SimulatorConfigurator simulationConfigurator;

	public void logStart() {
		LOGGER.info("Starting simulation from " + simulationConfigurator.getTimeDivisionController()
				.getTotalTimeInterval() + ".");
		logAccountsBalance(false);
	}

	public void logAccountsBalance(boolean printTotalWorth) {
		for (Account account : simulationHouse.getAccounts()) {
			LOGGER.info("Account \"" + account.getOwner() + "\":");
			account.getWallet()
					.forEach(balance -> LOGGER.info("\t" + balance));

			if (printTotalWorth) {
				logTotalWorth(account);
			}
		}
	}

	private void logTotalWorth(Account account) {
		CurrencyAmount totalRealAmount = new CurrencyAmount(Currency.REAL, 0.0);
		for (Currency currency : Currency.values()) {
			Balance balance = account.getWallet().getBalanceFor(currency);
			if (currency.isDigital()) {
				TemporalTicker temporalTicker = simulationHouse.getTemporalTickerFor(currency);
				if (temporalTicker != null && temporalTicker.getCurrentOrPreviousLast() != null) {
					totalRealAmount.setAmount(totalRealAmount.getAmount()
							.add(balance.getAmount()
									.multiply(temporalTicker.getCurrentOrPreviousLast())));
				}
			} else {
				totalRealAmount.setAmount(totalRealAmount.getAmount()
						.add(balance.getAmount()));
			}
		}
		LOGGER.info("\tTotal in " + Currency.REAL + ": " + totalRealAmount);
	}
}
