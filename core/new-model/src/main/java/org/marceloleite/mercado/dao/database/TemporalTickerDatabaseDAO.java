package org.marceloleite.mercado.dao.database;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.dao.database.repository.TemporalTickerRepository;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Trade;
import org.springframework.beans.factory.annotation.Qualifier;

@Named("TemporalTickerDatabaseDAO")
public class TemporalTickerDatabaseDAO implements TemporalTickerDAO {
	
	@Inject
	private TemporalTickerRepository temporalTickerRepository;
	
	@Inject
	@Qualifier("TradesDatabaseSiteDAO")
	private TradeDAO tradesDAO;

	@Override
	public <S extends TemporalTicker> S save(S entity) {
		return temporalTickerRepository.save(entity);
	}

	@Override
	public <S extends TemporalTicker> Iterable<S> saveAll(Iterable<S> entities) {
		return temporalTickerRepository.saveAll(entities);
	}

	@Override
	public Iterable<TemporalTicker> findAll() {
		return temporalTickerRepository.findAll();
	}

	@Override
	public TemporalTicker findByCurrencyAndStartTimeAndEndTime(Currency currency, ZonedDateTime startTime,
			ZonedDateTime endTime) {
		TemporalTicker temporalTicker = temporalTickerRepository.findByCurrencyAndStartTimeAndEndTime(currency, startTime, endTime);
		if (temporalTicker == null) {
			temporalTicker = createTemporalTicker(currency, startTime, endTime);
		}
		return temporalTicker;
	}

	private TemporalTicker createTemporalTicker(Currency currency, ZonedDateTime startTime, ZonedDateTime endTime) {
		List<Trade> trades = tradesDAO.findByCurrencyAndTimeBetween(currency, startTime, endTime);
		TemporalTickerCre
		return null;
	}

}
