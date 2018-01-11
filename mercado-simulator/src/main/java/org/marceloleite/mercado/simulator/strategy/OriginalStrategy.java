package org.marceloleite.mercado.simulator.strategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.order.BuyOrder;

public class OriginalStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(OriginalStrategy.class);

	private Currency currency;

	public OriginalStrategy() {
	}

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount realAmount = account.getBalance().get(Currency.REAL);
		if (realAmount.getAmount() > 0) {
			CurrencyAmount currencyAmountToInvest = new CurrencyAmount(realAmount);
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
			BuyOrder buyOrder = new BuyOrder(simulationTimeInterval.getStart(), currencyAmountToBuy,
					currencyAmountToInvest);
			LOGGER.debug("Buy order created is " + buyOrder);
			account.getBuyOrdersTemporalController().add(buyOrder);
		}
	}
}
