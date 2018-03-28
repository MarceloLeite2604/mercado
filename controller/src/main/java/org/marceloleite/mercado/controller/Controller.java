package org.marceloleite.mercado.controller;

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
import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Strategy;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.controller.properties.ControllerPropertiesRetriever;
import org.marceloleite.mercado.converter.entity.AccountPOToAccountDataConverter;
import org.marceloleite.mercado.converter.json.api.negotiation.BalanceApiToListBalanceDataConverter;
import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.persistence.daos.AccountDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.AccountPO;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.AccountInfo;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;
import org.marceloleite.mercado.xml.readers.AccountsXmlReader;

public class Controller {

	private static final Logger LOGGER = LogManager.getLogger(Controller.class);

	private AccountDAO accountDAO;

	private House house;

	private ControllerPropertiesRetriever controllerPropertiesRetriever;

	public Controller() {
		this.controllerPropertiesRetriever = new ControllerPropertiesRetriever();
		configureEntityManagerController();
		configureTradesSiteRetriever();
		this.accountDAO = new AccountDAO();
		this.house = new ControllerHouse();

	}

	private void configureTradesSiteRetriever() {
		Duration tradesSiteRetrieverDurationStep = controllerPropertiesRetriever
				.retrieveTradesSiteRetrieverDurationStep();
		if (tradesSiteRetrieverDurationStep != null) {
			TradesSiteRetriever.setConfiguredStepDuration(tradesSiteRetrieverDurationStep);
		}

		Integer tradesSiteRetrieverThreadPoolSize = controllerPropertiesRetriever
				.retrieveTradesSiteRetrieverThreadPoolSize();
		if (tradesSiteRetrieverThreadPoolSize != null) {
			TradesSiteRetriever.setConfiguredThreadPoolSize(tradesSiteRetrieverThreadPoolSize);
		}
	}

	public void start() {
		List<Account> accounts = retrieveAccounts();
		while (!finished()) {
			waitNextMinute();
			LOGGER.debug("Checking.");
			check(accounts);
		}
	}

	private void configureEntityManagerController() {
		String persistenceFileName = controllerPropertiesRetriever.retrievePersistenceFileName();
		if (persistenceFileName != null) {
			EntityManagerController.setPersistenceFileName(persistenceFileName);
		}
	}

	private void check(List<Account> accounts) {
		if (accounts != null && !accounts.isEmpty()) {
			TimeInterval timeInterval = retrievePreviousMinuteInterval();
			house.updateTemporalTickers(timeInterval);
			LOGGER.debug(house.getTemporalTickers().get(Currency.BITCOIN));
			checkAccounts(accounts, timeInterval);
		}
	}

	private void checkAccounts(List<Account> accounts, TimeInterval timeInterval) {
		for (Account account : accounts) {
			Map<Currency, List<Strategy>> currenciesStrategies = account.getCurrenciesStrategies();
			checkCurrenciesStrategies(timeInterval, account, currenciesStrategies);
		}
		save(accounts);
	}

	private void checkCurrenciesStrategies(TimeInterval timeInterval, Account account,
			Map<Currency, List<Strategy>> currenciesStrategies) {
		if (currenciesStrategies != null && !currenciesStrategies.isEmpty()) {
			for (Currency currency : currenciesStrategies.keySet()) {
				List<Strategy> strategies = currenciesStrategies.get(currency);
				checkStrategies(timeInterval, account, strategies);
				List<BalanceData> balanceDatas = retrieveAccountBalance(account.retrieveData());
				account.setBalance(new Balance(balanceDatas));
			}
		}
	}

	private void save(List<Account> accounts) {
		AccountPOToAccountDataConverter accountPOToAccountDataConverter = new AccountPOToAccountDataConverter();
		if (accounts != null && !accounts.isEmpty()) {
			for (Account account : accounts) {
				AccountData accountData = account.retrieveData();
				AccountPO accountPO = accountPOToAccountDataConverter.convertFrom(accountData);
				accountDAO.merge(accountPO);
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
		String xmlDirectoryPath = controllerPropertiesRetriever.retrieveXmlDirectoryPath();
		List<AccountData> accountDatas = new AccountsXmlReader(xmlDirectoryPath).readAccounts();
		List<Account> accounts = new ArrayList<>();
		if (accountDatas != null && !accountDatas.isEmpty()) {
			for (AccountData accountDataFromXml : accountDatas) {
				AccountPO accountPO = searchAccountOnDatabase(accountDataFromXml.getOwner());
				AccountData accountData;
				if (accountPO != null) {
					accountData = new AccountPOToAccountDataConverter().convertTo(accountPO);
				} else {
					accountData = accountDataFromXml;
					List<BalanceData> balanceDatas = retrieveAccountBalance(accountData);
					accountData.setBalanceDatas(balanceDatas);
					saveAccountOnDatabase(accountData);
				}
				Account account = new Account(accountData);
				accounts.add(account);
			}
		}
		return accounts;
	}

	private List<BalanceData> retrieveAccountBalance(AccountData accountData) {
		TapiInformation tapiInformation = new TapiInformation(accountData.getTapiInformationData());
		GetAccountInfoMethodResponse getAccountInfoMethodResponse = new GetAccountInfoMethod(tapiInformation).execute();
		AccountInfo accountInfo = getAccountInfoMethodResponse.getResponse();
		BalanceApiToListBalanceDataConverter balanceApiToListBalanceDataConverter = new BalanceApiToListBalanceDataConverter();
		List<BalanceData> balanceDatas = balanceApiToListBalanceDataConverter.convertTo(accountInfo.getBalanceApi());
		accountData.setBalanceDatas(balanceDatas);
		return balanceDatas;
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
