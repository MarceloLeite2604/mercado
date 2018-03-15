package org.marceloleite.mercado.configurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.Reader;
import org.marceloleite.mercado.commons.encryption.Encrypt;
import org.marceloleite.mercado.commons.properties.SystemProperty;
import org.marceloleite.mercado.databaseretriever.persistence.daos.PropertyDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class Configurator {

	private long nonceValue;

	private String encryptKey;

	private String emailAddress;

	private String emailPassword;

	public Configurator() {

	}

	public void configureSystem() {
		nonceValue = retrieveNonceValue();
		encryptKey = retrieveEncryptKey();
		emailAddress = retrieveEmailAddress();
		emailPassword = retrieveEmailPassword();

		persistProperties();
		informEncryptKey();
		System.out.println("System configuration complete.");
	}

	private String retrieveEncryptKey() {
		String encryptKeyFromEnvironmentVariable = retrieveEncryptKeyFromEnviromentVariable();
		String encryptKey;
		if (encryptKeyFromEnvironmentVariable == null || encryptKeyFromEnvironmentVariable.isEmpty()) {
			System.out.println("Encrypt key not found on system. Creating one.");
			encryptKey = createEncryptKey();
		} else {
			encryptKey = encryptKeyFromEnvironmentVariable;
		}
		return encryptKey;
	}

	private long retrieveNonceValue() {
		try {
			return new Reader().read("Please inform the last Nonce value used: ", new NonceParser(),
					new NonceChecker());
		} catch (IOException exception) {
			throw new RuntimeException("Error while retrieving nonce value.", exception);
		}
	}

	private String retrieveEncryptKeyFromEnviromentVariable() {
		return System.getenv(Encrypt.KEY_ENVIRONMENT_VARIABLE_NAME);
	}

	private String createEncryptKey() {
		return new Encrypt().generateKey();
	}

	private String retrieveEmailAddress() {
		try {
			return new Reader().read("Inform the system e-mail address: ", new EmailChecker());
		} catch (IOException exception) {
			throw new RuntimeException("Error while retrieving e-mail address.", exception);
		}
	}

	private String retrieveEmailPassword() {
		boolean passwordConfirmed = false;
		String emailPassword = null;
		try {
			Reader reader = new Reader();
			reader.hideInput(true);

			while (!passwordConfirmed) {
				emailPassword = reader.read("Inform the system e-mail password: ");
				String confirmPassword = reader.read("Confirm the system e-mail password: ");
				if (!emailPassword.equals(confirmPassword)) {
					System.out.println("Password confirmation does not match.");
				} else {
					passwordConfirmed = true;
				}
			}

			return emailPassword;
		} catch (IOException exception) {
			throw new RuntimeException("Error while retrieving e-mail password.", exception);
		}
	}

	private void persistProperties() {
		System.out.println("Persisting informations on database.");

		PropertyDAO propertyDAO = new PropertyDAO();

		List<PropertyPO> propertyPOs = new ArrayList<>();
		propertyPOs.add(createPropertyPO(SystemProperty.EMAIL_USERNAME, emailAddress));
		propertyPOs.add(createPropertyPO(SystemProperty.EMAIL_PASSWORD, emailPassword));
		propertyPOs.add(createPropertyPO(SystemProperty.NONCE, Long.toString(nonceValue)));
		propertyDAO.merge(propertyPOs);
	}

	private PropertyPO createPropertyPO(SystemProperty systemProperty, String value) {
		PropertyPO propertyPO = new PropertyPO();

		propertyPO.setName(systemProperty.getName());
		propertyPO.setValue(value);

		return propertyPO;
	}

	private void informEncryptKey() {
		System.out.println("Please store this environment variable on your system: ");
		System.out.println(Encrypt.KEY_ENVIRONMENT_VARIABLE_NAME + "=" + encryptKey);
	}
}
