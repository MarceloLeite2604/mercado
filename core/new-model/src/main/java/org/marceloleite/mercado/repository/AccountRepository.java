package org.marceloleite.mercado.repository;

import org.marceloleite.mercado.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

	public Account findByOwner(String owner);
}
