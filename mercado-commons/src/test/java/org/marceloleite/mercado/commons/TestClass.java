package org.marceloleite.mercado.commons;

public class TestClass {
	private String name;
	private Long longValue;
	private Double doubleValue;

	public TestClass(String name, Long longValue, Double doubleValue) {
		super();
		this.name = name;
		this.longValue = longValue;
		this.doubleValue = doubleValue;
	}

	public String getName() {
		return name;
	}

	public Long getLongValue() {
		return longValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}
}
