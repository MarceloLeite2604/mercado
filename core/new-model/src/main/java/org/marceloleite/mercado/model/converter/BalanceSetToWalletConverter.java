package org.marceloleite.mercado.model.converter;

import java.util.HashSet;
import java.util.Set;

import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.Wallet;

public final class BalanceSetToWalletConverter {
	
	private BalanceSetToWalletConverter() {
	}

	public static Wallet convertTo(Set<Balance> balances) {
		Wallet wallet = new Wallet();
		balances.forEach(wallet::add);
		return wallet;
	}

	public static Set<Balance> convertFrom(Wallet wallet) {
		Set<Balance> balances = new HashSet<>();
		wallet.forEach(balances::add);
		return balances;
	}

	
}
