package org.marceloleite.mercado.negotiationapi.model.getwithdrawal;

public enum WithdrawalStatus {

	OPEN(1),
	DONE(2),
	CANCELLED(3);

	private long value;

	private WithdrawalStatus(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}
	
	public static WithdrawalStatus getByValue(long value) {
		for (WithdrawalStatus withdrawalStatus : values()) {
			if ( withdrawalStatus.getValue() == value) {
				return withdrawalStatus;
			}
		}
		throw new IllegalArgumentException("Could not find a withdrawal status with value " + value + ".");
	}
}
