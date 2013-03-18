package com.martindavey.lotto;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil {

	private String SMTP_HOST = "smtp.gmail.com";
	private String FROM_ADDRESS = "martinjohndavey@gmail.com";
	private String PASSWORD = "24931648";
	private String FROM_NAME = "Martin";

	public boolean sendMail(String[] recipients, String[] bccRecipients, String subject, String message, String htmlMessage) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", SMTP_HOST);
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "false");
			props.put("mail.smtp.ssl.enable", "true");

			Session session = Session.getInstance(props, new SocialAuth());
			Message msg = new MimeMessage(session);

			InternetAddress from = new InternetAddress(FROM_ADDRESS, FROM_NAME);
			msg.setFrom(from);

			InternetAddress[] toAddresses = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				toAddresses[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, toAddresses);

			InternetAddress[] bccAddresses = new InternetAddress[bccRecipients.length];
			for (int j = 0; j < bccRecipients.length; j++) {
				bccAddresses[j] = new InternetAddress(bccRecipients[j]);
			}
			msg.setRecipients(Message.RecipientType.BCC, bccAddresses);

			msg.setSubject(subject);

			final MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(message, "text/plain; charset=UTF-8");

			final MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlMessage, "text/html; charset=UTF-8");

			final Multipart mp = new MimeMultipart("alternative");
			mp.addBodyPart(textPart);
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);

			Transport.send(msg);
			return true;
		}
		catch (UnsupportedEncodingException ex) {
			Logger.getLogger(MailUtil.class.getName()).log(Level.SEVERE, null, ex);
			return false;

		}
		catch (MessagingException ex) {
			Logger.getLogger(MailUtil.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}

	class SocialAuth extends Authenticator {

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {

			return new PasswordAuthentication(FROM_ADDRESS, PASSWORD);

		}
	}
}