package org.marceloleite.mercado.commons;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class OldMercadoBigDecimal extends BigDecimal {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_SCALE = 8;

	public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

	private static final short POSITIVE_INFINITY_TYPE = 1;
	private static final short FINITE_TYPE = 0;
	private static final short NEGATIVE_INFINITY_TYPE = -1;
	private static final short NOT_A_NUMBER_TYPE = -2;

	public static final OldMercadoBigDecimal POSITIVE_INFINITY = new OldMercadoBigDecimal(BigDecimal.ZERO,
			POSITIVE_INFINITY_TYPE);
	public static final OldMercadoBigDecimal NEGATIVE_INFINITY = new OldMercadoBigDecimal(BigDecimal.ZERO,
			NEGATIVE_INFINITY_TYPE);
	public static final OldMercadoBigDecimal NOT_A_NUMBER = new OldMercadoBigDecimal(BigDecimal.ZERO, NOT_A_NUMBER_TYPE);
	
	public static final OldMercadoBigDecimal ZERO = new OldMercadoBigDecimal(BigDecimal.ZERO);
	
	public static final OldMercadoBigDecimal ONE = new OldMercadoBigDecimal(BigDecimal.ONE);

	public short numberType;

	public OldMercadoBigDecimal() {
		super("0");
		this.numberType = FINITE_TYPE;
	}

	public OldMercadoBigDecimal(String string, short numberType) {
		super(string);
		this.numberType = FINITE_TYPE;
	}

	public OldMercadoBigDecimal(String string) {
		this(string, FINITE_TYPE);
	}

	public OldMercadoBigDecimal(Double value) {
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

	public OldMercadoBigDecimal(BigInteger bigInteger) {
		this(bigInteger.toString());
	}

	private OldMercadoBigDecimal(BigDecimal bigDecimal, short numberType) {
		this(bigDecimal.toString(), numberType);
	}

	public OldMercadoBigDecimal(BigDecimal bigDecimal) {
		super(bigDecimal.toString());
	}

	public OldMercadoBigDecimal(Long value) {
		super(value.toString());
	}

	public OldMercadoBigDecimal(int value) {
		super(Integer.toString(value));
	}

	public OldMercadoBigDecimal divide(OldMercadoBigDecimal divisor) {
		return new OldMercadoBigDecimal(super.divide(divisor, 2*DEFAULT_SCALE, DEFAULT_ROUNDING_MODE), this.numberType);
	}

	public OldMercadoBigDecimal multiply(OldMercadoBigDecimal multiplicand) {
		return new OldMercadoBigDecimal(super.multiply(multiplicand), this.numberType).setDefaultScale();
	}

	public OldMercadoBigDecimal add(OldMercadoBigDecimal augend) {
		return new OldMercadoBigDecimal(super.add(augend), this.numberType).setDefaultScale();
	}

	public OldMercadoBigDecimal subtract(OldMercadoBigDecimal subtrahend) {
		return new OldMercadoBigDecimal(super.subtract(subtrahend), this.numberType).setDefaultScale();
	}

	private OldMercadoBigDecimal setDefaultScale() {
		return new OldMercadoBigDecimal(super.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), this.numberType);
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
		OldMercadoBigDecimal other = (OldMercadoBigDecimal) obj;
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

	public int compareTo(OldMercadoBigDecimal val) {
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
	public OldMercadoBigDecimal setScale(int newScale) {
		return new OldMercadoBigDecimal(super.setScale(newScale, OldMercadoBigDecimal.DEFAULT_ROUNDING_MODE));
	}
}
