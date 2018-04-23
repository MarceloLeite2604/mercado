package org.marceloleite.mercado.commons.formatter;

import java.text.DecimalFormat;

public class PercentageFormatter {

	private static PercentageFormatter instance;

	private static final String NUMBER_FORMAT = "0.00";

	private PercentageFormatter() {
	}

	public String format(Double value) {
		String result;
		if (value == Double.NaN || value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY) {
			result = Double.toString(value);
		} else {
			DecimalFormat decimalFormatter = new DecimalFormat(NUMBER_FORMAT);
			result = decimalFormatter.format(value * 100) + "%";
		}
		return result;
	}

	public static PercentageFormatter getInstance() {
		if (instance == null) {
			instance = new PercentageFormatter();
		}
		return instance;
	}
}
