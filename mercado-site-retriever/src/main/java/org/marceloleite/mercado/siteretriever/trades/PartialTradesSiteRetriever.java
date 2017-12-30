package org.marceloleite.mercado.siteretriever.trades;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Retriever;
import org.marceloleite.mercado.commons.util.UnixTimeSeconds;
import org.marceloleite.mercado.siteretriever.AbstractSiteRetriever;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

class PartialTradesSiteRetriever extends AbstractSiteRetriever implements Retriever<List<JsonTrade>> {

	private static final long MAX_RETRIES = 5l;

	private static final long WAIT_TIME = 500l;

	public PartialTradesSiteRetriever(Currency currency) {
		super(currency);
	}

	private static final String METHOD = "trades";

	public List<JsonTrade> retrieve(Object... args) {

		LocalDateTime from = null;
		if (args[0] instanceof LocalDateTime) {
			from = (LocalDateTime) args[0];
		}

		LocalDateTime to = null;
		if (args[1] instanceof LocalDateTime) {
			to = (LocalDateTime) args[1];

		}

		boolean concluded = false;
		long retries = 0l;
		List<JsonTrade> jsonTrades = null;
		while (!concluded) {
			try {
				jsonTrades = Arrays.asList(createWebTarget().path(getPathWithParameters(from, to))
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

	@Override
	protected String getMethod() {
		return METHOD;
	}

	private String getPathWithParameters(LocalDateTime from, LocalDateTime to) {
		return String.format(getPath() + "%d/%d/", new UnixTimeSeconds(from).get(), new UnixTimeSeconds(to).get());
	}

}
