package org.marceloleite.mercado.retriever.exception;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.TimeIntervalToStringConverter;

public class NoTemporalTickerForPeriodException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoTemporalTickerForPeriodException(Currency currency, TimeInterval timeInterval) {
		super("Could not retrieve temporal ticker for currency " + currency.getAcronym() + " on period "
				+ new TimeIntervalToStringConverter().convertTo(timeInterval) + ".");
	}

}
