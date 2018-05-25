package org.marceloleite.mercado.strategies.sixth.analyser;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.commons.Statistics;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphic;

public class UndefinedStatusAnalyser extends StatusAnalyserTemplate {

	private UndefinedStatusAnalyser() {
	};

	private UndefinedStatusAnalyser(Builder builder) {
		super(builder);
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public SixthStrategyStatus getStatus() {
		return SixthStrategyStatus.UNDEFINED;
	}

	@Override
	public Order analyse(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getStrategy().getCurrency());
		Statistics lastPriceStatistics = getStrategy().getStatistics()
				.getLastPriceStatistics();
		Order result = null;
		if (lastPriceStatistics.getRatio() > 0) {
			getStrategy().updateBase(temporalTicker);
			SixthStrategyGraphic graphic = getStrategy().getGraphic();
			if (graphic != null) {
				graphic.addLimitPointsOnGraphicData(temporalTicker.getStart());
			}
			result = createBuyOrder(timeInterval, account, house);
		}
		return result;
	}

	public static class Builder extends StatusAnalyserTemplate.Builder<Builder> {
		public UndefinedStatusAnalyser build() {
			return new UndefinedStatusAnalyser(this);
		}
	}

}
