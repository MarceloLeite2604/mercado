package org.marceloleite.mercado.orderanalyser.exception;

import org.marceloleite.mercado.orderanalyser.OrderAnalyserException;

public class NoBalanceForMinimalValueOrderAnalyserException extends OrderAnalyserException {

	private static final long serialVersionUID = 1L;
	
	public NoBalanceForMinimalValueOrderAnalyserException(String message) {
		super(message);
	}

}
