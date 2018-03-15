package org.marceloleite.mercado.encryptor;

import java.io.IOException;

import org.marceloleite.mercado.commons.Reader;
import org.marceloleite.mercado.commons.encryption.Encrypt;

public class Encryptor {

	public void encrypt() {
		String content;

		try {
			content = new Reader().read("Inform the content to encrypt: ");
		} catch (IOException exception) {
			throw new RuntimeException("Error while reading content to encrypt.", exception);
		}
		String key = System.getenv(Encrypt.KEY_ENVIRONMENT_VARIABLE_NAME);
		if (key == null || key.isEmpty()) {
			try {
				Reader reader = new Reader();
				reader.setMaxRetries(1);
				key = reader.read("Inform the encrypt key to use or leave it blank to generate a new one: ");
			} catch (IOException exception) {
				key = new Encrypt().generateKey();
				System.out.println("Encrypt key was not informed, using key \"" + key + "\".");

			}
		} else {
			System.out.println(
					"Using key stored on \"" + Encrypt.KEY_ENVIRONMENT_VARIABLE_NAME + "\" environment variable.");
		}
		String encryptedContent = new Encrypt().encrypt(content, key);
		System.out.println("Encrypted content is:");
		System.out.println(encryptedContent);
	}
}
