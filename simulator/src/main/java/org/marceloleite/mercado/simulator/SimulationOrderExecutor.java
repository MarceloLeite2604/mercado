package org.marceloleite.mercado.simulator;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.OrderExecutor;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Wallet;

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
			((Wallet)account.getWallet())
					.withdraw(currencyAmountToPay);
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			((Wallet)account.getWallet())
					.deposit(currencyAmountToDeposit);
			order.setStatus(OrderStatus.FILLED);
		} else {
			LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
					+ currencyAmountToPay.getCurrency() + " balance to execute " + order + ". Cancelling.");
			order.setStatus(OrderStatus.CANCELLED);
		}
		return order;
	}

	private CurrencyAmount calculateCurrencyAmountToPay(Order order) {
		BigDecimal amountToPay = order.getLimitPrice()
				.multiply(order.getQuantity());
		Currency currencyToPay = order.getCurrencyPair().getFirstCurrency();
		return new CurrencyAmount(currencyToPay, amountToPay);
	}

	private CurrencyAmount calculateCurrencyAmountToSell(Order order) {
		BigDecimal quantity = order.getQuantity();
		Currency currencyToSell = order.getCurrencyPair().getSecondCurrency();
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
			((Wallet)account.getWallet())
					.withdraw(currencyAmountToSell);
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			((Wallet)account.getWallet())
					.deposit(currencyAmountToDeposit);
			order.setStatus(OrderStatus.FILLED);
		} else {
			LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
					+ currencyAmountToSell.getCurrency() + " balance to execute sell " + order + ". Cancelling.");
			order.setStatus(OrderStatus.CANCELLED);
		}
		return order;
	}

	private boolean hasBalance(Account account, CurrencyAmount amountToHave) {
		return ((Wallet)account.getWallet())
				.hasBalanceFor(amountToHave);
	}

	private CurrencyAmount calculateBuyOrderComission(Order order, House house) {
		CurrencyAmount currencyAmountToBuy = elaborateCurrencyAmountToBuy(order);
		BigDecimal comissionAmount = currencyAmountToBuy.getAmount()
				.multiply(new BigDecimal(house.getComissionPercentage()));
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), comissionAmount);
	}

	private CurrencyAmount elaborateCurrencyAmountToBuy(Order order) {
		BigDecimal quantity = order.getQuantity();
		Currency currencyToBuy = order.getCurrencyPair().getSecondCurrency();
		return new CurrencyAmount(currencyToBuy, quantity);
	}

	private CurrencyAmount calculateSellOrderCommission(Order order, House house) {
		CurrencyAmount currencyAmountToReceive = elaborateCurrencyAmountToReceive(order);
		BigDecimal comissionAmount = currencyAmountToReceive.getAmount()
				.multiply(new BigDecimal(house.getComissionPercentage()));
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), comissionAmount);
	}

	private CurrencyAmount elaborateCurrencyAmountToReceive(Order order) {
		BigDecimal amount = order.getQuantity()
				.multiply(order.getLimitPrice());
		Currency currency = order.getCurrencyPair().getFirstCurrency();
		return new CurrencyAmount(currency, amount);
	}

	private void depositComission(CurrencyAmount currencyAmountComission, House house, Account account) {
		house.getCommissionWalletFor(account)
				.deposit(currencyAmountComission);
	}

	private CurrencyAmount calculateBuyOrderDeposit(Order order, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToBuy = elaborateCurrencyAmountToBuy(order);
		BigDecimal amountToDeposit = currencyAmountToBuy.getAmount()
				.subtract(currencyAmountComission.getAmount());
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), amountToDeposit);
	}

	private CurrencyAmount calculateSellOrderDeposit(Order order, House house, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToReceive = elaborateCurrencyAmountToReceive(order);
		BigDecimal amountToDeposit = currencyAmountToReceive.getAmount()
				.subtract(currencyAmountComission.getAmount());
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), amountToDeposit);
	}
}
