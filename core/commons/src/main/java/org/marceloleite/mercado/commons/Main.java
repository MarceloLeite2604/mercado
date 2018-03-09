package org.marceloleite.mercado.commons;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.DurationToStringConverter;
import org.marceloleite.mercado.commons.encryption.Encrypt;

public class Main {

	public static void main(String[] args) {
		// daylightSavingTime();
		// durationToStringConverter();
		encrypt();
		// createEncryptKey();
	}

	@SuppressWarnings("unused")
	private static void daylightSavingTime() {
		ZonedDateTime start = ZonedDateTime.of(2017, 10, 14, 23, 0, 0, 0, ZoneOffset.UTC);
		ZonedDateTime end = ZonedDateTime.of(2017, 10, 15, 03, 0, 0, 0, ZoneOffset.UTC);
		TimeDivisionController timeDivisionController = new TimeDivisionController(new TimeInterval(start, end), Duration.ofHours(1l));
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			System.out.println(timeInterval);	
		}
		
	}

	@SuppressWarnings("unused")
	private static void durationToStringConverter() {
		Duration duration = Duration.ofDays(10l);
		duration = duration.minus(Duration.ofHours(7l));
		duration = duration.minus(Duration.ofMinutes(93l));
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		System.out.println(durationToStringConverter.convertTo(duration));
	}
	
	@SuppressWarnings("unused")
	private static void encrypt() {
		String string = "This message will be encrypted.";
		Encrypt encrypt = new Encrypt();
		String encryptedString = encrypt.encrypt(string);
		System.out.println(encryptedString);
		string = encrypt.decrypt(encryptedString);
		System.out.println(string);
	}
	
	@SuppressWarnings("unused")
	private static void createEncryptKey() {
		System.out.println(new Encrypt().generateKey());
	}

	
}
