package org.marceloleite.mercado.retriever.email;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailMessage {

	private List<String> toAddresses;

	private List<String> ccAddresses;

	private List<String> bccAddresses;

	private String subject;

	private String content;

	private MimeMultipart mimeMultipart;

	public EmailMessage() {
		super();
		this.toAddresses = new ArrayList<>();
		this.ccAddresses = new ArrayList<>();
		this.bccAddresses = new ArrayList<>();
	}

	public String getSubject() {
		return subject;
	}

	public List<String> getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}

	public List<String> getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(List<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public List<String> getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(List<String> bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MimeMultipart getMimeMultipart() {
		return mimeMultipart;
	}

	public void setMimeMultipart(MimeMultipart mimeMultipart) {
		this.mimeMultipart = mimeMultipart;
	}

	private MimeMessage createMimeMessage() {
		MimeMessage mimeMessage = new MimeMessage(SessionCreator.createSession());

		Address[] toAddresses = createAddresses(this.toAddresses);
		Address[] ccAddresses = createAddresses(this.ccAddresses);
		Address[] bccAddresses = createAddresses(this.bccAddresses);

		try {
			mimeMessage.setFrom(new InternetAddress(UsernameRetriever.retrieveUsername()));
			mimeMessage.setRecipients(Message.RecipientType.TO, toAddresses);
			mimeMessage.setRecipients(Message.RecipientType.CC, ccAddresses);
			mimeMessage.setRecipients(Message.RecipientType.BCC, bccAddresses);
			mimeMessage.setSubject(subject);
			
			if (mimeMultipart != null) {
				mimeMessage.setContent(mimeMultipart);
			} else {
				mimeMessage.setText(content);
			}
			
		} catch (MessagingException exception) {
			throw new RuntimeException("Error while creating mime message for e-mail.", exception);
		}

		return mimeMessage;
	}

	private Address[] createAddresses(List<String> addressesList) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			if (addressesList != null) {
				addressesList.forEach(address -> stringBuilder.append(address + ","));
			}

			return InternetAddress.parse(stringBuilder.toString());
		} catch (AddressException exception) {
			throw new RuntimeException("Error while creating mail addresses.", exception);
		}
	}

	public void send() {
		MimeMessage mimeMessage = createMimeMessage();
		try {
			Transport.send(mimeMessage);
		} catch (MessagingException exception) {
			exception.printStackTrace();
		}
	}
}
