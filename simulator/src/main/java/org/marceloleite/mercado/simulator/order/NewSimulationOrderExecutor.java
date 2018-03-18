package org.marceloleite.mercado.simulator.order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.NewOrderExecutor;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;

public class NewSimulationOrderExecutor implements NewOrderExecutor {

	private static final Logger LOGGER = LogManager.getLogger(NewSimulationOrderExecutor.class);

	@Override
	public Order placeOrder(Order order, House house, Account account) {
		switch (order.getType()) {
		case BUY:
			order = executeBuyOrder(order, house, account);
		case SELL:
			order = executeSellOrder(order, house, account);
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
					+ currencyAmountToPay.getCurrency() + " balance to execute buy order. Cancelling.");
			order.setStatus(OrderStatus.CANCELLED);
		}
		return order;
	}

	private CurrencyAmount calculateCurrencyAmountToPay(Order order) {
		Double amountToPay = order.getLimitPrice() * order.getQuantity();
		Currency currencyToPay = order.getFirstCurrency();
		return new CurrencyAmount(currencyToPay, amountToPay);
	}
	
	private CurrencyAmount calculateCurrencyAmountToSell(Order order) {
		Double quantity = order.getQuantity();
		Currency currencyToSell = order.getSecondCurrency();
		return new CurrencyAmount(currencyToSell, quantity);
	}

	

	public Order executeSellOrder(Order order, House house, Account account) {
		CurrencyAmount currencyAmountToSell = calculateCurrencyAmountToSell(order);
		if (hasBalance(account, currencyAmountToSell)) {
			LOGGER.info("Executing " + order + " on \"" + account.getOwner() + "\" account.");
			CurrencyAmount currencyAmountCommission = calculateSellOrderCommission(order, house);
			CurrencyAmount currencyAmountToDeposit = calculateSellOrderDeposit(order, currencyAmountCommission);
			LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
			depositComission(currencyAmountCommission, house, account);
			LOGGER.debug("Amount to withdraw is " + currencyAmountToSell + ".");
			account.getBalance().withdraw(currencyAmountToSell);
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			account.getBalance().deposit(currencyAmountToDeposit);
			order.setStatus(OrderStatus.FILLED);
		} else {
			LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
					+ currencyAmountToSell.getCurrency() + " balance to execute sell order. Cancelling.");
			order.setStatus(OrderStatus.CANCELLED);
		}
		return order;
	}

	private boolean hasBalance(Account account, CurrencyAmount amountToHave) {
		Currency currency = amountToHave.getCurrency();
		CurrencyAmount balanceAmount = account.getBalance().get(currency);
		return (balanceAmount.getAmount() >= amountToHave.getAmount());
	}

	private CurrencyAmount calculateBuyOrderComission(Order order, House house) {
		CurrencyAmount currencyAmountToBuy = elaborateCurrencyAmountToBuy(order);
		double comissionAmount = currencyAmountToBuy.getAmount() * house.getComissionPercentage();
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), comissionAmount);
	}

	private CurrencyAmount elaborateCurrencyAmountToBuy(Order order) {
		Double quantity = order.getQuantity();
		Currency currencyToBuy = order.getSecondCurrency();
		return new CurrencyAmount(currencyToBuy, quantity);
	}

	private CurrencyAmount calculateSellOrderCommission(Order order, House house) {
		CurrencyAmount currencyAmountToReceive = elaborateCurrencyAmountToReceive(order);
		double comissionAmount = currencyAmountToReceive.getAmount() * house.getComissionPercentage();
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), comissionAmount);
	}

	private CurrencyAmount elaborateCurrencyAmountToReceive(Order order) {
		Double quantity = order.getQuantity();
		Currency firstCurrency = order.getFirstCurrency();
		return new CurrencyAmount(firstCurrency, quantity);
	}

	private void depositComission(CurrencyAmount currencyAmountComission, House house, Account account) {
		Balance balance = house.getComissionBalance().getOrDefault(account.getOwner(), new Balance());
		balance.deposit(currencyAmountComission);
		house.getComissionBalance().put(account.getOwner(), balance);
	}

	private CurrencyAmount calculateBuyOrderDeposit(Order order, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToBuy = elaborateCurrencyAmountToBuy(order);
		double amountToDeposit = currencyAmountToBuy.getAmount() - currencyAmountComission.getAmount();
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), amountToDeposit);
	}
	
	private CurrencyAmount calculateSellOrderDeposit(Order order, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToReceive = elaborateCurrencyAmountToReceive(order);
		double amountToDeposit = currencyAmountToReceive.getAmount() - currencyAmountComission.getAmount();
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), amountToDeposit);
	}
}
