package org.marceloleite.mercado.dao.site.siteretriever.trade;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;

public class TSRParameters {

	private Currency currency;

	private TimeInterval timeInterval;

	public TSRParameters(Currency currency, TimeInterval timeInterval) {
		super();
		checkArguments(currency, timeInterval);
		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	public Currency getCurrency() {
		return currency;
	}

	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

	private void checkArguments(Currency currency, TimeInterval timeInterval) {
		if (currency == null) {
			throw new IllegalArgumentException("Currency cannot be null.");
		}
		if (timeInterval == null) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}
}
