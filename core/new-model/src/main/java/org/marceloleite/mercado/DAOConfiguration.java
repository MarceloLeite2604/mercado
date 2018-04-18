package org.marceloleite.mercado;

import javax.inject.Inject;

import org.marceloleite.mercado.dao.database.AccountDatabaseDAO;
import org.marceloleite.mercado.dao.database.PropertyDatabaseDAO;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.dao.xml.AccountXMLDAO;
import org.marceloleite.mercado.dao.xml.PropertyXMLDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DAOConfiguration {

	@Inject
	private AccountXMLDAO accountXMLDAO;

	@Inject
	private AccountDatabaseDAO accountDatabaseDAO;

	@Inject
	private PropertyDatabaseDAO propertyDatabaseDAO;

	@Inject
	private PropertyXMLDAO propertyXMLDAO;

	@Bean("AccountXMLDAO")
	public AccountDAO createAccountXMLDAO() {
		return accountXMLDAO;
	}

	@Bean("AccountDatabaseDAO")
	public AccountDAO createAccountDatabaseDAO() {
		return accountDatabaseDAO;
	}

	@Bean("PropertyDatabaseDAO")
	public PropertyDAO createPropertyDatabaseDAO() {
		return propertyDatabaseDAO;
	}

	@Bean("PropertyXMLDAO")
	public PropertyDAO createPropertyXMLDAO() {
		return propertyXMLDAO;
	}

}
