package org.marceloleite.mercado.orderanalyser;

public class NoBalanceForMinimalValueOrderAnalyserException extends OrderAnalyserException {

	private static final long serialVersionUID = 1L;
	
	public NoBalanceForMinimalValueOrderAnalyserException(String message) {
		super(message);
	}

}
