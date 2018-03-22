package org.marceloleite.mercado.commons.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PercentageFormatter {

	private static final String NUMBER_FORMAT = "0.00";

	public String format(BigDecimal value) {
		DecimalFormat decimalFormatter = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormatter.format(value.multiply(new BigDecimal("100"))) + " %";
	}
}
