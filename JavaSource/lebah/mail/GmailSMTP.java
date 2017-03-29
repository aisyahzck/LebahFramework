package lebah.mail;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GmailSMTP {
	
	/*
	public static void sendMail(String mailTo, String mailSubject, String mailText) throws Exception {
		sendMail("lebahemail@gmail.com", mailTo, mailSubject, mailText, null);
	}

	public static void sendMail(String mailFrom, String mailTo, String mailSubject, String mailText) throws Exception {
		String username = "lebahemail@gmail.com";
		String password = "shamsulbahrin543";
 		sendEmail(username, password, mailFrom, mailTo, mailSubject, mailText, null);
	}
	*/
	
	
 
	/*
	public static void sendMail(String mailTo, String mailSubject, String mailText, List<String> recepients) throws Exception {
		sendMail("lebahemail@gmail.com", mailTo, mailSubject, mailText, recepients);
	}
	
	public static void sendMail(String mailFrom, String mailTo, String mailSubject, String mailText, List<String> recepients) throws Exception {
		String username = "lebahemail@gmail.com";
		String password = "shamsulbahrin543";
 		sendEmail(username, password, mailFrom, mailTo, mailSubject, mailText, recepients);
	}
	*/
	
	public static void sendEmail(String username, String password,
			String mailTo, String mailSubject, String mailText)
			throws Exception {
		sendEmail(username, password, username, mailTo, mailSubject, mailText, null);
	}	
	
	public static void sendEmail(String username, String password,
			String mailTo, String mailSubject, String mailText, List<String> recepients)
			throws Exception {
		sendEmail(username, password, username, mailTo, mailSubject, mailText, recepients);
	}
	
	/**
	 * 
	 * @param username
	 * @param password Password must be encrypted using DESEncryption with the lebah.mail.EncryptModule
	 * @param mailFrom
	 * @param mailTo
	 * @param mailSubject
	 * @param mailText
	 * @param recepients
	 * @throws MessagingException
	 */

	public static void sendEmail(final String username, final String password,
			String mailFrom, String mailTo, String mailSubject, String mailText, List<String> recepients)
			throws Exception {
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587"); //465
		
		//decrypt the password
		DESEncryption myEncryptor= new DESEncryption();
		final String decrypted = myEncryptor.decrypt(password);
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, decrypted);
			}
		  });
 
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mailFrom));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
		
		if ( recepients != null ) {
			for ( String add : recepients ) {
		         message.addRecipient(Message.RecipientType.CC, new InternetAddress(add));
			}
		}
		
		message.setSubject(mailSubject);
		message.setContent(mailText, "text/html" );
		Transport.send(message);
	}
	
	
	public static void sendEmail2(String mailFrom, String mailTo, String mailSubject, String mailText, List<String> recepients)
			throws Exception {
		
		Properties props = new Properties();
		//props.put("mail.smtp.auth", "true");
		//props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "211.25.200.126");
		props.put("mail.smtp.port", "25");
		
		//decrypt the password
		//DESEncryption myEncryptor= new DESEncryption();
		//final String decrypted = myEncryptor.decrypt(password);
 
		/*
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, decrypted);
			}
		  });
		*/
		Session session = Session.getInstance(props);
 	
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mailFrom));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
		
		if ( recepients != null ) {
			for ( String add : recepients ) {
		         message.addRecipient(Message.RecipientType.CC, new InternetAddress(add));
			}
		}
		
		message.setSubject(mailSubject);
		message.setContent(mailText, "text/html" );
		Transport.send(message);
	}
	
	public static void main(String[] args) throws Exception {
		//GmailSMTP.sendEmail("sbahrin3@gmail.com", "sbahrin3@gmail.com", "Test", "Test", null);
	}
	
}