package org.marceloleite.mercado;

import org.marceloleite.mercado.model.Account;

public interface AccountDAO {

	public Account findByOwner(String owner);

	public <S extends Account> S save(Account account);

}
