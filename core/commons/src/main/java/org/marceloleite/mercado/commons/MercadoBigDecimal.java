package org.marceloleite.mercado.commons;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MercadoBigDecimal extends BigDecimal {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_SCALE = 16;

	private static final short POSITIVE_INFINITY_VALUE = 1;
	private static final short FINITE_NUMBER = 0;
	private static final short NEGATIVE_INFINITY_VALUE = -1;

	public static final MercadoBigDecimal POSITIVE_INFINITY = new MercadoBigDecimal(BigDecimal.ZERO,
			POSITIVE_INFINITY_VALUE);
	public static final MercadoBigDecimal NEGATIVE_INFINITY = new MercadoBigDecimal(BigDecimal.ZERO,
			NEGATIVE_INFINITY_VALUE);

	public MercadoBigDecimal(BigInteger bigInteger) {
		super(bigInteger);
		infinity = FINITE_NUMBER;
	}

	private MercadoBigDecimal(BigDecimal bigDecimal, short infinity) {
		super(bigDecimal.toString());
		this.infinity = infinity;
	}

	public MercadoBigDecimal(BigDecimal bigDecimal) {
		super(bigDecimal.toString());
		this.infinity = FINITE_NUMBER;
	}

	public short infinity;

	public MercadoBigDecimal divide(BigDecimal divisor) {
		return this.divide(divisor).setDefaultScale();
	}

	public MercadoBigDecimal multiply(BigDecimal multiplicand) {
		return new MercadoBigDecimal(this.multiply(multiplicand), this.infinity).setDefaultScale();
	}

	public MercadoBigDecimal add(BigDecimal multiplicand) {
		return new MercadoBigDecimal(this.add(multiplicand), this.infinity).setDefaultScale();
	}

	public MercadoBigDecimal subtract(BigDecimal multiplicand) {
		return new MercadoBigDecimal(this.subtract(multiplicand), this.infinity).setDefaultScale();
	}

	private MercadoBigDecimal setDefaultScale() {
		return new MercadoBigDecimal(this.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), this.infinity);
	}

	public short getInfinity() {
		return infinity;
	}

	public void setInfinity(short infinity) {
		this.infinity = infinity;
	}

	@Override
	public String toString() {
		switch (infinity) {
		case POSITIVE_INFINITY_VALUE:
			return new Double(Double.POSITIVE_INFINITY).toString();
		case NEGATIVE_INFINITY_VALUE:
			return new Double(Double.NEGATIVE_INFINITY).toString();
		default:
			return super.toString();
		}
	}
}
