package it.vige.realtime.javamail;

import static java.lang.System.getProperties;
import static java.util.logging.Logger.getLogger;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Session.getDefaultInstance;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	private static final Logger logger = getLogger(SendMail.class.getName());

	private Properties mailServerProperties;
	private Session getMailSession;
	private MimeMessage generateMailMessage;

	public void completeClientSend() throws AddressException, MessagingException {
		// Step1
		logger.info("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		logger.info("Mail Server Properties have been setup successfully..");
		// Step2
		logger.info("\n\n 2nd ===> get Mail .");
		getMailSession = getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(TO, new InternetAddress("test1@crunchify.com"));
		generateMailMessage.addRecipient(CC, new InternetAddress("test2@crunchify.com"));
		generateMailMessage.setSubject("Greetings from Crunchify..");
		String emailBody = "Test email by Crunchify.com JavaMail API example. "
				+ "<br><br> Regards, <br>Crunchify Admin";
		generateMailMessage.setContent(emailBody, "text/html");
		logger.info("Mail Session has been created successfully..");
		// Step3
		logger.info("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "<----- Your GMAIL ID ----->", "<----- Your GMAIL PASSWORD ----->");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();

	}
}