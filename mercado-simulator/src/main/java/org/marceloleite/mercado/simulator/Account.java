package org.marceloleite.mercado.simulator;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.simulator.converter.CurrencyAmountToStringConverter;
import org.marceloleite.mercado.simulator.order.BuyOrder;
import org.marceloleite.mercado.simulator.order.SellOrder;
import org.marceloleite.mercado.simulator.strategy.Strategy;
import org.marceloleite.mercado.simulator.structure.AccountData;
import org.marceloleite.mercado.simulator.structure.BuyOrderData;
import org.marceloleite.mercado.simulator.structure.DepositData;
import org.marceloleite.mercado.simulator.structure.SellOrderData;
import org.marceloleite.mercado.simulator.temporalcontroller.TemporalController;

public class Account {

	private static final Logger LOGGER = LogManager.getLogger(Account.class);

	private String owner;

	private Balance balance;

	private TemporalController<Deposit> depositsTemporalController;

	private TemporalController<BuyOrder> buyOrdersTemporalController;

	private TemporalController<SellOrder> sellOrdersTemporalController;

	private Map<Currency, List<Strategy>> currenciesStrategies;

	public Account(String owner, Balance balance, TemporalController<Deposit> depositsTemporalController,
			TemporalController<BuyOrder> buyOrdersTemporalController,
			TemporalController<SellOrder> sellOrdersTemporalController,
			Map<Currency, List<Strategy>> currenciesStrategies) {
		super();
		this.owner = owner;
		this.balance = balance;
		this.depositsTemporalController = depositsTemporalController;
		this.buyOrdersTemporalController = buyOrdersTemporalController;
		this.sellOrdersTemporalController = sellOrdersTemporalController;
		this.currenciesStrategies = currenciesStrategies;
	}

	public Account(AccountData accountData) {
		super();
		this.owner = accountData.getOwner();
		this.balance = new Balance(accountData.getBalanceData());
		this.depositsTemporalController = createDepositsTemporalController(accountData);
		this.buyOrdersTemporalController = createBuyOrdersTemporalController(accountData);
		this.sellOrdersTemporalController = createSellOrdersTemporalController(accountData);
		this.currenciesStrategies = new EnumMap<>(Currency.class);
	}

	private TemporalController<Deposit> createDepositsTemporalController(AccountData accountData) {
		TemporalController<Deposit> deposits = new TemporalController<>();
		for (DepositData depositData : accountData.getDepositDatas()) {
			deposits.add(new Deposit(depositData));
		}

		return deposits;
	}

	private TemporalController<BuyOrder> createBuyOrdersTemporalController(AccountData accountData) {
		TemporalController<BuyOrder> buyOrders = new TemporalController<>();
		for (BuyOrderData buyOrdersData : accountData.getBuyOrderDatas()) {
			buyOrders.add(new BuyOrder(buyOrdersData));
		}
		return buyOrders;
	}

	private TemporalController<SellOrder> createSellOrdersTemporalController(AccountData accountData) {
		TemporalController<SellOrder> sellOrders = new TemporalController<>();
		for (SellOrderData sellOrderData : accountData.getSellOrderDatas()) {
			sellOrders.add(new SellOrder(sellOrderData));
		}

		return sellOrders;
	}

	public Account(String owner) {
		this(owner, null, null, null, null, null);
	}

	public String getOwner() {
		return owner;
	}

	public Balance getBalance() {
		return balance;
	}

	public TemporalController<Deposit> getDepositsTemporalController() {
		return depositsTemporalController;
	}

	public TemporalController<BuyOrder> getBuyOrdersTemporalController() {
		return buyOrdersTemporalController;
	}

	public TemporalController<SellOrder> getSellOrdersTemporalController() {
		return sellOrdersTemporalController;
	}

	public Map<Currency, List<Strategy>> getCurrenciesStrategies() {
		return currenciesStrategies;
	}

	public void checkTimedEvents(TimeInterval currentTimeInterval) {
		checkDeposits(currentTimeInterval);
	}

	private void checkDeposits(TimeInterval currentTimeInterval) {
		List<Deposit> depositsToExecute = depositsTemporalController.retrieve(currentTimeInterval);
		CurrencyAmountToStringConverter currencyAmountToStringConverter = new CurrencyAmountToStringConverter();
		for (Deposit deposit : depositsToExecute) {
			LOGGER.info("Depositing " + currencyAmountToStringConverter.convertTo(deposit.getCurrencyAmount())
					+ " on \"" + owner + "\" account.");
			balance.deposit(deposit.getCurrencyAmount());
		}
	}

}