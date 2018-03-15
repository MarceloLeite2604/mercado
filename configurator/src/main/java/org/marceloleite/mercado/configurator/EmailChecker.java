package org.marceloleite.mercado.configurator;

import java.util.regex.Pattern;

import org.marceloleite.mercado.commons.interfaces.Checker;

public class EmailChecker implements Checker<String> {

	private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

	private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

	@Override
	public boolean check(String string) {
		boolean result = EMAIL_REGEX_PATTERN.matcher(string).find();
		if (!result) {
			System.out.println("The content informed does not appear to be an email address.");
		}
		return result;
	}

}
