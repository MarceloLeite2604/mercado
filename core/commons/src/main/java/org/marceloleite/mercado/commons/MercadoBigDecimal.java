package org.marceloleite.mercado.commons;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MercadoBigDecimal extends BigDecimal {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_SCALE = 8;

	public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

	private static final short POSITIVE_INFINITY_TYPE = 1;
	private static final short FINITE_TYPE = 0;
	private static final short NEGATIVE_INFINITY_TYPE = -1;
	private static final short NOT_A_NUMBER_TYPE = -2;

	public static final MercadoBigDecimal POSITIVE_INFINITY = new MercadoBigDecimal(BigDecimal.ZERO,
			POSITIVE_INFINITY_TYPE);
	public static final MercadoBigDecimal NEGATIVE_INFINITY = new MercadoBigDecimal(BigDecimal.ZERO,
			NEGATIVE_INFINITY_TYPE);
	public static final MercadoBigDecimal NOT_A_NUMBER = new MercadoBigDecimal(BigDecimal.ZERO, NOT_A_NUMBER_TYPE);
	
	public static final MercadoBigDecimal ZERO = new MercadoBigDecimal(BigDecimal.ZERO);
	
	public static final MercadoBigDecimal ONE = new MercadoBigDecimal(BigDecimal.ONE);

	public short numberType;

	public MercadoBigDecimal() {
		super("0");
		this.numberType = FINITE_TYPE;
	}

	public MercadoBigDecimal(String string, short numberType) {
		super(string);
		this.numberType = FINITE_TYPE;
	}

	public MercadoBigDecimal(String string) {
		this(string, FINITE_TYPE);
	}

	public MercadoBigDecimal(Double value) {
		this(value.toString());

		if (value == Double.NEGATIVE_INFINITY) {
			numberType = NEGATIVE_INFINITY_TYPE;
		} else if (value == Double.POSITIVE_INFINITY) {
			numberType = POSITIVE_INFINITY_TYPE;
		} else if (value == Double.NaN) {
			numberType = NOT_A_NUMBER_TYPE;
		} else {
			numberType = FINITE_TYPE;
		}
	}

	public MercadoBigDecimal(BigInteger bigInteger) {
		this(bigInteger.toString());
	}

	private MercadoBigDecimal(BigDecimal bigDecimal, short numberType) {
		this(bigDecimal.toString(), numberType);
	}

	public MercadoBigDecimal(BigDecimal bigDecimal) {
		super(bigDecimal.toString());
	}

	public MercadoBigDecimal(Long value) {
		super(value.toString());
	}

	public MercadoBigDecimal divide(MercadoBigDecimal divisor) {
		return new MercadoBigDecimal(super.divide(divisor, 2*DEFAULT_SCALE, DEFAULT_ROUNDING_MODE), this.numberType);
	}

	public MercadoBigDecimal multiply(MercadoBigDecimal multiplicand) {
		return new MercadoBigDecimal(super.multiply(multiplicand), this.numberType).setDefaultScale();
	}

	public MercadoBigDecimal add(MercadoBigDecimal augend) {
		return new MercadoBigDecimal(super.add(augend), this.numberType).setDefaultScale();
	}

	public MercadoBigDecimal subtract(MercadoBigDecimal subtrahend) {
		return new MercadoBigDecimal(super.subtract(subtrahend), this.numberType).setDefaultScale();
	}

	private MercadoBigDecimal setDefaultScale() {
		return new MercadoBigDecimal(super.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), this.numberType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + numberType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		MercadoBigDecimal other = (MercadoBigDecimal) obj;
		if (numberType != other.numberType)
			return false;
		if (!super.equals(obj))
			return false;
		return true;
	}

	@Override
	public String toString() {
		switch (numberType) {
		case POSITIVE_INFINITY_TYPE:
			return new Double(Double.POSITIVE_INFINITY).toString();
		case NEGATIVE_INFINITY_TYPE:
			return new Double(Double.NEGATIVE_INFINITY).toString();
		case NOT_A_NUMBER_TYPE:
			return new Double(Double.NaN).toString();
		default:
			return super.toString();
		}
	}

	public int compareTo(MercadoBigDecimal val) {
		switch (numberType) {
		case POSITIVE_INFINITY_TYPE:
			return (val.numberType == POSITIVE_INFINITY_TYPE ? 0 : 1);
		case NEGATIVE_INFINITY_TYPE:
			return (val.numberType == NEGATIVE_INFINITY_TYPE ? 0 : -1);
		case NOT_A_NUMBER_TYPE:
			return (val.numberType == NOT_A_NUMBER_TYPE ? 0 : -1);
		default:
			switch (val.numberType) {
			case POSITIVE_INFINITY_TYPE:
				return -1;
			case NEGATIVE_INFINITY_TYPE:
				return 1;
			case NOT_A_NUMBER_TYPE:
				return 1;
			default:
				return super.compareTo(val);
			}
		}
	}
	
	@Override
	public MercadoBigDecimal setScale(int newScale) {
		return new MercadoBigDecimal(super.setScale(newScale, MercadoBigDecimal.DEFAULT_ROUNDING_MODE));
	}
}
