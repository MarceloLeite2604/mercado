package org.marceloleite.mercado.configurator;

import org.marceloleite.mercado.commons.interfaces.Checker;

public class NonceChecker implements Checker<Long> {

	@Override
	public boolean check(Long nonceValue) {
		if (nonceValue < 0) {
			System.out.println("Nonce value cannot be negative.");
			return false;
		} else if (nonceValue == 0) {
			System.out.println("Nonce value cannot de zero.");
			return false;
		}
		return true;
	}

}
