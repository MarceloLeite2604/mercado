package org.marceloleite.mercado.simulator.order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.OrderExecutor;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.base.model.order.OrderStatus;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.commons.Currency;

public class OldSimulationOrderExecutor implements OrderExecutor {

	private static final Logger LOGGER = LogManager.getLogger(SimulationOrderExecutor.class);

	public BuyOrder executeBuyOrder(BuyOrder buyOrder, House house, Account account) {
		buyOrder.updateOrder(house.getTemporalTickers());
		CurrencyAmount currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
		if (hasBalance(account, currencyAmountToPay)) {
			LOGGER.info("Executing " + buyOrder + " on \"" + account.getOwner() + "\" account.");
			CurrencyAmount currencyAmountCommission = calculateComission(buyOrder, house);
			LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
			CurrencyAmount currencyAmountToDeposit = calculateDeposit(buyOrder, currencyAmountCommission);
			LOGGER.debug("Amount to withdraw is " + currencyAmountToPay + ".");
			depositComission(currencyAmountCommission, house, account);
			account.getBalance().withdraw(currencyAmountToPay);
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			account.getBalance().deposit(currencyAmountToDeposit);
			buyOrder.setOrderStatus(OrderStatus.EXECUTED);
		} else {
			LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
					+ currencyAmountToPay.getCurrency() + " balance to execute buy order. Cancelling.");
			buyOrder.setOrderStatus(OrderStatus.CANCELLED);
		}
		return buyOrder;
	}

	public SellOrder executeSellOrder(SellOrder sellOrder, House house, Account account) {
		sellOrder.updateOrder(house.getTemporalTickers());
		CurrencyAmount currencyAmountToSell = sellOrder.getCurrencyAmountToSell();
		if (hasBalance(account, currencyAmountToSell)) {
			LOGGER.info("Executing " + sellOrder + " on \"" + account.getOwner() + "\" account.");
			CurrencyAmount currencyAmountCommission = calculateCommission(sellOrder, house);
			CurrencyAmount currencyAmountToDeposit = calculateDeposit(sellOrder, currencyAmountCommission);
			LOGGER.debug("Commission amount is " + currencyAmountCommission + ".");
			depositComission(currencyAmountCommission, house, account);
			LOGGER.debug("Amount to withdraw is " + currencyAmountToSell + ".");
			account.getBalance().withdraw(currencyAmountToSell);
			LOGGER.debug("Amount to deposit is " + currencyAmountToDeposit + ".");
			account.getBalance().deposit(currencyAmountToDeposit);
			sellOrder.setOrderStatus(OrderStatus.EXECUTED);
		} else {
			LOGGER.info("Account \"" + account.getOwner() + "\" does not have enough "
					+ currencyAmountToSell.getCurrency() + " balance to execute sell order. Cancelling.");
			sellOrder.setOrderStatus(OrderStatus.CANCELLED);
		}
		return sellOrder;
	}

	private boolean hasBalance(Account account, CurrencyAmount amountToHave) {
		Currency currency = amountToHave.getCurrency();
		CurrencyAmount balanceAmount = account.getBalance().get(currency);
		return (balanceAmount.getAmount() >= amountToHave.getAmount());
	}

	private CurrencyAmount calculateComission(BuyOrder buyOrder, House house) {
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		double comissionAmount = currencyAmountToBuy.getAmount() * house.getComissionPercentage();
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), comissionAmount);
	}

	private CurrencyAmount calculateCommission(SellOrder sellOrder, House house) {
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		double comissionAmount = currencyAmountToReceive.getAmount() * house.getComissionPercentage();
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), comissionAmount);
	}

	private void depositComission(CurrencyAmount currencyAmountComission, House house, Account account) {
		Balance balance = house.getComissionBalance().getOrDefault(account.getOwner(), new Balance());
		balance.deposit(currencyAmountComission);
		house.getComissionBalance().put(account.getOwner(), balance);
	}

	private CurrencyAmount calculateDeposit(BuyOrder buyOrder, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		double amountToDeposit = currencyAmountToBuy.getAmount() - currencyAmountComission.getAmount();
		return new CurrencyAmount(currencyAmountToBuy.getCurrency(), amountToDeposit);
	}

	private CurrencyAmount calculateDeposit(SellOrder sellOrder, CurrencyAmount currencyAmountComission) {
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		double amountToDeposit = currencyAmountToReceive.getAmount() - currencyAmountComission.getAmount();
		return new CurrencyAmount(currencyAmountToReceive.getCurrency(), amountToDeposit);
	}
}
