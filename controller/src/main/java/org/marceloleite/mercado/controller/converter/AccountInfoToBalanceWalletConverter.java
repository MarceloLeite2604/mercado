package org.marceloleite.mercado.controller.converter;

import java.util.Map.Entry;

import org.marceloleite.mercado.api.negotiation.model.AccountInfo;
import org.marceloleite.mercado.api.negotiation.model.Currencies;
import org.marceloleite.mercado.api.negotiation.model.CurrencyInfo;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.Wallet;
import org.springframework.util.CollectionUtils;

public class AccountInfoToBalanceWalletConverter implements Converter<AccountInfo, Wallet> {

	private static AccountInfoToBalanceWalletConverter instance;

	private AccountInfoToBalanceWalletConverter() {
	}

	@Override
	public Wallet convertTo(AccountInfo accountInfo) {
		Wallet wallet = new Wallet();
		if (!CollectionUtils.isEmpty(accountInfo.getBalance())) {
			for (Entry<Currency, CurrencyInfo> entry : accountInfo.getBalance()
					.entrySet()) {
				wallet.add(createBalanceFromEntry(entry));
			}
		}
		return wallet;
	}

	private Balance createBalanceFromEntry(Entry<Currency, CurrencyInfo> entry) {
		Currency currency = entry.getKey();
		CurrencyInfo currencyInfo = entry.getValue();
		Balance balance = new Balance();
		balance.setCurrency(currency);
		balance.setAmount(currencyInfo.getTotal());
		return balance;
	}

	@Override
	public AccountInfo convertFrom(Wallet wallet) {
		throw new UnsupportedOperationException();
	}

	public static AccountInfoToBalanceWalletConverter getInstance() {
		if (instance == null) {
			instance = new AccountInfoToBalanceWalletConverter();
		}
		return instance;
	}

}
