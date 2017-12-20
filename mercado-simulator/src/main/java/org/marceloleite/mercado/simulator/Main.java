package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.additional.PriceRetriever;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.util.converter.StringToLocalDateTimeConverter;
import org.marceloleite.mercado.commons.util.converter.StringToLocalDateTimeConverterTest;

public class Main {

	public static void main(String[] args) {
		timeDivisionController();
	}

	private static void timeDivisionController() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minus(Duration.ofDays(10));
		Duration divisionDuration = Duration.ofHours(10);
		TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, divisionDuration);
		for (int counter = 0; counter < timeDivisionController.getDivisions(); counter++) {
			TimeInterval nextTimeInterval = timeDivisionController.getNextTimeInterval();
			System.out.println(new ObjectToJsonConverter().convert(nextTimeInterval));
		}

	}

	private static void simulator() {
		StringToLocalDateTimeConverter stringToLocalDateTimeFormatter = new StringToLocalDateTimeConverter();

		Account account = new Account("Marcelo");

		Simulator simulator = configureSimulator();

		Deposit deposit = new Deposit();
		LocalDateTime time = stringToLocalDateTimeFormatter.convert("05/09/2017 21:31:00");
		deposit.setTime(time);
		deposit.setCurrencyAmount(new CurrencyAmount(Currency.REAL, 550.37));
		account.addDeposit(deposit, time);

		deposit = new Deposit();
		time = stringToLocalDateTimeFormatter.convert("29/11/2017 10:34:00");
		deposit.setTime(time);
		deposit.setCurrencyAmount(new CurrencyAmount(Currency.REAL, 1030.0));
		account.addDeposit(deposit, time);

		BuyOrder buyOrder = new BuyOrder();
		buyOrder.setCurrencyToSpend(Currency.REAL);
		buyOrder.setSpendAmount(250.0);
		buyOrder.setCurrencyToBuy(Currency.BITCOIN);
		buyOrder.setTime(stringToLocalDateTimeFormatter.convert("07/09/2017 21:47:15"));
		account.addBuyOrder(buyOrder, time);

		buyOrder = new BuyOrder();
		buyOrder.setCurrencyToSpend(Currency.REAL);
		buyOrder.setSpendAmount(250.0);
		buyOrder.setCurrencyToBuy(Currency.LITECOIN);
		buyOrder.setTime(stringToLocalDateTimeFormatter.convert("07/09/2017 21:47:15"));
		account.addBuyOrder(buyOrder, time);

		buyOrder = new BuyOrder();
		buyOrder.setCurrencyToSpend(Currency.REAL);
		buyOrder.setSpendAmount(750.0);
		buyOrder.setCurrencyToBuy(Currency.BITCOIN);
		buyOrder.setTime(stringToLocalDateTimeFormatter.convert("29/11/2017 13:32:05"));
		account.addBuyOrder(buyOrder, time);

		buyOrder = new BuyOrder();
		buyOrder.setCurrencyToSpend(Currency.REAL);
		buyOrder.setSpendAmount(250.0);
		buyOrder.setCurrencyToBuy(Currency.LITECOIN);
		buyOrder.setTime(stringToLocalDateTimeFormatter.convert("29/11/2017 13:32:05"));
		account.addBuyOrder(buyOrder, time);

		CurrencyMonitoring currencyMonitoring = new CurrencyMonitoring();
		currencyMonitoring.setCurrency(Currency.BITCOIN);
		currencyMonitoring.setIncreasePercentage(0.05);
		currencyMonitoring.setDecreasePercentage(0.03);
		account.addCurrencyMonitoring(currencyMonitoring);

		simulator.runSimulation();

		/*
		 * System.out.println(new
		 * ObjectToJsonFormatter().format(simulator.getBalance()));
		 * System.out.println(new
		 * ObjectToJsonFormatter().format(simulator.getComission()));
		 */

		/*
		 * Map<Currency, CurrencyAmount> balances = simulator.getBalance()
		 * .getBalances();
		 */

		/*
		 * System.out.println("Balance: "); for (Currency currency :
		 * Currency.values()) { CurrencyAmount currencyAmount =
		 * balances.get(currency); if (currencyAmount != null) {
		 * calculateRealValueOf(currencyAmount); } }
		 */

		/*
		 * System.out.println("Comission: "); Map<Currency, CurrencyAmount>
		 * comission = simulator.getComission() .getBalances(); for (Currency
		 * currency : Currency.values()) { CurrencyAmount currencyAmount =
		 * comission.get(currency); if (currencyAmount != null) {
		 * calculateRealValueOf(currencyAmount); } }
		 */
	}

	private static Simulator configureSimulator() {
		StringToLocalDateTimeConverter stringToLocalDateTimeFormatter = new StringToLocalDateTimeConverter();
		Simulator simulator = new Simulator();
		LocalDateTime startTime = stringToLocalDateTimeFormatter.convert("01/09/2017 00:00:00");
		LocalDateTime stopTime = LocalDateTime.now();
		/*
		 * LocalDateTime stopTime =
		 * stringToLocalDateTimeFormatter.format("02/09/2017 00:00:00");
		 */
		Duration stepDuration = Duration.ofMinutes(10);
		simulator.setStartTime(startTime);
		simulator.setStopTime(stopTime);
		simulator.setStepTime(stepDuration);
		return simulator;
	}

	private static void calculateRealValueOf(CurrencyAmount currencyAmount) {
		double currentPrice;
		if (currencyAmount.getCurrency() != Currency.REAL) {
			currentPrice = new PriceRetriever().retrieve(currencyAmount.getCurrency(), LocalDateTime.now());
		} else {
			currentPrice = 1;
		}

		CurrencyAmount realCurrencyAmount = new CurrencyAmount(Currency.REAL,
				currencyAmount.getAmount() * currentPrice);
		System.out.println(realCurrencyAmount);
	}

}
