package org.marceloleite.mercado;

import org.marceloleite.mercado.database.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DAOConfiguration {

	@Autowired
	private AccountRepository accountRepository;

	@Bean
	public AccountDAO accountDAO() {
		return accountRepository;
	}
}
