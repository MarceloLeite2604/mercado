package org.marceloleite.mercado;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Parameter;
import org.marceloleite.mercado.model.Property;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.Variable;
import org.marceloleite.mercado.model.Withdrawal;
import org.marceloleite.mercado.repository.AccountRepository;
import org.marceloleite.mercado.repository.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PropertyRepository propertyRepository;

	private static final String ACCOUNT_OWNER = "Marcelo Leite";
	
	private static final String PROPERTY_NAME = "org.marceloleite.property"; 

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			Account account = createAccount();
			Property property = createProperty();

			persistAccount(account);

			persistProperty(property);

			writeXML(account, "account");
			
			writeXML(property, "property");

			writeJson(account, "account");
			
			writeJson(property, "property");
		};
	}

	private Account createAccount() {

		Account account = null;

		account = accountRepository.findByOwner(ACCOUNT_OWNER);

		if (account == null) {
			Parameter parameter = new Parameter();
			parameter.setName("destroyAll");
			parameter.setValue("true");

			Variable variable = new Variable();
			variable.setName("tryHarder");
			variable.setValue("whyNot");

			Strategy strategy = new Strategy();
			strategy.setClassName("org.marceloleite.mercado.test.Main");
			strategy.setParameters(Arrays.asList(parameter));

			Balance balance = new Balance();
			balance.setCurrency(Currency.REAL);
			balance.setAmount(new BigDecimal("1000"));

			Withdrawal withdrawal = new Withdrawal();
			withdrawal.setCurrency(Currency.REAL);
			withdrawal.setAmount(new BigDecimal("1000"));

			Order order = new Order();
			order.setCreated(ZonedDateTimeUtils.now());
			order.setQuantity(new BigDecimal("0.23"));
			order.setExecutedPriceAverage(new BigDecimal("32000"));
			order.setExecutedQuantity(new BigDecimal("0.035734"));
			order.setFee(new BigDecimal("14"));
			order.setFirstCurrency(Currency.REAL);
			order.setSecondCurrency(Currency.LITECOIN);
			order.setHasFills(true);
			order.setIntended(ZonedDateTimeUtils.now().minusMinutes(10));
			order.setLimitPrice(new BigDecimal("32200"));
			order.setStatus(OrderStatus.FILLED);
			order.setType(OrderType.SELL);
			order.setUpdated(ZonedDateTimeUtils.now().minusMinutes(5));

			account = new Account();
			account.setOwner(ACCOUNT_OWNER);
			account.setEmail("marceloleite2604@gmail.com");
			account.setStrategies(new ArrayList<>());
			account.addStrategy(strategy);

			account.setBalances(new ArrayList<>());
			account.addBalance(balance);

			account.setWithdrawals(new ArrayList<>());
			account.addWithdrawal(withdrawal);

			account.setOrders(new ArrayList<>());
			account.addOrder(order);
		}

		return account;
	}

	private Property createProperty() {
		Property property = null;
				
		property = propertyRepository.findByName(PROPERTY_NAME);

		if (property == null) {
			property = new Property();
			property.setName(PROPERTY_NAME);
			property.setValue("propertyValue");
		}
		return property;
	}

	private void writeJson(Object object, String fileName) {
		log.info("Persisting Json.");
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		try (OutputStream outputStream = new FileOutputStream("src/main/resources/" + fileName + ".json")) {
			outputStream.write(objectToJsonConverter.convertTo(object).getBytes());
		} catch (IOException exception) {
			throw new RuntimeException("Error while writing Json file.", exception);
		}

	}

	private void writeXML(Object object, String fileName) {
		log.info("Writing XML.");
		File outputFile = new File("src/main/resources/" + fileName + ".xml");
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Account.class, Property.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(object, outputFile);
		} catch (JAXBException exception) {
			throw new RuntimeException("Error while writing XML file.", exception);
		}

	}

	private void persistAccount(Account account) {
		log.info("Persisting account.");

		accountRepository.save(account);
	}

	private void persistProperty(Property property) {
		log.info("Persisting property.");

		propertyRepository.save(property);
	}

}
