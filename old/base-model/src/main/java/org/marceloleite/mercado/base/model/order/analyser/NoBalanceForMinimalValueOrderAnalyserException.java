package org.marceloleite.mercado.base.model.order.analyser;

public class NoBalanceForMinimalValueOrderAnalyserException extends OrderAnalyserException {

	private static final long serialVersionUID = 1L;
	
	public NoBalanceForMinimalValueOrderAnalyserException(String message) {
		super(message);
	}

}
