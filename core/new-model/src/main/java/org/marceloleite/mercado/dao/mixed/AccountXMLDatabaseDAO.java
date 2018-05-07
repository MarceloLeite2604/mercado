package org.marceloleite.mercado.dao.mixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.model.Account;
import org.springframework.stereotype.Repository;

@Repository
@Named("AccountXMLDatabaseDAO")
public class AccountXMLDatabaseDAO implements AccountDAO {
	
	@Inject
	@Named("AccountXMLDAO")
	private AccountDAO accountXMLDAO;
	
	@Inject
	@Named("AccountDatabaseDAO")
	private AccountDAO accountDatabaseDAO;

	@Override
	public <S extends Account> Iterable<S> saveAll(Iterable<S> accounts) {
		return accountDatabaseDAO.saveAll(accounts);
	}

	@Override
	public Account findByOwner(String owner) {
		Account account = accountDatabaseDAO.findByOwner(owner);
		if (account == null ) {
			account = accountXMLDAO.findByOwner(owner);
		}
		return account;
	}
	
	@Override
	public Iterable<Account> findAll() {
		Iterable<Account> accountsFromXML = accountXMLDAO.findAll();
		List<Account> accounts = new ArrayList<>();
		for (Account accountFromXML : accountsFromXML) {
			Account accountFromDatabase = accountDatabaseDAO.findByOwner(accountFromXML.getOwner());
			accounts.add(Optional.ofNullable(accountFromDatabase).orElse(accountFromXML));
		}
		return accounts;
	}
	

	@Override
	public <S extends Account> S save(S account) {
		return accountDatabaseDAO.save(account);
	}

	@Override
	public <S extends Account> Optional<S> findById(Long id) {
		return accountDatabaseDAO.findById(id);
	}
}
