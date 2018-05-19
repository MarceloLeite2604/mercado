package org.marceloleite.mercado.strategies.sixth.graphic;

import java.io.File;

import org.marceloleite.mercado.email.EmailMessage;
import org.marceloleite.mercado.utils.EmailUtils;

public class SixthStrategyGraphicSender implements Runnable {

	private static final String DEFAULT_EMAIL_SUBJECT = "Daily Graphic";

	private SixthStrategyGraphic sixthStrategyGraphic;

	private String emailSubject;

	private String emailAddress;

	public SixthStrategyGraphicSender(String emailAddress, SixthStrategyGraphic sixthStrategyGraphic) {
		this.emailAddress = emailAddress;
		this.sixthStrategyGraphic = sixthStrategyGraphic;
		this.emailSubject = DEFAULT_EMAIL_SUBJECT;
	}

	@Override
	public void run() {
		sixthStrategyGraphic.addLimitPointsOnLastTimeAvailable();
		File graphicFile = sixthStrategyGraphic.createFile();
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.getToAddresses()
				.add(emailAddress);
		emailMessage.setSubject(emailSubject);
		emailMessage.setMimeMultipart(EmailUtils.createGraphicMimeMultipart(graphicFile));
		emailMessage.send();
		sixthStrategyGraphic.clearData();
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

}
