package org.marceloleite.mercado.dao.site.siteretriever.trade;

import java.util.List;

import org.marceloleite.mercado.model.Trade;

public class TSRResult {

	private static final int MAX_TRADES_RETRIEVED = 1000;
	
	private TSRParameters tsrParameters;

	private TSRResultStatus tsrResultStatus;

	private List<Trade> trades;

	private Exception exception;

	public TSRResult(TSRParameters tsrParameters, List<Trade> trades) {
		super();
		this.tsrParameters = tsrParameters;
		this.trades = trades;
		this.tsrResultStatus = checkResultStatus();
	}

	public TSRResult(TSRParameters tsrParameters, Exception exception) {
		super();
		this.tsrParameters = tsrParameters;
		this.exception = exception;
		this.tsrResultStatus = TSRResultStatus.FAILURE;
	}

	public TSRParameters getTsrParameters() {
		return tsrParameters;
	}

	public TSRResultStatus getTsrResultStatus() {
		return tsrResultStatus;
	}

	public List<Trade> getTrades() {
		return trades;
	}

	public Exception getException() {
		return exception;
	}

	private TSRResultStatus checkResultStatus() {
		if (trades.size() >= MAX_TRADES_RETRIEVED) {
			return TSRResultStatus.MAX_TRADES_REACHED;
		} else {
			return TSRResultStatus.OK;
		}
	}
}
