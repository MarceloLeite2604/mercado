package org.marceloleite.mercado.orderanalyser;

public class NoBalanceOrderAnalyserException extends OrderAnalyserException {

	private static final long serialVersionUID = 1L;
	
	public NoBalanceOrderAnalyserException(String message) {
		super(message);
	}

}
