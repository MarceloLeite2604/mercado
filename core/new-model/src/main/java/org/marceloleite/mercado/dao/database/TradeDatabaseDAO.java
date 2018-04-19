package org.marceloleite.mercado.dao.database;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.dao.database.repository.TradeRepository;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.Trade;

@Named("TradeDatabaseDAO")
public class TradeDatabaseDAO implements TradeDAO {

	@Inject
	private TradeRepository tradeRepository;

	@Override
	public <S extends Trade> S save(S trade) {
		return tradeRepository.save(trade);
	}

	@Override
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		return tradeRepository.findByCurrencyAndTimeBetween(currency, start, end);
	}
}
