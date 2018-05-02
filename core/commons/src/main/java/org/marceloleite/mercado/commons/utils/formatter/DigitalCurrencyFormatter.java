package org.marceloleite.mercado.commons.utils.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class DigitalCurrencyFormatter {
	
	private static final String NUMBER_FORMAT = "0.00000000";
	
	private DigitalCurrencyFormatter() {
	}
	
	public static String format(BigDecimal amount) {
		DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormat.format(amount);
	}
	
	public static String format(Double amount) {
		return format(new BigDecimal(amount));
	}
}
