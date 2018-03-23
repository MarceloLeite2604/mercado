package org.marceloleite.mercado.simulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.OrderStatus;

public class SimulationOrderExecutor implements OrderExecutor {

	private static final Logger LOGGER = LogManager.getLogger(SimulationOrderExecutor.class);

	@Override
	public Order placeOrder(Order order, House house, Account account) {
		switch (order.getType()) {
		case BUY:
			order = executeBuyOrder(order, house, account);
			break;
		case SELL:
			order = executeSellOrder(order, house, account);
			break;
		}
		return null;
	}

	public Order executeBuyOrder(Order order, House house, Account account) {
		CurrencyAmount currencyAmountToPay = calculateCurrencyAmountToPay(order);
		if (hasBalance(account, currencyAmountToPay)) {
			LOGGER.info("Executing " + order + " on \"" + account.getOwner() + "\" account.");
			CurrencyAmount currencyAmountCommission = calculateBuyOrderComission(order, house);
			LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
			CurrencyAmount currencyAmountToDeposit = calculateBuyOrderDeposit(order, currencyAmountCommission);
			LOGGER.debug("Amount to withdraw is " + currencyAmountToPay + ".");
			depositComission(currencyAmountCommission, house, account);
			account.getBalance().withdraw(currencyAmountToPay);
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			account.getBalance().deposit(currencyAmountToDeposit);
			order.setStatus(OrderStatus.FILLED);
		} else {
			LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
					+ currencyAmountToPay.getCurrency() + " balance to execute " + order + ". Cancelling.");
			order.setStatus(OrderStatus.CANCELLED);
		}
		return order;
	}

	private CurrencyAmount calculateCurrencyAmountToPay(Order order) {
		MercadoBigDecimal amountToPay = order.getLimitPrice().multiply(order.getQuantity());
		Currency currencyToPay = order.getFirstCurrency();
		return new CurrencyAmount(currencyToPay, amountToPay);
	}

	private CurrencyAmount calculateCurrencyAmountToSell(Order order) {
		MercadoBigDecimal quantity = order.getQuantity();
		Currency currencyToSell = order.getSecondCurrency();
		return new CurrencyAmount(currencyToSell, quantity);
	}

	public Order executeSellOrder(Order order, House house, Account account) {
		CurrencyAmount currencyAmountToSell = calculateCurrencyAmountToSell(order);
		if (hasBalance(account, currencyAmountToSell)) {
			LOGGER.info("Executing " + order + " on \"" + account.getOwner() + "\" account.");
			CurrencyAmount currencyAmountCommission = calculateSellOrderCommission(order, house);
			CurrencyAmount currencyAmountToDeposit = calculateSellOrderDeposit(order, house, currencyAmountCommission);
			LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
			depositComission(currencyAmountCommission, house, account);
			LOGGER.debug("Amount to withdraw is " + currencyAmountToSell + ".");
			account.getBalance().withdraw(currencyAmountToSell);
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			account.getBalance().deposit(currencyAmountToDeposit);
			order.setStatus(OrderStatus.FILLED);
		} else {
			LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
					+ currencyAmountToSell.getCurrency() + " balance to execute sell " + order + ". Cancelling.");
			order.setStatus(OrderStatus.CANCELLED);
		}
		return order;
	}

	private boolean hasBalance(Account account, CurrencyAmount amountToHave) {
		Currency currency = amountToHave.getCurrency();
		CurrencyAmount balanceAmount = account.getBalance().get(currency);
		return (balanceAmount.getAmount().compareTo(amountToHave.getAmount()) >= 0);
	}

	private CurrencyAmount calculateBuyOrderComission(Order order, House house) {
		CurrencyAmount currencyAmountToBuy = elaborateCurrencyAmountToBuy(order);
		MercadoBigDecimal comissionAmount = currencyAmountToBuy.getAmount().multiply(house.getComissionPercentage());
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), comissionAmount);
	}

	private CurrencyAmount elaborateCurrencyAmountToBuy(Order order) {
		MercadoBigDecimal quantity = order.getQuantity();
		Currency currencyToBuy = order.getSecondCurrency();
		return new CurrencyAmount(currencyToBuy, quantity);
	}

	private CurrencyAmount calculateSellOrderCommission(Order order, House house) {
		CurrencyAmount currencyAmountToReceive = elaborateCurrencyAmountToReceive(order);
		MercadoBigDecimal comissionAmount = currencyAmountToReceive.getAmount().multiply(house.getComissionPercentage());
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), comissionAmount);
	}

	private CurrencyAmount elaborateCurrencyAmountToReceive(Order order) {
		MercadoBigDecimal amount = order.getQuantity().multiply(order.getLimitPrice());
		Currency currency = order.getFirstCurrency();
		return new CurrencyAmount(currency, amount);
	}

	private void depositComission(CurrencyAmount currencyAmountComission, House house, Account account) {
		Balance balance = house.getComissionBalance().getOrDefault(account.getOwner(), new Balance());
		balance.deposit(currencyAmountComission);
		house.getComissionBalance().put(account.getOwner(), balance);
	}

	private CurrencyAmount calculateBuyOrderDeposit(Order order, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToBuy = elaborateCurrencyAmountToBuy(order);
		MercadoBigDecimal amountToDeposit = currencyAmountToBuy.getAmount().subtract(currencyAmountComission.getAmount());
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), amountToDeposit);
	}

	private CurrencyAmount calculateSellOrderDeposit(Order order, House house, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToReceive = elaborateCurrencyAmountToReceive(order);
		MercadoBigDecimal amountToDeposit = currencyAmountToReceive.getAmount().subtract(currencyAmountComission.getAmount());
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), amountToDeposit);
	}
}
