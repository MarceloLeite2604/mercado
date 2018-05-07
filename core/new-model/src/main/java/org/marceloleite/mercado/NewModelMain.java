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
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.utils.EncryptUtils;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.dao.database.repository.TickerRepository;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.dao.interfaces.PropertyDAO;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.dao.site.siteretriever.OrderbookSiteRetriever;
import org.marceloleite.mercado.dao.site.siteretriever.TickerSiteRetriever;
import org.marceloleite.mercado.dao.site.siteretriever.trade.TradeSiteRetriever;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Orderbook;
import org.marceloleite.mercado.model.Parameter;
import org.marceloleite.mercado.model.Property;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Ticker;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.model.Variable;
import org.marceloleite.mercado.model.Wallet;
import org.marceloleite.mercado.model.Withdrawal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class NewModelMain {

	private static final Logger LOGGER = LoggerFactory.getLogger(NewModelMain.class);

	private static final String OUTPUT_FOLDER = "output/";

	private static final String XML_FILE_PATH_TEMPLATE = OUTPUT_FOLDER + "%s.xml";

	private static final String JSON_FILE_PATH_TEMPLATE = OUTPUT_FOLDER + "%s.json";

	private static final String ACCOUNT_OWNER = "Marcelo Leite";

	private static final String PROPERTY_NAME = "org.marceloleite.property";

	private static final Currency CURRENCY = Currency.LITECOIN;

	private static final ZonedDateTime START_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 13, 00, 0, 0),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	private static final ZonedDateTime END_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 14, 00, 0, 0),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	private static final ZonedDateTime TICKER_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 13, 18, 9, 0),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	private static final Long TRADE_ID = 1L;

	private static final ZonedDateTime TRADE_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 14, 20, 19),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	private static final ZonedDateTime TRADE_FIND_START_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 14, 20, 19),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	private static final ZonedDateTime TRADE_FIND_END_TIME = ZonedDateTime.of(LocalDateTime.of(2018, 04, 15, 20, 19),
			ZonedDateTimeUtils.DEFAULT_ZONE_ID);

	@Inject
	@Named("AccountXMLDatabaseDAO")
	private AccountDAO accountDAO;

	@Inject
	@Named("PropertyDatabaseDAO")
	private PropertyDAO propertyDAO;

	@Inject
	@Named("TradeDatabaseSiteDAO")
	private TradeDAO tradeDAO;

	@Inject
	@Named("TemporalTickerDatabaseDAO")
	private TemporalTickerDAO temporalTickerDAO;

	@Inject
	private TickerRepository tickerRepository;

	public static void main(String[] args) {
		SpringApplication.run(NewModelMain.class);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			encrypt();

			// AccountXMLDAO.setXMLDirectory("output/");
			//
			// Account account = createAccount();
			// persistAccount(account);
			// writeJson(account, "account");
			//
			// Property property = createProperty();
			// persistProperty(property);
			// writeXML(property, "property");
			// writeJson(property, "property");
			//
			// TemporalTicker temporalTicker = createTemporalTicker();
			// persistTemporalTicker(temporalTicker);
			// writeXML(temporalTicker, "temporalTicker");
			// writeJson(temporalTicker, "temporalTicker");
			//
			// Ticker ticker = createTicker();
			// persistTicker(ticker);
			// writeXML(ticker, "ticker");
			// writeJson(ticker, "ticker");

			// List<Trade> trades = tradeDAO.findByCurrencyAndTimeBetween(CURRENCY,
			// TRADE_FIND_START_TIME,
			// TRADE_FIND_END_TIME);
			// LOGGER.info("Total trades found: " + trades.size());

			// double nan = Double.NaN;
			// double posInfinity = Double.POSITIVE_INFINITY;
			// double negInfinity = Double.NEGATIVE_INFINITY;

			// System.out.println("NaN: " + Double.isFinite(nan));
			// System.out.println("PosInfinity: " + Double.isFinite(posInfinity));
			// System.out.println("NegInfinity: " + Double.isFinite(negInfinity));
		};
	}

	private void encrypt() {
		System.out.println(EncryptUtils.encrypt("novoMercado"));
	}

	private Account createAccount() {

		Account account = accountDAO.findByOwner(ACCOUNT_OWNER);
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

			Order order = Order.builder()
					.setCreated(ZonedDateTimeUtils.now())
					.setQuantity(new BigDecimal("0.23"))
					.setExecutedPriceAverage(new BigDecimal("32000"))
					.setExecutedQuantity(new BigDecimal("0.035734"))
					.setFee(new BigDecimal("14"))
					.setCurrencyPair(CurrencyPair.retrieveByPair(Currency.REAL, Currency.LITECOIN))
					.setHasFills(true)
					.setIntended(ZonedDateTimeUtils.now()
							.minusMinutes(10))
					.setLimitPrice(new BigDecimal("32200"))
					.setStatus(OrderStatus.FILLED)
					.setType(OrderType.SELL)
					.setUpdated(ZonedDateTimeUtils.now()
							.minusMinutes(5))
					.build();

			account = new Account();
			account.setOwner(ACCOUNT_OWNER);
			account.setEmail("marceloleite2604@gmail.com");
			account.setStrategies(new ArrayList<>());
			account.addStrategy(strategy);

			account.setWallet(new Wallet());
			account.addBalance(balance);

			account.setWithdrawals(new ArrayList<>());
			account.addWithdrawal(withdrawal);

			account.setOrders(new ArrayList<>());
			account.addOrder(order);
		}

		return account;
	}

	private Property createProperty() {
		Property property = propertyDAO.findByName(PROPERTY_NAME);

		if (property == null) {
			property = new Property();
			property.setName(PROPERTY_NAME);
			property.setValue("propertyValue");
		}
		return property;
	}

	private TemporalTicker createTemporalTicker() {
		TemporalTicker temporalTicker = null;
		temporalTicker = temporalTickerDAO.findByCurrencyAndStartAndEnd(CURRENCY, START_TIME, END_TIME);
		if (temporalTicker == null) {
			/* temporalTicker = */TemporalTicker.builder()
					.currency(CURRENCY)
					.start(START_TIME)
					.end(END_TIME)
					.duration(Duration.ofSeconds(60))
					.first(new BigDecimal("12"))
					.last(new BigDecimal("67"))
					.previousLast(new BigDecimal("125"))
					.highest(new BigDecimal("23"))
					.lowest(new BigDecimal("62"))
					.average(new BigDecimal("10"))
					.buy(new BigDecimal("2.34"))
					.previousBuy(new BigDecimal("2325"))
					.sell(new BigDecimal("36"))
					.previousSell(new BigDecimal("57"))
					.orders(20L)
					.buyOrders(8L)
					.sellOrders(2L)
					.volumeTraded(new BigDecimal("204"))
					.build();
		}
		return temporalTicker;
	}

	private Ticker createTicker() {
		Ticker ticker = tickerRepository.findByCurrencyAndTickerTime(CURRENCY, TICKER_TIME);
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

	private Trade createTrade() {
		Trade trade = null;
		List<Trade> trades = tradeDAO.findByCurrencyAndTimeBetween(CURRENCY, TRADE_TIME, TRADE_FIND_END_TIME);
		if (trades != null && !trades.isEmpty()) {
			LOGGER.info("Total trades retrieved: {}", trades.size());
			trade = trades.get(0);
		}

		if (trade == null) {
			trade = new Trade();
			trade.setId(TRADE_ID);
			trade.setAmount(new BigDecimal("1.496"));
			trade.setCurrency(CURRENCY);
			trade.setPrice(new BigDecimal("503.76"));
			trade.setTime(TRADE_TIME);
			trade.setType(TradeType.BUY);
		}
		return trade;
	}

	private void writeJson(Object object, String fileName) {
		LOGGER.info("Persisting Json.");
		try (OutputStream outputStream = new FileOutputStream(createJsonFileOutput(fileName))) {
			outputStream.write(ObjectToJsonConverter.writeAsJson(object)
					.getBytes());
		} catch (IOException exception) {
			throw new RuntimeException("Error while writing Json file.", exception);
		}

	}

	private void writeXML(Object object, String fileName) {
		LOGGER.info("Writing XML.");
		File outputFile = createXmlFileOutput(fileName);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Account.class, Property.class, TemporalTicker.class, Ticker.class,
					Trade.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(object, outputFile);
		} catch (JAXBException exception) {
			throw new RuntimeException("Error while writing XML file.", exception);
		}

	}

	private File createXmlFileOutput(String fileName) {
		return new File(String.format(XML_FILE_PATH_TEMPLATE, fileName));
	}

	private File createJsonFileOutput(String fileName) {
		return new File(String.format(JSON_FILE_PATH_TEMPLATE, fileName));
	}

	private void persistAccount(Account account) {
		LOGGER.info("Persisting account.");
		accountDAO.save(account);
	}

	private void persistProperty(Property property) {
		LOGGER.info("Persisting property.");
		propertyDAO.save(property);
	}

	private void persistTemporalTicker(TemporalTicker temporalTicker) {
		LOGGER.info("Persisting temporal ticker.");
		temporalTickerDAO.save(temporalTicker);
	}

	private void persistTicker(Ticker ticker) {
		LOGGER.info("Persisting ticker.");
		tickerRepository.save(ticker);
	}

	private void persistTrade(Trade trade) {
		LOGGER.info("Persisting trade.");
		try {
			tradeDAO.save(trade);
		} catch (UnsupportedOperationException exception) {
			LOGGER.warn("\"save\" method is unsupported by this DAO.");
		}
	}

	@SuppressWarnings("unused")
	private static void orderbookSiteRetriever() {
		Orderbook orderbook = new OrderbookSiteRetriever().retrieve(Currency.BITCOIN);
		System.out.println(ObjectToJsonConverter.writeAsJson(orderbook));
	}

	@SuppressWarnings("unused")
	private static void tickerSiteRetriever() {
		Ticker ticker = new TickerSiteRetriever().retrieve(Currency.BITCOIN);
		System.out.println(ObjectToJsonConverter.writeAsJson(ticker));
	}

	@SuppressWarnings("unused")
	public void testTradesSiteRetriever() {
		ZonedDateTime to = ZonedDateTimeUtils.now();
		ZonedDateTime from = ZonedDateTime.from(to)
				.minusDays(2);
		TimeInterval timeInterval = new TimeInterval(from, to);
		List<Trade> trades = new TradeSiteRetriever().retrieve(Currency.BITCOIN, timeInterval);
		System.out.println(trades.size());
		// System.out.println(ObjectToJsonConverter.writeAsJson(trades));
	}
}
