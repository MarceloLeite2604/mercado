package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.simulator.converter.CurrencyAmountToStringConverter;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder.SellOrder;
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
		this.currenciesStrategies = createCurrenciesStrategies(accountData);
	}

	public Account(Account account) {
		this(new String(account.getOwner()), new Balance(account.getBalance()),
				new TemporalController<Deposit>(account.getDepositsTemporalController()),
				new TemporalController<>(account.getBuyOrdersTemporalController()),
				new TemporalController<>(account.getSellOrdersTemporalController()),
				new EnumMap<Currency, List<Strategy>>(account.getCurrenciesStrategies()));
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
		BuyOrderBuilder buyOrderBuilder = new BuyOrderBuilder();
		for (BuyOrderData buyOrderData : accountData.getBuyOrderDatas()) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(buyOrderData.getCurrencyAmountToBuy());
			CurrencyAmount currencyAmountToPay = new CurrencyAmount(buyOrderData.getCurrencyAmountToPay());
			BuyOrder buyOrder = buyOrderBuilder.toExecuteOn(buyOrderData.getTime()).buying(currencyAmountToBuy).paying(currencyAmountToPay).build();
			buyOrders.add(buyOrder);
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
	
	private Map<Currency, List<Strategy>> createCurrenciesStrategies(AccountData accountData) {
		Map<Currency, List<String>> currenciesStrategyClassnames = accountData.getCurrenciesStrategies();
		Map<Currency, List<Strategy>> currenciesStrategies = new EnumMap<Currency, List<Strategy>>(Currency.class);
		StrategyLoader strategyLoader = new StrategyLoader();
		if (currenciesStrategyClassnames != null && !currenciesStrategyClassnames.isEmpty()) {
			for (Currency currency : currenciesStrategyClassnames.keySet()) {
				List<String> strategiesClassNames = currenciesStrategyClassnames.get(currency);
				if ( strategiesClassNames != null && !strategiesClassNames.isEmpty()) {
					List<Strategy> strategies = new ArrayList<>();
					for (String strategyClassName : strategiesClassNames) {
						Strategy strategy = strategyLoader.load(strategyClassName.trim(), currency);
						strategies.add(strategy);
					}
					currenciesStrategies.put(currency, strategies);
				}
			}
		}
		return currenciesStrategies;
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