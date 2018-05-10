package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.StrategyExecutorClassLoader;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountsRetriever {

	private static final Logger LOGGER = LogManager.getLogger(AccountsRetriever.class);

	@Inject
	@Named("AccountXMLDatabaseDAO")
	private AccountDAO accountDAO;

	@Inject
	private StrategyExecutorClassLoader strategyExecutorClassLoader;

	public List<Account> retrieve() {
		List<Account> accounts = new ArrayList<>();
		Iterable<Account> retrievedAccounts = accountDAO.findAll();
		retrievedAccounts.forEach(accounts::add);
		createStrategiesExecutors(accounts);
		LOGGER.debug("{} account(s) found.", accounts.size());
		return accounts;
	}

	private void createStrategiesExecutors(List<Account> accounts) {
		for (Account account : accounts) {
			account.getStrategies()
					.forEach(strategy -> strategy.setExecutor(strategyExecutorClassLoader.load(strategy)));
		}
	}
}
