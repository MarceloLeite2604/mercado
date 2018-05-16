package org.marceloleite.mercado.dao.site.siteretriever.trade;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.dao.site.siteretriever.AbstractSiteRetriever;
import org.marceloleite.mercado.model.Trade;
import org.springframework.web.client.RestTemplate;

class TSRPartial extends AbstractSiteRetriever {

	private static final Logger LOGGER = LogManager.getLogger(TSRPartial.class);

	private static final String METHOD = "trades";

	private static final long MAX_RETRIES = 5l;

	private static final long WAIT_TIME = 500l;

	public TSRResult retrieve(TSRParameters tsrParameters) {
//		LOGGER.debug(
//				Thread.currentThread().getId() + " Retrieving " + tsrParameters.getCurrency() + " currency for " + tsrParameters.getTimeInterval() + ".");
		
		TSRResult result = null;

		boolean concluded = false;
		long retries = 0l;
		List<Trade> trades = null;
		while (!concluded) {
			try {
				RestTemplate restTemplate = new RestTemplate();
				URI uri = elaborateURL(tsrParameters);
				Trade[] tradesArray = restTemplate.getForEntity(uri, Trade[].class)
						.getBody();
				result = new TSRResult(tsrParameters, Arrays.asList(tradesArray));
				Optional.ofNullable(trades)
						.orElse(new ArrayList<>())
						.stream()
						.forEach(trade -> trade.setCurrency(tsrParameters.getCurrency()));
				concluded = true;
			} catch (NotFoundException notFoundException) {
				if (++retries > MAX_RETRIES) {
					result = new TSRResult(tsrParameters, notFoundException);
					concluded = true;
				}
				try {
					Thread.sleep(WAIT_TIME);
				} catch (InterruptedException exception) {
					result = new TSRResult(tsrParameters,
							new RuntimeException("Exception occurred while thread sleeping.", exception));
					concluded = true;
				}
			}
		}

		return result;
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}

	protected URI elaborateURL(TSRParameters tsrParameters) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(TARGET_URL);
		stringBuilder.append(String.format(BASE_PATH_TEMPLATE, tsrParameters.getCurrency(), getMethod()));
		stringBuilder.append(String.format("%d/%d/",
				ZonedDateTimeUtils.formatAsEpochTime(tsrParameters.getTimeInterval()
						.getStart()),
				ZonedDateTimeUtils.formatAsEpochTime(tsrParameters.getTimeInterval()
						.getEnd())));
		try {
			return new URI(stringBuilder.toString());
		} catch (URISyntaxException exception) {
			throw new RuntimeException("Error while elaborating URI.", exception);
		}
	}
}
