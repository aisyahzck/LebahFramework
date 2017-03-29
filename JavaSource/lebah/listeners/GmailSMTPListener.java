package lebah.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lebah.mail.MailerDaemon;

public class GmailSMTPListener implements ServletContextListener, ServletContextAttributeListener {
	private static MailerDaemon mailer;

	public GmailSMTPListener() {}

	public void contextInitialized(ServletContextEvent event) {
		String mailUser = "";
		String mailPassword = "";
		ServletContext c = event.getServletContext();
		if (c != null) {
			mailUser = c.getInitParameter("mailUser");
			mailPassword = c.getInitParameter("mailPassword");
		}
		MailerDaemon.getInstance().start(mailUser, mailPassword);
	}
	
	public void contextDestroyed(ServletContextEvent event)  {
		MailerDaemon.getInstance().stop();
	}
	
	//Notification that a new attribute was added to the servlet context. 
	public void attributeAdded(ServletContextAttributeEvent event) {
	}
          
	//Notification that an existing attribute has been remved from the servlet context. 
	public void attributeRemoved(ServletContextAttributeEvent event) {
	}          
	
	//Notification that an attribute on the servlet context has been replaced. 
	public void attributeReplaced(ServletContextAttributeEvent event) {
	}


}
