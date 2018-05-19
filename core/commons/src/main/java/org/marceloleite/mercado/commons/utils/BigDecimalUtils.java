package org.marceloleite.mercado.commons.utils;

import java.math.BigDecimal;

public final class BigDecimalUtils {
	
	public static final int DEFAULT_SCALE = 12;
	
	public static final int DEFAULT_ROUNDING = BigDecimal.ROUND_HALF_EVEN;
	
	private BigDecimalUtils() {}
	
	public static BigDecimal setDefaultScale(BigDecimal bigDecimal) {
		return bigDecimal.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
	}

}
