package org.marceloleite.mercado.commons;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.converter.DurationToStringConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.encryption.Encrypt;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class Main {

	public static void main(String[] args) {
		// daylightSavingTime();
		// durationToStringConverter();
		// encrypt();
		// createEncryptKey();
		// mercadoBigDecimal();
		alarm();
	}

	@SuppressWarnings("unused")
	private static void daylightSavingTime() {
		ZonedDateTime start = ZonedDateTime.of(2017, 10, 14, 23, 0, 0, 0, ZoneOffset.UTC);
		ZonedDateTime end = ZonedDateTime.of(2017, 10, 15, 03, 0, 0, 0, ZoneOffset.UTC);
		TimeDivisionController timeDivisionController = new TimeDivisionController(new TimeInterval(start, end),
				Duration.ofHours(1l));
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			System.out.println(timeInterval);
		}

	}

	@SuppressWarnings("unused")
	private static void durationToStringConverter() {
		Duration duration = Duration.ofDays(10l);
		duration = duration.minus(Duration.ofHours(7l));
		duration = duration.minus(Duration.ofMinutes(93l));
		System.out.println(DurationToStringConverter.getInstance().convertTo(duration));
	}

	@SuppressWarnings("unused")
	private static void encrypt() {
		String string = "This message will be encrypted.";
		String encryptedString = Encrypt.getInstance().encrypt(string);
		System.out.println(encryptedString);
		string = Encrypt.getInstance().decrypt("HQbtfCrbBsz1YcnbP/8dkUXogkD+qRqj");
		System.out.println(string);
	}

	@SuppressWarnings("unused")
	private static void createEncryptKey() {
		System.out.println(Encrypt.getInstance().generateKey());
	}

	@SuppressWarnings("unused")
	private static void mercadoBigDecimal() {
		MercadoBigDecimal unitPrice = new MercadoBigDecimal("3475.00000");
		MercadoBigDecimal balance = new MercadoBigDecimal("1000.00000");

		MercadoBigDecimal amount = balance.divide(unitPrice);
		System.out.println("Amount: " + amount);
		System.out.println("Balance: " + amount.multiply(unitPrice));

	}

	private static void alarm() {
		ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(2018, 01, 01, 07, 00),
				ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(2018, 01, 02, 07, 00),
				ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		Duration divisionDuration = Duration.ofMinutes(1);
		ZonedDateTime alarmTime = ZonedDateTime.of(LocalDateTime.of(2018, 01, 01, 10, 17),
				ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		ZonedDateTime disarmTime = ZonedDateTime.of(LocalDateTime.of(2018, 01, 01, 10, 20),
				ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, divisionDuration);
		Alarm alarm = new Alarm(alarmTime, 30);
		List<TimeInterval> timeIntervals = timeDivisionController.geTimeIntervals();

		String status;
		for (TimeInterval timeInterval : timeIntervals) {
			if (disarmTime.isEqual(timeInterval.getStart())) {
				System.out.println("Disarming.");
				alarm.disarm();
				alarmTime = ZonedDateTime.of(LocalDateTime.of(2018, 01, 01, 16, 00),
						ZonedDateTimeUtils.DEFAULT_ZONE_ID);
				alarm.setStartTime(alarmTime);
			}
			if (alarm.isRinging(timeInterval.getStart())) {
				status = "Riiiinng";
			} else {
				status = "Tick";
			}
			System.out.println(ZonedDateTimeToStringConverter.getInstance().convertTo(timeInterval.getStart()) + ": "
					+ status + ".");
		}
	}
}
