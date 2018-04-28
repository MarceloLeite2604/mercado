package org.marceloleite.mercado.dao.database;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.dao.database.repository.AccountRepository;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.model.Account;
import org.springframework.stereotype.Repository;

@Repository
@Named("AccountDatabaseDAO")
public class AccountDatabaseDAO implements AccountDAO {

	@Inject
	private AccountRepository accountRepository;

	@Override
	public Account findByOwner(String owner) {
		return accountRepository.findByOwner(owner);
	}

	@Override
	public Iterable<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public <S extends Account> S save(S account) {
		return accountRepository.save(account);
	}

	@Override
	public <S extends Account> Iterable<S> saveAll(Iterable<S> accounts) {
		return accountRepository.saveAll(accounts);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Account> Optional<S> findById(Long id) {
		return (Optional<S>) accountRepository.findById(id);
	}
}
