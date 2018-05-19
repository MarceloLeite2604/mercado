package org.marceloleite.mercado.strategies.sixth.graphic;

import java.io.File;

import org.marceloleite.mercado.email.EmailMessage;
import org.marceloleite.mercado.utils.EmailUtils;

public class SixthStrategyGraphicSender implements Runnable {

	private static final String EMAIL_TITLE = "Daily Graphic";

	private SixthStrategyGraphic sixthStrategyGraphic;

	private String emailAddress;

	public SixthStrategyGraphicSender(String emailAddress, SixthStrategyGraphic sixthStrategyGraphic) {
		this.emailAddress = emailAddress;
		this.sixthStrategyGraphic = sixthStrategyGraphic;
	}

	@Override
	public void run() {
		File graphicFile = sixthStrategyGraphic.createFile();
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.getToAddresses()
				.add(emailAddress);
		emailMessage.setSubject(EMAIL_TITLE);
		emailMessage.setMimeMultipart(EmailUtils.createGraphicMimeMultipart(graphicFile));
		emailMessage.send();
	}

}
