package org.marceloleite.mercado.siteretriever.trades;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.EpochSecondsToLocalDateTimeConveter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;
import org.marceloleite.mercado.siteretriever.AbstractSiteRetriever;

class PartialTradesSiteRetriever extends AbstractSiteRetriever {

	private static final long MAX_RETRIES = 5l;

	private static final long WAIT_TIME = 500l;

	public PartialTradesSiteRetriever(Currency currency) {
		super(currency);
	}

	private static final String METHOD = "trades";

	public List<JsonTrade> retrieve(TimeInterval timeInterval) {

		checkArguments(timeInterval);

		boolean concluded = false;
		long retries = 0l;
		List<JsonTrade> jsonTrades = null;
		while (!concluded) {
			try {
				jsonTrades = Arrays.asList(createWebTarget().path(getPathWithParameters(timeInterval))
						.request(MediaType.APPLICATION_JSON).get(JsonTrade[].class));
				concluded = true;
			} catch (NotFoundException notFoundException) {
				if (++retries > MAX_RETRIES) {
					throw notFoundException;
				}
				try {
					Thread.sleep(WAIT_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		jsonTrades.stream().forEach(jsonTrade -> jsonTrade.setCurrency(currency));
		return jsonTrades;
	}

	private void checkArguments(TimeInterval timeInterval) {
		if (timeInterval == null) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}

	private String getPathWithParameters(TimeInterval timeInterval) {
		EpochSecondsToLocalDateTimeConveter epochSecondsToLocalDateTimeConveter = new EpochSecondsToLocalDateTimeConveter();
		return String.format(getPath() + "%d/%d/",
				epochSecondsToLocalDateTimeConveter.convertTo(timeInterval.getStart()),
				epochSecondsToLocalDateTimeConveter.convertTo(timeInterval.getEnd()));
	}

}
