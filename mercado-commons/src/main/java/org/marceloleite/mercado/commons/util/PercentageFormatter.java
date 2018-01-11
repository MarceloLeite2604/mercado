package org.marceloleite.mercado.commons.util;

import java.text.DecimalFormat;

public class PercentageFormatter {

	private static final String NUMBER_FORMAT = "#.##";

	public String format(double value) {
		DecimalFormat decimalFormatter = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormatter.format(value*100.0) + "%";
	}
}
