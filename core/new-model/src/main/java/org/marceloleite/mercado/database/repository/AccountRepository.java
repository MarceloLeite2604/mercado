package org.marceloleite.mercado.database.repository;

import org.marceloleite.mercado.AccountDAO;
import org.marceloleite.mercado.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long>, AccountDAO {

	public Account findByOwner(String owner);
	
	public <S extends Account> S save(Account account);
}
