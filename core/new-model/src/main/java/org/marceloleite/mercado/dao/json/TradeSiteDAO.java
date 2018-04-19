package org.marceloleite.mercado.dao.json;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.dao.json.siteretriever.trade.TradeSiteRetriever;
import org.marceloleite.mercado.model.Trade;

@Named("TradeSiteDAO")
public class TradeSiteDAO implements TradeDAO {
	
	@Inject
	private TradeSiteRetriever tradeSiteRetriever;

	@Override
	public <S extends Trade> S save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		return tradeSiteRetriever.retrieve(currency, new TimeInterval(start, end));
	}

}
