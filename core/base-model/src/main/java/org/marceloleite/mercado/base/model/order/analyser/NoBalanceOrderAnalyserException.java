package org.marceloleite.mercado.base.model.order.analyser;

public class NoBalanceOrderAnalyserException extends OrderAnalyserException {

	private static final long serialVersionUID = 1L;
	
	public NoBalanceOrderAnalyserException(String message) {
		super(message);
	}

}
