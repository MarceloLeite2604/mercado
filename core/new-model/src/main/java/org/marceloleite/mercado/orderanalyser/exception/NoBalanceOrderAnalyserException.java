package org.marceloleite.mercado.orderanalyser.exception;

import org.marceloleite.mercado.orderanalyser.OrderAnalyserException;

public class NoBalanceOrderAnalyserException extends OrderAnalyserException {

	private static final long serialVersionUID = 1L;
	
	public NoBalanceOrderAnalyserException(String message) {
		super(message);
	}

}
