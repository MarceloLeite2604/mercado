package org.marceloleite.mercado.base.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.converter.CurrencyAmountToStringConverter;
import org.marceloleite.mercado.base.model.converter.ListParameterDatasToListPropertyConverter;
import org.marceloleite.mercado.base.model.converter.ListVariableDatasToListPropertyConverter;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder;
import org.marceloleite.mercado.base.model.temporalcontroller.TemporalController;
import org.marceloleite.mercado.base.model.util.StrategyClassLoader;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.data.ClassData;
import org.marceloleite.mercado.data.DepositData;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.data.StrategyData;
import org.marceloleite.mercado.data.TapiInformationData;

public class Account {

	private static final Logger LOGGER = LogManager.getLogger(Account.class);

	private String owner;

	private Balance balance;

	private TapiInformation tapiInformation;

	private String email;

	private TemporalController<Deposit> depositsTemporalController;

	private TemporalController<Order> buyOrdersTemporalController;

	private TemporalController<Order> sellOrdersTemporalController;

	private Map<Currency, List<Strategy>> currenciesStrategies;

	public Account(String owner, TapiInformation tapiInformation, String email, Balance balance,
			TemporalController<Deposit> depositsTemporalController,
			TemporalController<Order> buyOrdersTemporalController,
			TemporalController<Order> sellOrdersTemporalController,
			Map<Currency, List<Strategy>> currenciesStrategies) {
		super();
		this.owner = owner;
		this.tapiInformation = tapiInformation;
		this.balance = balance;
		this.depositsTemporalController = depositsTemporalController;
		this.buyOrdersTemporalController = buyOrdersTemporalController;
		this.sellOrdersTemporalController = sellOrdersTemporalController;
		this.currenciesStrategies = currenciesStrategies;
	}

	public Account(AccountData accountData) {
		super();
		this.owner = accountData.getOwner();
		TapiInformationData tapiInformationData = accountData.getTapiInformationData();
		this.tapiInformation = new TapiInformation(tapiInformationData);
		this.email = accountData.getEmail();
		this.balance = new Balance(accountData.getBalanceDatas());
		this.depositsTemporalController = createDepositsTemporalController(accountData);
		this.buyOrdersTemporalController = createBuyOrdersTemporalController(accountData);
		this.sellOrdersTemporalController = createSellOrdersTemporalController(accountData);
		this.currenciesStrategies = createCurrenciesStrategies(accountData);
	}

	public Account(Account account) {
		this(new String(account.getOwner()), account.getTapiInformation(), account.getEmail(),
				new Balance(account.getBalance()),
				new TemporalController<Deposit>(account.getDepositsTemporalController()),
				new TemporalController<>(account.getBuyOrdersTemporalController()),
				new TemporalController<>(account.getSellOrdersTemporalController()),
				new EnumMap<Currency, List<Strategy>>(account.getCurrenciesStrategies()));
	}

	public Account(String owner) {
		this(owner, null, null, null, null, null, null, null);
	}

	private TemporalController<Deposit> createDepositsTemporalController(AccountData accountData) {
		TemporalController<Deposit> deposits = new TemporalController<>();
		List<DepositData> depositDatas = accountData.getDepositDatas();
		if (depositDatas != null && !depositDatas.isEmpty()) {
			for (DepositData depositData : depositDatas) {
				deposits.add(new Deposit(depositData));
			}
		}

		return deposits;
	}

	private TemporalController<Order> createBuyOrdersTemporalController(AccountData accountData) {
		TemporalController<Order> buyOrders = new TemporalController<>();
		BuyOrderBuilder buyOrderBuilder = new BuyOrderBuilder();
		List<OrderData> buyOrderDatas = accountData.getBuyOrderDatas();
		if (buyOrderDatas != null && !buyOrderDatas.isEmpty()) {
			for (OrderData orderData : buyOrderDatas) {
				CurrencyAmount currencyAmountToBuy = new CurrencyAmount(orderData.getSecondCurrency(), orderData.getQuantity());
				CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(orderData.getFirstCurrency(), orderData.getLimitPrice());
				Order order = buyOrderBuilder.toExecuteOn(orderData.getIntended()).buying(currencyAmountToBuy)
						.payingUnitPriceOf(currencyAmountUnitPrice).build();
				buyOrders.add(order);
			}
		}
		return buyOrders;
	}

