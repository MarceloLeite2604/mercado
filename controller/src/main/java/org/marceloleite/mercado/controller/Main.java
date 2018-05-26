package org.marceloleite.mercado.controller;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.marceloleite.mercado.NewModelMain;
import org.marceloleite.mercado.PersistenceContextConfiguration;
import org.marceloleite.mercado.dao.xml.XMLDAOConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "org.marceloleite.mercado", excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = PersistenceContextConfiguration.class),
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = NewModelMain.class),
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = XMLDAOConfiguration.class)})
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@DependsOn("MercadoApplicationContextAware")
public class Main {
	
	@Inject
	private Controller controller;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			controller();
			// testTradeSiteRetriever();
		};
	}
	
	@SuppressWarnings("unused")
	private void controller() {
		controller.start();
	}

//	@SuppressWarnings("unused")
//	private static void mailOrderExecutor() {
//		Account account = new Account();
//		account.setOwner("Marcelo Leite");
//		account.setEmail("marceloleite2604@gmail.com");
//		Order buyOrder = Order.builder()
//				.setCreated(ZonedDateTimeUtils.now())
//				.setCurrencyPair(CurrencyPair.retrieveByPair(Currency.REAL, Currency.BITCOIN))
//				.setQuantity(new BigDecimal("0.0567"))
//				.setLimitPrice(new BigDecimal("700.00"))
//				.build();
//		MailOrderExecutor.getInstance()
//				.placeOrder(buyOrder, null, account);
//
//	}	
}
