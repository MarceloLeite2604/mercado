package org.marceloleite.org.marceloleite.mercado.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.converter.entity.AccountPOToAccountDataConverter;
import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.databaseretriever.persistence.daos.AccountDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.AccountPO;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;
import org.marceloleite.mercado.xml.readers.AccountsXmlReader;

public class Controller 
{
	
	private static final String XML_DIRECTORY_PATH = "src/main/resources/xmls";
	
    private List<Account> accounts;
    
    private TemporalTickerRetriever temporalTickerRetriever;

	private AccountDAO accountDAO;
    
    public Controller() {
		this.accounts = new ArrayList<>();
		this.temporalTickerRetriever = new TemporalTickerRetriever();
		this.accountDAO = new AccountDAO();
	}
    
    public void start() {
    	retrieveAccounts();
    }

	private Map<Currency, TemporalTicker> retrieveTemporalTickers() {
		TimeInterval timeInterval = retrievePreviousMinuteInterval();
    	Map<Currency, TemporalTicker> temporalTickersByCurrency = new EnumMap<>(Currency.class);
    	
    	for (Currency currency : Currency.values()) {
    		TemporalTicker temporalTicker = temporalTickerRetriever.retrieve(currency, timeInterval);
    		temporalTickersByCurrency.put(currency, temporalTicker);
		}
    	
    	return temporalTickersByCurrency;
	}

	private TimeInterval retrievePreviousMinuteInterval() {
		ZonedDateTime now = ZonedDateTimeUtils.now();
    	ZonedDateTime endTime = now.minusSeconds(now.getSecond()).minusMinutes(1);
    	ZonedDateTime startTime = endTime.minusMinutes(1);
		return new TimeInterval(startTime, endTime);
	}

	private void retrieveAccounts() {
		List<AccountData> accountDatas = new AccountsXmlReader(XML_DIRECTORY_PATH).readAccounts();
		
    	for (AccountData accountDataFromXml : accountDatas) {
			AccountPO accountPO = searchAccountOnDatabase(accountDataFromXml.getOwner());
			AccountData accountData;
			if (accountPO != null) {
				accountData = new AccountPOToAccountDataConverter().convertTo(accountPO);
			} else {
				accountData = accountDataFromXml;
				saveAccountOnDatabase(accountDataFromXml);
			}
			Account account = new Account(accountData);
			accounts.add(account);
		}
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
