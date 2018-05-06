package org.marceloleite.mercado.simulator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountsRetriever {
	
	@Inject
	@Named("AccountXMLDatabaseDAO")
	private AccountDAO accountDAO;

	public List<Account> retrieve() {
		List<Account> accounts = new ArrayList<>();
		Iterable<Account> retrievedAccounts = accountDAO.findAll();
		retrievedAccounts.forEach(accounts::add);
		return accounts;
	}
}
