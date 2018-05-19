package org.marceloleite.mercado.commons;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.utils.BigDecimalUtils;
import org.marceloleite.mercado.commons.utils.DurationUtils;
import org.marceloleite.mercado.commons.utils.EncryptUtils;

public class Main {

	public static void main(String[] args) {
		// daylightSavingTime();
		// durationToStringConverter();
		// encrypt();
		// createEncryptKey();
		// mercadoBigDecimal();
		// alarm();
	}

	@SuppressWarnings("unused")
	private static void daylightSavingTime() {
		ZonedDateTime start = ZonedDateTime.of(2017, 10, 14, 23, 0, 0, 0, ZoneOffset.UTC);
		ZonedDateTime end = ZonedDateTime.of(2017, 10, 15, 03, 0, 0, 0, ZoneOffset.UTC);
		TimeDivisionController timeDivisionController = new TimeDivisionController(new TimeInterval(start, end),
				Duration.ofHours(1l));
		for (TimeInterval timeInterval : timeDivisionController.getTimeIntervals()) {
			System.out.println(timeInterval);
		}

	}

	@SuppressWarnings("unused")
	private static void durationToStringConverter() {
		Duration duration = Duration.ofDays(10l);
		duration = duration.minus(Duration.ofHours(7l));
		duration = duration.minus(Duration.ofMinutes(93l));
		System.out.println(DurationUtils.formatAsSpelledNumber(duration));
	}

	@SuppressWarnings("unused")
	private static void encrypt() {
		String string = "This message will be encrypted.";
		String encryptedString = EncryptUtils.encrypt(string);
		System.out.println(encryptedString);
		string = EncryptUtils.decrypt("HQbtfCrbBsz1YcnbP/8dkUXogkD+qRqj");
		System.out.println(string);
	}

	@SuppressWarnings("unused")
	private static void createEncryptKey() {
		System.out.println(EncryptUtils.generateKey());
	}

	@SuppressWarnings("unused")
	private static void mercadoBigDecimal() {
		BigDecimal unitPrice = new BigDecimal("3475.00000");
		BigDecimal balance = new BigDecimal("1000.00000");

		BigDecimal amount = balance.divide(unitPrice, BigDecimalUtils.DEFAULT_ROUNDING);
		System.out.println("Amount: " + amount);
		System.out.println("Balance: " + amount.multiply(unitPrice));

	}	
}
