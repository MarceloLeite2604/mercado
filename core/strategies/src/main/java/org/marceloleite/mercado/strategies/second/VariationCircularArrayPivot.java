package org.marceloleite.mercado.strategies.second;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.marceloleite.mercado.base.model.TemporalTickerVariation;
import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class VariationCircularArrayPivot {

	CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	public VariationCircularArrayPivot(CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray) {
		super();
		this.temporalTickerVariationCircularArray = temporalTickerVariationCircularArray;
	}

	public List<MercadoBigDecimal> getOrderVariations() {
		return elaborateList(TemporalTickerVariation::getOrderVariation);
	}
	
	public List<MercadoBigDecimal> getHighVariations() {
		return elaborateList(TemporalTickerVariation::getHighVariation);
	}
	
	private List<MercadoBigDecimal> elaborateList(Function<? super TemporalTickerVariation, ? extends MercadoBigDecimal> function) {
		return temporalTickerVariationCircularArray.asList().stream().map(function).collect(Collectors.toList());
	}
}
