package org.marceloleite.mercado.dao.database;

import org.marceloleite.mercado.dao.database.repository.AccountRepository;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.model.Account;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountDatabaseDAO implements AccountDAO {

	@Autowired
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
