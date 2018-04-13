package org.marceloleite.mercado;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Ticker;
import org.marceloleite.mercado.model.Variable;
import org.marceloleite.mercado.model.Withdrawal;
import org.marceloleite.mercado.repository.AccountRepository;
import org.marceloleite.mercado.repository.PropertyRepository;
import org.marceloleite.mercado.repository.TemporalTickerRepository;
import org.marceloleite.mercado.repository.TickerRepository;
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

	@Autowired
	private TemporalTickerRepository temporalTickerRepository;

	@Autowired
	private TickerRepository tickerRepository;

	private static final String ACCOUNT_OWNER = "Marcelo Leite";

	private static final String PROPERTY_NAME = "org.marceloleite.property";

	private static final Currency CURRENCY = Currency.LITECOIN;

	private static final ZonedDateTime START_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 13, 18, 8, 0),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	private static final ZonedDateTime END_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 13, 18, 9, 0),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	private static final ZonedDateTime TICKER_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 13, 18, 9, 0),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {

			Account account = createAccount();
			persistAccount(account);
			writeXML(account, "account");
			writeJson(account, "account");

			Property property = createProperty();
			persistProperty(property);
			writeXML(property, "property");
			writeJson(property, "property");

			TemporalTicker temporalTicker = createTemporalTicker();
			persistTemporalTicker(temporalTicker);
			writeXML(temporalTicker, "temporalTicker");
			writeJson(temporalTicker, "temporalTicker");

			Ticker ticker = createTicker();
			persistTicker(ticker);
			writeXML(ticker, "ticker");
			writeJson(ticker, "ticker");
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

	private TemporalTicker createTemporalTicker() {
		TemporalTicker temporalTicker = null;
		temporalTicker = temporalTickerRepository.findByCurrencyAndStartTimeAndEndTime(CURRENCY, START_TIME, END_TIME);
		if (temporalTicker == null) {
			temporalTicker = new TemporalTicker();
			temporalTicker.setCurrency(CURRENCY);
			temporalTicker.setStartTime(START_TIME);
			temporalTicker.setEndTime(END_TIME);
			temporalTicker.setAveragePrice(new BigDecimal("10"));
			temporalTicker.setBuy(new BigDecimal("2.34"));
			temporalTicker.setBuyOrders(8L);
			temporalTicker.setFirstPrice(new BigDecimal("12"));
			temporalTicker.setHighestPrice(new BigDecimal("23"));
			temporalTicker.setLastPrice(new BigDecimal("67"));
			temporalTicker.setLowestPrice(new BigDecimal("62"));
			temporalTicker.setOrders(20L);
			temporalTicker.setPreviousBuy(new BigDecimal("2325"));
			temporalTicker.setPreviousLastPrice(new BigDecimal("125"));
			temporalTicker.setPreviousSell(new BigDecimal("57"));
			temporalTicker.setSell(new BigDecimal("36"));
			temporalTicker.setSellOrders(2L);
			temporalTicker.setTimeDuration(Duration.ofSeconds(60));
			temporalTicker.setVolumeTraded(new BigDecimal("204"));
		}
		return temporalTicker;
	}

	private Ticker createTicker() {
		Ticker ticker = null;

		ticker = tickerRepository.findByCurrencyAndTickerTime(CURRENCY, TICKER_TIME);
		if (ticker == null) {
			ticker = new Ticker();
			ticker.setCurrency(CURRENCY);
			ticker.setTickerTime(TICKER_TIME);
			ticker.setBuy(new BigDecimal("10.3"));
			ticker.setHigh(new BigDecimal("4938"));
			ticker.setLast(new BigDecimal("25.15"));
			ticker.setLow(new BigDecimal("562.72"));
			ticker.setSell(new BigDecimal("23.883"));
			ticker.setVolume(new BigDecimal("26378.4"));
		}
		return ticker;
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
			jaxbContext = JAXBContext.newInstance(Account.class, Property.class, TemporalTicker.class, Ticker.class);
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

	private void persistTemporalTicker(TemporalTicker temporalTicker) {
		log.info("Persisting temporal ticker.");
		temporalTickerRepository.save(temporalTicker);
	}

	private void persistTicker(Ticker ticker) {
		log.info("Persisting ticker.");
		tickerRepository.save(ticker);
	}

}
