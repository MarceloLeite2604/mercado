package org.marceloleite.mercado.dao.database;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.TemporalTickerCreator;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.database.repository.TemporalTickerRepository;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Repository;

@Repository
@Named("TemporalTickerDatabaseDAO")
public class TemporalTickerDatabaseDAO implements TemporalTickerDAO {

	@Inject
	private TemporalTickerRepository temporalTickerRepository;

	@Inject
	@Named("TradeDatabaseSiteDAO")
	private TradeDAO tradesDAO;

	@Inject
	private TemporalTickerCreator temporalTickerCreator;

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
	public TemporalTicker findByCurrencyAndStartAndEnd(Currency currency, ZonedDateTime startTime,
			ZonedDateTime endTime) {
		TemporalTicker temporalTicker = temporalTickerRepository.findByCurrencyAndStartAndEnd(currency, startTime,
				endTime);
		if (temporalTicker == null) {
			temporalTicker = createTemporalTicker(currency, startTime, endTime);
			temporalTickerRepository.save(temporalTicker);
		}
		return temporalTicker;
	}

	private TemporalTicker createTemporalTicker(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TemporalTicker result = null;
		List<Trade> trades = tradesDAO.findByCurrencyAndTimeBetween(currency, start, end);
		result = temporalTickerCreator.create(currency, new TimeInterval(start, end), trades);
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends TemporalTicker> Optional<S> findById(Long id) {
		return (Optional<S>) temporalTickerRepository.findById(id);
	}

}
