package org.marceloleite.org.marceloleite.mercado.controller;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.methods.getaccountinfo.GetAccountInfoMethod;
import org.marceloleite.mercado.api.negotiation.methods.getaccountinfo.GetAccountInfoMethodResponse;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.base.model.Strategy;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.converter.entity.AccountPOToAccountDataConverter;
import org.marceloleite.mercado.converter.entity.ClassPOToClassDataConverter;
import org.marceloleite.mercado.converter.json.api.negotiation.BalanceApiToListBalanceDataConverter;
import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.data.ClassData;
import org.marceloleite.mercado.databaseretriever.persistence.daos.AccountDAO;
import org.marceloleite.mercado.databaseretriever.persistence.daos.ClassDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.AccountPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassPO;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.AccountInfo;
import org.marceloleite.mercado.xml.readers.AccountsXmlReader;

public class Controller {

	private static final Logger LOGGER = LogManager.getLogger(Controller.class);

	private static final String XML_DIRECTORY_PATH = "src/main/resources/xmls";

	private AccountDAO accountDAO;

	private House house;

	public Controller() {
		this.accountDAO = new AccountDAO();
		this.house = new ControllerHouse();
	}

	public void start() {
		List<Account> accounts = retrieveAccounts();
		while (!finished()) {
			waitNextMinute();
			LOGGER.debug("Checking.");
			check(accounts);
		}
	}

	private void check(List<Account> accounts) {
		if (accounts != null && !accounts.isEmpty()) {
			TimeInterval timeInterval = retrievePreviousMinuteInterval();
			house.updateTemporalTickers(timeInterval);
			checkAccounts(accounts, timeInterval);
		}
	}

	private void checkAccounts(List<Account> accounts, TimeInterval timeInterval) {
		for (Account account : accounts) {
			Map<Currency, List<Strategy>> currenciesStrategies = account.getCurrenciesStrategies();
			checkCurrenciesStrategies(timeInterval, account, currenciesStrategies);
		}
	}

	private void checkCurrenciesStrategies(TimeInterval timeInterval, Account account,
			Map<Currency, List<Strategy>> currenciesStrategies) {
		if (currenciesStrategies != null && !currenciesStrategies.isEmpty()) {
			for (Currency currency : currenciesStrategies.keySet()) {
				List<Strategy> strategies = currenciesStrategies.get(currency);
				checkStrategies(timeInterval, account, strategies);
				checkBuyOrders(timeInterval, account, house);
				checkSellOrders(timeInterval, account, house);
				saveStrategies(strategies);
			}
		}
	}

	private void saveStrategies(List<Strategy> strategies) {
		if (strategies != null && !strategies.isEmpty()) {
			ClassDAO classDAO = new ClassDAO();
			ClassPOToClassDataConverter classPOToClassDataConverter = new ClassPOToClassDataConverter();
			for (Strategy strategy : strategies) {
				ClassData classData = strategy.retrieveData();
				ClassPO classPO = classPOToClassDataConverter.convertFrom(classData);
				classDAO.merge(classPO);
			}
		}
	}

	private void checkBuyOrders(TimeInterval currentTimeInterval, Account account, House house) {
		OrderExecutor orderExecutor = house.getOrderExecutor();
		List<BuyOrder> buyOrders = account.getBuyOrdersTemporalController().retrieve(currentTimeInterval);
		if (buyOrders != null && !buyOrders.isEmpty()) {
			for (BuyOrder buyOrder : buyOrders) {
				orderExecutor.executeBuyOrder(buyOrder, house, account);
			}
		}
	}

	private void checkSellOrders(TimeInterval currentTimeInterval, Account account, House house) {
		OrderExecutor orderExecutor = house.getOrderExecutor();
		List<SellOrder> sellOrders = account.getSellOrdersTemporalController().retrieve(currentTimeInterval);
		if (sellOrders != null && !sellOrders.isEmpty()) {
			for (SellOrder sellOrder : sellOrders) {
				orderExecutor.executeSellOrder(sellOrder, house, account);
			}
		}
	}

	private void checkStrategies(TimeInterval timeInterval, Account account, List<Strategy> strategies) {
		if (strategies != null && !strategies.isEmpty()) {
			for (Strategy strategy : strategies) {
				strategy.check(timeInterval, account, house);
			}
		}
	}

	private void waitNextMinute() {
		ZonedDateTime now = ZonedDateTimeUtils.now();
		ZonedDateTime nextMinute = now.plusMinutes(1).minusSeconds(now.getSecond());
		Duration timeToWait = Duration.between(now, nextMinute);
		threadSleep(timeToWait);

	}

	private void threadSleep(Duration timeToWait) {
		try {
			Thread.sleep(timeToWait.getSeconds() * 1000);
		} catch (InterruptedException exception) {
			throw new RuntimeException("Exception occurred while waiting sleeping.", exception);
		}
	}

	private boolean finished() {
		return false;
	}

	private TimeInterval retrievePreviousMinuteInterval() {
		ZonedDateTime now = ZonedDateTimeUtils.now();
		ZonedDateTime endTime = now.minusSeconds(now.getSecond()).minusMinutes(1);
		ZonedDateTime startTime = endTime.minusMinutes(1);
		return new TimeInterval(startTime, endTime);
	}

	private List<Account> retrieveAccounts() {
		List<AccountData> accountDatas = new AccountsXmlReader(XML_DIRECTORY_PATH).readAccounts();
		List<Account> accounts = new ArrayList<>();
		if (accountDatas != null && !accountDatas.isEmpty()) {
			for (AccountData accountDataFromXml : accountDatas) {
				AccountPO accountPO = searchAccountOnDatabase(accountDataFromXml.getOwner());
				AccountData accountData;
				if (accountPO != null) {
					accountData = new AccountPOToAccountDataConverter().convertTo(accountPO);
				} else {
					accountData = accountDataFromXml;
					accountData = retrieveAccountBalance(accountData);
					saveAccountOnDatabase(accountData);
				}
				Account account = new Account(accountData);
				accounts.add(account);
			}
		}
		return accounts;
	}

	private AccountData retrieveAccountBalance(AccountData accountData) {
		TapiInformation tapiInformation = new TapiInformation(accountData.getTapiInformationData());
		GetAccountInfoMethodResponse getAccountInfoMethodResponse = new GetAccountInfoMethod(tapiInformation).execute();
		AccountInfo accountInfo = getAccountInfoMethodResponse.getResponse();
		BalanceApiToListBalanceDataConverter balanceApiToListBalanceDataConverter = new BalanceApiToListBalanceDataConverter();
		List<BalanceData> balanceDatas = balanceApiToListBalanceDataConverter.convertTo(accountInfo.getBalanceApi());
		balanceDatas.forEach(balanceData -> balanceData.setAccountData(accountData));
		accountData.setBalanceDatas(balanceDatas);
		return accountData;
	}

	private AccountPO searchAccountOnDatabase(String owner) {
		AccountPO accountPO = new AccountPO();
		accountPO.setOwner(owner);
		return accountDAO.findById(accountPO);
	}

	private void saveAccountOnDatabase(AccountData accountDataFromXml) {
		AccountPO accountPO = new AccountPOToAccountDataConverter().convertFrom(accountDataFromXml);
		accountDAO.persist(accountPO);
	}
}
