package org.marceloleite.mercado.commons;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import org.marceloleite.mercado.commons.interfaces.Checker;
import org.marceloleite.mercado.commons.interfaces.Parser;

public class Reader {

	private static final int DEFAULT_MAX_RETRIES = 3;

	private BufferedReader bufferedReader;

	private boolean hideInput;
	
	private int maxRetries;

	public Reader() {
		this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		this.maxRetries = DEFAULT_MAX_RETRIES;
	}

	public <T> T read(String inputMessage, Parser<T> parser, Checker<T> check) throws IOException {
		boolean valueRead = false;
		String value;
		T parsedValue = null;
		int retries = 0;
		try {
			while (!valueRead && retries < maxRetries) {
				System.out.print(inputMessage);
				value = readInput();
				if (value != null && !value.isEmpty()) {
					retries = 0;
					parsedValue = parser.parse(value);
					if (parsedValue != null) {
						valueRead = check.check(parsedValue);
					}
				} else {
					retries++;
				}
			}
		} catch (IOException exception) {
			throw new RuntimeException("Error while reading content.", exception);
		}

		if (retries >= DEFAULT_MAX_RETRIES) {
			throw new IOException("User did not enter a value.");
		}

		return parsedValue;
	}

	private String readInput() throws IOException {
		if (hideInput) {
			Console console = System.console();
			if (console != null) {
				return new String(console.readPassword());
			} else {
				return bufferedReader.readLine();
			}
		} else {
			return bufferedReader.readLine();
		}
	}

	public String read(String inputMessage) throws IOException {
		return read(inputMessage, (string) -> string, (string) -> true);
	}

	public String read(String inputMessage, Checker<String> checker) throws IOException {
		return read(inputMessage, (string) -> string, checker);
	}

	public void hideInput(boolean hideInput) {
		this.hideInput = hideInput;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}
}
