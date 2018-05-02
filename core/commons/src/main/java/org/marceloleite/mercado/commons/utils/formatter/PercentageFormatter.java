package org.marceloleite.mercado.commons.utils.formatter;

import java.text.DecimalFormat;

public final class PercentageFormatter {

	private static final String NUMBER_FORMAT = "0.00";

	private PercentageFormatter() {
	}

	public static String format(Double value) {
		String result;
		if (value == Double.NaN || value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY) {
			result = Double.toString(value);
		} else {
			DecimalFormat decimalFormatter = new DecimalFormat(NUMBER_FORMAT);
			result = decimalFormatter.format(value * 100) + "%";
		}
		return result;
	}	
}
