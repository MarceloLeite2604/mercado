package org.marceloleite.mercado.strategies.second;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.CircularArray;

public class VariationCircularArrayPivot {

	CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray;

	public VariationCircularArrayPivot(CircularArray<TemporalTickerVariation> temporalTickerVariationCircularArray) {
		super();
		this.temporalTickerVariationCircularArray = temporalTickerVariationCircularArray;
	}

	public List<Double> getOrderVariations() {
		return elaborateList(TemporalTickerVariation::getOrderVariation);
	}
	
	public List<Double> getHighVariations() {
		return elaborateList(TemporalTickerVariation::getHighVariation);
	}
	
	private List<Double> elaborateList(Function<? super TemporalTickerVariation, ? extends Double> function) {
		return temporalTickerVariationCircularArray.asList().stream().map(function).collect(Collectors.toList());
	}
}
