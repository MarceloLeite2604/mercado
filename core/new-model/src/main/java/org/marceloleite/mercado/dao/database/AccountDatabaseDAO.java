package org.marceloleite.mercado.dao.database;

import javax.inject.Inject;

import org.marceloleite.mercado.dao.database.repository.AccountRepository;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.model.Account;

public class AccountDatabaseDAO implements AccountDAO {

	@Inject
	private AccountRepository accountRepository;

	@Override
	public Account findByOwner(String owner) {
		return accountRepository.findByOwner(owner);
	}

	@Override
	public <S extends Account> S save(S account) {
		return accountRepository.save(account);
	}
}
