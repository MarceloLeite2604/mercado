package org.marceloleite.mercado.controller;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.api.negotiation.method.GetAccountInfo;
import org.marceloleite.mercado.api.negotiation.method.TapiResponse;
import org.marceloleite.mercado.api.negotiation.model.AccountInfo;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.controller.converter.AccountInfoToBalanceWalletConverter;
import org.marceloleite.mercado.controller.utils.TimeIntervalUtils;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.email.EmailMessage;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class Controller {

	private static final Logger LOGGER = LogManager.getLogger(Controller.class);

	@Inject
	@Named("AccountXMLDatabaseDAO")
	private AccountDAO accountDAO;

	@Inject
	@Named("ControllerHouse")
	private House house;

	@Inject
	private TemporalTickerRetriever temporalTickerRetriever;

	public void start() {
		sendStartEmails();
		while (!finished()) {
			waitNextMinute();
			LOGGER.debug("Checking.");
			check();
		}
	}

	private void sendStartEmails() {
		house.getAccounts()
				.forEach(this::sendEmailFor);
	}

	private void sendEmailFor(Account account) {
		EmailMessage emailMessage = new EmailMessage();
		String email = account.getEmail();
		if (email != null) {
			emailMessage.getToAddresses()
					.add(email);
			emailMessage.setSubject("Started");
			emailMessage.setContent("Mercado Controller has started.");
			emailMessage.send();
		}
	}

	private void check() {
		house.process(retrieveTemporalTickers());
		retrieveBalances();
		save();
	}

	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> retrieveTemporalTickers() {
		TimeInterval timeInterval = TimeIntervalUtils.getInstance()
				.retrievePreviousMinuteInterval();
		Map<Currency, TemporalTicker> temporalickers = temporalTickerRetriever.retrieveFor(timeInterval);
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval = new TreeMap<>();
		temporalTickersByTimeInterval.put(timeInterval, temporalickers);
		return temporalTickersByTimeInterval;
	}

	private void save() {
		accountDAO.saveAll(house.getAccounts());
	}

	private void waitNextMinute() {
		ZonedDateTime now = ZonedDateTimeUtils.now();
		ZonedDateTime nextMinute = now.plusMinutes(1)
				.minusSeconds(now.getSecond());
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

	private void retrieveBalances() {
		for (Account account : house.getAccounts()) {
			account.setWallet(retrieveBalanceFor(account));
		}
	}

	private Wallet retrieveBalanceFor(Account account) {
		TapiResponse<AccountInfo> tapiResponse = new GetAccountInfo(account.getTapiInformation()).execute();
		if (tapiResponse.getStatusCode() != 100) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"Balance retrieval for account \"" + account.getOwner() + "\" returned the error message: ");
			stringBuilder.append(tapiResponse.getStatusCode() + " - " + tapiResponse.getErrorMessage());
			throw new RuntimeException(stringBuilder.toString());
		}

		return AccountInfoToBalanceWalletConverter.fromAccountInfoToWallet(tapiResponse.getResponseData());
	}
}
