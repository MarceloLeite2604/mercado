package org.marceloleite.mercado.api.negotiation.methods.getwithdrawal.response;

public enum WithdrawalStatus {

	OPEN(1L),
	DONE(2L),
	CANCELLED(3L);

	private Long value;

	private WithdrawalStatus(Long value) {
		this.value = value;
	}

	public Long getValue() {
		return value;
	}
	
	public static WithdrawalStatus getByValue(Long value) {
		for (WithdrawalStatus withdrawalStatus : values()) {
			if ( withdrawalStatus.getValue() == value) {
				return withdrawalStatus;
			}
		}
		throw new IllegalArgumentException("Could not find a withdrawal status with value " + value + ".");
	}
}