	private TemporalController<Order> createSellOrdersTemporalController(AccountData accountData) {
		TemporalController<Order> sellOrders = new TemporalController<>();
		SellOrderBuilder sellOrderBuilder = new SellOrderBuilder();
		List<OrderData> sellOrderDatas = accountData.getSellOrderDatas();
		if (sellOrderDatas != null && !sellOrderDatas.isEmpty()) {
			for (OrderData orderData : sellOrderDatas) {
				CurrencyAmount currencyAmountToSell = new CurrencyAmount(orderData.getSecondCurrency(), orderData.getQuantity());
				CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(orderData.getFirstCurrency(), orderData.getLimitPrice());
				Order order = sellOrderBuilder.toExecuteOn(orderData.getIntended())
						.selling(currencyAmountToSell).receivingUnitPriceOf(currencyAmountUnitPrice).build();
				sellOrders.add(order);
			}
		}
		return sellOrders;
	}

	private Map<Currency, List<Strategy>> createCurrenciesStrategies(AccountData accountData) {
		Map<Currency, List<Strategy>> currenciesStrategies = new EnumMap<Currency, List<Strategy>>(Currency.class);
		StrategyClassLoader strategyLoader = new StrategyClassLoader();

		List<StrategyData> strategyDatas = accountData.getStrategyDatas();
		ListParameterDatasToListPropertyConverter listParameterDatasToListPropertyConverter = new ListParameterDatasToListPropertyConverter();
		ListVariableDatasToListPropertyConverter listVariableDatasToListPropertyConverter = new ListVariableDatasToListPropertyConverter();

		if (strategyDatas != null && !strategyDatas.isEmpty()) {
			for (StrategyData strategyData : strategyDatas) {
				List<Strategy> strategies = new ArrayList<>();
				List<ClassData> classDatas = strategyData.getClassDatas();
				Currency currency = strategyData.getCurrency();
				for (ClassData classData : classDatas) {
					Strategy strategy = strategyLoader.load(classData.getName().trim(), currency);

					List<Property> properties = listParameterDatasToListPropertyConverter
							.convertTo(classData.getParameterDatas());
					strategy.setParameters(properties);

					List<Property> variables = listVariableDatasToListPropertyConverter
							.convertTo(classData.getVariableDatas());
					if (variables != null && !variables.isEmpty()) {
						strategy.setVariables(variables);
					}

					strategies.add(strategy);
				}
				currenciesStrategies.put(currency, strategies);
			}
		}
		return currenciesStrategies;
	}

	public String getOwner() {
		return owner;
	}

	public TapiInformation getTapiInformation() {
		return tapiInformation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public TemporalController<Deposit> getDepositsTemporalController() {
		return depositsTemporalController;
	}

	public TemporalController<Order> getBuyOrdersTemporalController() {
		return buyOrdersTemporalController;
	}

	public TemporalController<Order> getSellOrdersTemporalController() {
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

	public AccountData retrieveData() {
		AccountData accountData = new AccountData();

		accountData.setEmail(email);
		accountData.setOwner(owner);

		List<BalanceData> balanceDatas = balance.retrieveData();
		balanceDatas.forEach(balanceData -> balanceData.setAccountData(accountData));
		accountData.setBalanceDatas(balanceDatas);

		List<StrategyData> strategyDatas = retrieveStrategyDatas();
		strategyDatas.forEach(strategyData -> strategyData.setAccountData(accountData));
		accountData.setStrategyDatas(strategyDatas);

		TapiInformationData tapiInformationData = tapiInformation.retrieveData();
		tapiInformationData.setAccountData(accountData);
		accountData.setTapiInformationData(tapiInformationData);

		return accountData;
	}

	private List<StrategyData> retrieveStrategyDatas() {
		List<StrategyData> strategyDatas = new ArrayList<>();
		Set<Currency> currencies = currenciesStrategies.keySet();
		if (currencies != null && !currencies.isEmpty()) {
			for (Currency currency : currencies) {
				StrategyData strategyData = new StrategyData();
				strategyData.setCurrency(currency);
				List<Strategy> strategies = currenciesStrategies.get(currency);
				List<ClassData> classDatas = new ArrayList<>();
				if (strategies != null && !strategies.isEmpty()) {
					for (Strategy strategy : strategies) {
						ClassData classData = strategy.retrieveData();
						classData.setStrategyData(strategyData);
						classDatas.add(classData);
					}
					strategyData.setClassDatas(classDatas);
				}
				strategyDatas.add(strategyData);
			}
		}
		return strategyDatas;
	}
}