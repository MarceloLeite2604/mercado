package org.marceloleite.mercado.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.properties.Property;

public class OriginalStrategy extends AbstractStrategy {

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

	@Override
	protected Property retrieveVariable(String name) {
		return null;
	}

	@Override
	protected void defineVariable(Property variable) {
		
	}

	@Override
	protected void defineParameter(Property parameter) {
		
	}
}
