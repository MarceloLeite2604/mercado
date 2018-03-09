package org.marceloleite.mercado.api.negotiation.methods.listsystemmessages;

public enum ListSystemMessagesParameters {
	LEVEL("level");
	
	private String name;

	private ListSystemMessagesParameters(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
