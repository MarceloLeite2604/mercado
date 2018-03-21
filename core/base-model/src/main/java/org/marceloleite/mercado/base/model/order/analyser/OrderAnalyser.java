package org.marceloleite.mercado.base.model.order.analyser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.order.MinimalAmounts;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;

public class OrderAnalyser {

	private static final Logger LOGGER = LogManager.getLogger(OrderAnalyser.class);

	private Balance balance;

	private OrderType orderType;

	private CurrencyAmount first;

	private CurrencyAmount second;

	private CurrencyAmount unitPrice;

	private boolean cancelled;

	public OrderAnalyser(Balance balance, OrderType orderType, CurrencyAmount unitPrice, Currency firstCurrency,
			Currency secondCurrency) {
		this.balance = balance;
		this.cancelled = false;
		this.orderType = orderType;
		this.unitPrice = unitPrice;
		this.first = new CurrencyAmount(firstCurrency, 0.0);
		this.second = new CurrencyAmount(secondCurrency, 0.0);
	}

	public CurrencyAmount getFirst() {
		return first;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setFirst(CurrencyAmount first)
			throws NoBalanceOrderAnalyserException, MinimalValueOrderAnalyserException {
		setFirst(first, false);
	}

	private void setFirst(CurrencyAmount first, boolean adjustingFromSecond)
			throws NoBalanceOrderAnalyserException, MinimalValueOrderAnalyserException {
		CurrencyAmount firstAdjusted = first;
		if (orderType == OrderType.BUY) {
			firstAdjusted = checkBalance(first);
		}

		if (!adjustingFromSecond || (adjustingFromSecond && first != firstAdjusted)) {
			CurrencyAmount relativeSecond = calculateSecondFor(firstAdjusted);
			setSecond(relativeSecond, true);
		}
		this.first = firstAdjusted;
	}

	public CurrencyAmount getSecond() {
		return second;
	}

	public void setSecond(CurrencyAmount second)
			throws MinimalValueOrderAnalyserException, NoBalanceOrderAnalyserException {
		setSecond(second, false);
	}

	private void setSecond(CurrencyAmount second, boolean adjustingFromFirst)
			throws MinimalValueOrderAnalyserException, NoBalanceOrderAnalyserException {
		CurrencyAmount secondAdjusted = checkMinimal(second);
		if (orderType == OrderType.SELL) {
			secondAdjusted = checkBalance(secondAdjusted);
		}
		if (!adjustingFromFirst || (adjustingFromFirst && (second != secondAdjusted))) {
			CurrencyAmount relativefirst = calculateFirstFor(secondAdjusted);
			setFirst(relativefirst, true);
		}
		this.second = secondAdjusted;
	}

	private CurrencyAmount checkMinimal(CurrencyAmount currencyAmount) throws MinimalValueOrderAnalyserException {
		if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmount)) {
			Double minimalAmount = MinimalAmounts.retrieveMinimalAmountFor(currencyAmount.getCurrency());
			CurrencyAmount minimal = new CurrencyAmount(currencyAmount.getCurrency(), minimalAmount);
			LOGGER.debug(currencyAmount + " is lower than minimal of " + minimal + ".");
			
			if (balance.hasBalance(currencyAmount)) {
				LOGGER.debug("Increasing value to " + minimal + ".");
				return minimal;
			} else {
				throw new MinimalValueOrderAnalyserException(currencyAmount + " is lower than minimal "
						+ new NonDigitalCurrencyFormatter().format(minimalAmount) + ".");
			}
		}
		return currencyAmount;
	}

	private CurrencyAmount checkBalance(CurrencyAmount currencyAmount) throws NoBalanceOrderAnalyserException {
		if (!balance.hasBalance(currencyAmount)) {
			CurrencyAmount currencyAmountBalance = balance.get(currencyAmount.getCurrency());
			LOGGER.debug(currencyAmount + " is higher than " + currencyAmountBalance + " in balance.");
			CurrencyAmount secondCalculated = calculateSecondFor(currencyAmountBalance);
			if (!MinimalAmounts.isAmountLowerThanMinimal(secondCalculated)) {
				second = secondCalculated;
				LOGGER.debug("Lowering value to " + currencyAmountBalance + ".");
				return currencyAmountBalance;
			} else {
				throw new NoBalanceOrderAnalyserException("Not enough balance for " + currencyAmount);
			}
		}
		return currencyAmount;
	}

	private CurrencyAmount calculateSecondFor(CurrencyAmount currencyAmount) {
		double amount = currencyAmount.getAmount() / unitPrice.getAmount();
		return new CurrencyAmount(second.getCurrency(), amount);
	}

	private CurrencyAmount calculateFirstFor(CurrencyAmount currencyAmount) {
		double amount = currencyAmount.getAmount() * unitPrice.getAmount();
		return new CurrencyAmount(first.getCurrency(), amount);
	}

	public CurrencyAmount getUnitPrice() {
		return unitPrice;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}