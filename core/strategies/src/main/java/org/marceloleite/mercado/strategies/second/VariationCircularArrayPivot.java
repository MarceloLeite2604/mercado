package org.marceloleite.mercado.strategies.second;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.marceloleite.mercado.base.model.TemporalTickerVariation;

public class VariationCircularArrayPivot {

	CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	public VariationCircularArrayPivot(CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray) {
		super();
		this.temporalTickerVariationCircularArray = temporalTickerVariationCircularArray;
	}

	public List<BigDecimal> getOrderVariations() {
		return elaborateList(TemporalTickerVariation::getOrderVariation);
	}
	
	public List<BigDecimal> getHighVariations() {
		return elaborateList(TemporalTickerVariation::getHighVariation);
	}
	
	private List<BigDecimal> elaborateList(Function<? super TemporalTickerVariation, ? extends BigDecimal> function) {
		return temporalTickerVariationCircularArray.asList().stream().map(function).collect(Collectors.toList());
	}
}
