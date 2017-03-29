package lebah.mail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.mail.MessagingException;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class MailerDaemon implements Runnable {
	
	private Thread thread;
	private String mailUser;
	private String mailPassword;
	private String host;
	private String port;
	private static MailerDaemon instance;
	Queue<Map<String, String>> q = new LinkedList<Map<String, String>>();
	
	private MailerDaemon() {

	}
	
	public static MailerDaemon getInstance() {
		if ( instance == null ) instance = new MailerDaemon();
		return instance;
	}

	@Override
	public void run() {
		boolean sent = false;
		while ( thread != null ) {
			try {
				Thread.sleep(5000);
				sent = false;
				if ( q.size() > 0 ) {
					Map<String, String> m = q.peek();
					try {
						System.out.println("MailerDaemon Sending Email to " + m.get("to"));
						sendEmail(m);
						sent = true;
						System.out.println("Successfully sent email to " + m.get("to"));
					} catch (Exception e) {
						System.out.println("MailerDaemon CAN'T SENT EMAIL to " + m.get("to"));
						e.printStackTrace();
					}
					q.poll(); //remove
					if ( sent ) {
						//q.poll();
						System.out.println("Que size left: " + q.size());
					}
				}
				
			} catch ( InterruptedException e ) {}
		}
	}
	
	public void start(String mailUser, String mailPassword) {
		this.mailUser = mailUser;
		this.mailPassword = mailPassword;
		thread = new Thread(this);
		thread.start();
	}
	
	public void start(String mailUser, String mailPassword, String host, String port) {
		this.mailUser = mailUser;
		this.mailPassword = mailPassword;
		this.host = host;
		this.port = port;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		thread = null;
	}
	
	public synchronized void addMessage(String to, String subject, String text) {
		Map<String, String> m = new HashMap<String, String>();
		m.put("to", to);
		m.put("subject", subject);
		m.put("text", text);
		q.add(m);
	}
	
	private void sendEmail(Map<String, String> m) throws Exception {
		String mailTo = m.get("to");
		String mailSubject = m.get("subject");
		String mailText = m.get("text");
		lebah.mail.GmailSMTP.sendEmail(mailUser, mailPassword, mailTo, mailSubject, mailText);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	

}
