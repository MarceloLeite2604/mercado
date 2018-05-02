package org.marceloleite.mercado.dao.site.siteretriever.trade;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.dao.site.siteretriever.AbstractSiteRetriever;
import org.marceloleite.mercado.model.Trade;
import org.springframework.web.client.RestTemplate;

class PartialTradeSiteRetriever extends AbstractSiteRetriever {

	private static final String METHOD = "trades";

	private static final long MAX_RETRIES = 5l;

	private static final long WAIT_TIME = 500l;

	public PartialTradeSiteRetriever() {
		super();
	}

	public List<Trade> retrieve(Currency currency, TimeInterval timeInterval) {

		checkArguments(currency, timeInterval);

		boolean concluded = false;
		long retries = 0l;
		List<Trade> trades = null;
		while (!concluded) {
			try {
				RestTemplate restTemplate = new RestTemplate();
				URI uri = elaborateURL(currency, timeInterval);
				Trade[] tradesArray = restTemplate.getForEntity(uri, Trade[].class)
						.getBody();
				trades = Arrays.asList(tradesArray);
				concluded = true;
			} catch (NotFoundException notFoundException) {
				if (++retries > MAX_RETRIES) {
					throw notFoundException;
				}
				try {
					Thread.sleep(WAIT_TIME);
				} catch (InterruptedException exception) {
					throw new RuntimeException("Exception occurred while thread sleeping.", exception);
				}
			}
		}
		trades.stream()
				.forEach(trade -> trade.setCurrency(currency));
		return trades;
	}

	private void checkArguments(Currency currency, TimeInterval timeInterval) {
		if (currency == null) {
			throw new IllegalArgumentException("Currency cannot be null.");
		}
		if (timeInterval == null) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}

	protected URI elaborateURL(Currency currency, TimeInterval timeInterval) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(TARGET_URL);
		stringBuilder.append(String.format(BASE_PATH_TEMPLATE, currency, getMethod()));
		stringBuilder.append(String.format("%d/%d/", ZonedDateTimeUtils.formatAsEpochTime(timeInterval.getStart()),
				ZonedDateTimeUtils.formatAsEpochTime(timeInterval.getEnd())));
		try {
			return new URI(stringBuilder.toString());
		} catch (URISyntaxException exception) {
			throw new RuntimeException("Error while elaborating URI.", exception);
		}
	}
}
