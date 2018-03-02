package org.marceloleite.mercado.data;

public class VariableData {

	private ClassData classData;

	private String name;

	private String value;

	public String getName() {
		return name;
	}

	public ClassData getClassData() {
		return classData;
	}

	public void setClassData(ClassData classData) {
		this.classData = classData;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
