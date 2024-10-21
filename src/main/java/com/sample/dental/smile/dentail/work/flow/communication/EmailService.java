package com.sample.dental.smile.dentail.work.flow.communication;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

	public void sendOtpEmail(String recipientEmail, String otp) {
		final String senderEmail = "aravindgoudchedurupally@gmail.com"; // your email
		final String senderPassword = "caravind"; // your email password

		// Set up properties for the email session
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.example.com"); // specify your SMTP server
		props.put("mail.smtp.port", "587"); // or 465
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true"); // TLS

		// Create a session with an authenticator
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			// Create a new email message
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject("Your OTP Code");
			message.setText("Your OTP code is: " + otp);

			// Send the message
			Transport.send(message);
			System.out.println("OTP sent successfully to " + recipientEmail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
