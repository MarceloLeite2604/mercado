package org.marceloleite.mercado.siteretriever;

import java.time.ZonedDateTime;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.model.Orderbook;
import org.marceloleite.mercado.model.Ticker;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	@Inject
	private TradesSiteRetriever tradesSiteRetriever;
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			tickerSiteRetriever();
			// orderbookSiteRetriever();
			// testTradesSiteRetriever();
		};
	}

	@SuppressWarnings("unused")
	private static void orderbookSiteRetriever() {
		Orderbook orderbook = new OrderbookSiteRetriever().retrieve(Currency.BITCOIN);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convertTo(orderbook));
	}

	@SuppressWarnings("unused")
	private static void tickerSiteRetriever() {
		Ticker ticker = new TickerSiteRetriever().retrieve(Currency.BITCOIN);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convertTo(ticker));
	}

	@SuppressWarnings("unused")
	public void testTradesSiteRetriever() {
		ZonedDateTime to = ZonedDateTimeUtils.now();
		ZonedDateTime from = ZonedDateTime.from(to).minusDays(2);
		TimeInterval timeInterval = new TimeInterval(from, to);
		Map<Long, Trade> trades = tradesSiteRetriever.retrieve(Currency.BITCOIN, timeInterval);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(trades.size());
		// System.out.println(objectToJsonConverter.convertTo(trades));
	}
}
