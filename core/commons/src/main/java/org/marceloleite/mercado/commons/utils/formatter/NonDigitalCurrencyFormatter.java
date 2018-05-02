package org.marceloleite.mercado.commons.utils.formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class NonDigitalCurrencyFormatter {
	
	private static final String NUMBER_FORMAT = "0.00000";
	
	private NonDigitalCurrencyFormatter() {
	}
	
	public static String format(double amount) {
		return format(new BigDecimal(amount));
	}

	public static String format(BigDecimal amount) {
		DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT);
		return decimalFormat.format(amount);
	}
}
