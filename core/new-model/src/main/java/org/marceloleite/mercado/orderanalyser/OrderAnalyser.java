package org.marceloleite.mercado.orderanalyser;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.MinimalAmounts;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.utils.BigDecimalUtils;
import org.marceloleite.mercado.commons.utils.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceOrderAnalyserException;

public class OrderAnalyser {

	private static final Logger LOGGER = LogManager.getLogger(OrderAnalyser.class);

	private Account account;

	private OrderType orderType;

	private CurrencyAmount first;

	private CurrencyAmount second;

	private CurrencyAmount unitPrice;

	private boolean cancelled;

	public OrderAnalyser(Account account, OrderType orderType, CurrencyAmount unitPrice, Currency firstCurrency,
			Currency secondCurrency) {
		this.account = account;
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
			throws NoBalanceOrderAnalyserException, NoBalanceForMinimalValueOrderAnalyserException {
		setFirst(first, false);
	}

	private void setFirst(CurrencyAmount first, boolean adjustingFromSecond)
			throws NoBalanceOrderAnalyserException, NoBalanceForMinimalValueOrderAnalyserException {
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
			throws NoBalanceForMinimalValueOrderAnalyserException, NoBalanceOrderAnalyserException {
		setSecond(second, false);
	}

	private void setSecond(CurrencyAmount second, boolean adjustingFromFirst)
			throws NoBalanceForMinimalValueOrderAnalyserException, NoBalanceOrderAnalyserException {
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

	private CurrencyAmount checkMinimal(CurrencyAmount currencyAmount)
			throws NoBalanceForMinimalValueOrderAnalyserException {
		if (MinimalAmounts.getInstance()
				.isAmountLowerThanMinimal(currencyAmount)) {
			double minimalAmount = MinimalAmounts.getInstance()
					.retrieveMinimalAmountFor(currencyAmount.getCurrency());
			CurrencyAmount minimal = new CurrencyAmount(currencyAmount.getCurrency(), new BigDecimal(minimalAmount));
			LOGGER.debug(currencyAmount + " is lower than minimal of " + minimal + ".");

			if (account.getWallet()
					.hasBalanceFor(currencyAmount)) {
				LOGGER.debug("Increasing value to " + minimal + ".");
				return minimal;
			} else {
				throw new NoBalanceForMinimalValueOrderAnalyserException(currencyAmount + " is lower than minimal "
						+ NonDigitalCurrencyFormatter.format(minimalAmount) + ".");
			}
		}
		return currencyAmount;
	}

	private CurrencyAmount checkBalance(CurrencyAmount currencyAmount) throws NoBalanceOrderAnalyserException {
		if (!account.getWallet()
				.hasBalanceFor(currencyAmount)) {
			CurrencyAmount currencyAmountBalance = account.getWallet()
					.getBalanceFor(currencyAmount.getCurrency())
					.asCurrencyAmount();
			LOGGER.debug(currencyAmount + " is higher than " + currencyAmountBalance + " in balance.");
			CurrencyAmount secondCalculated = calculateSecondFor(currencyAmountBalance);
			if (!MinimalAmounts.getInstance()
					.isAmountLowerThanMinimal(secondCalculated)) {
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
		BigDecimal amount = BigDecimalUtils.setDefaultScale(currencyAmount.getAmount())
				.divide(unitPrice.getAmount(), BigDecimalUtils.DEFAULT_ROUNDING);
		return new CurrencyAmount(second.getCurrency(), amount);
	}

	private CurrencyAmount calculateFirstFor(CurrencyAmount currencyAmount) {
		BigDecimal amount = BigDecimalUtils.setDefaultScale(currencyAmount.getAmount())
				.multiply(unitPrice.getAmount());
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