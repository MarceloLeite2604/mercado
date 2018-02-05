package org.marceloleite.mercado.simulator.strategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;

public class OriginalStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(OriginalStrategy.class);

	private Currency currency;

	public OriginalStrategy(Currency currency) {
		this.currency = currency;
	}

	public OriginalStrategy() {
	}

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		if (house.getTemporalTickers().get(currency) != null) {
			CurrencyAmount realAmount = account.getBalance().get(Currency.REAL);
			if (realAmount.getAmount() > 0) {
				CurrencyAmount currencyAmountToPay = new CurrencyAmount(realAmount);
				CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
				BuyOrder buyOrder = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
						.buying(currencyAmountToBuy).paying(currencyAmountToPay).build();
				LOGGER.debug("Buy order created is " + buyOrder);
				account.getBuyOrdersTemporalController().add(buyOrder);
			}
		}
	}
}
