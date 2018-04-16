package org.marceloleite.mercado.dao.database.repository;

import org.marceloleite.mercado.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

	public Account findByOwner(String owner);
}
