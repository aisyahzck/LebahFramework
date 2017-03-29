/**
 * 
 */
package lebah.listeners;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 */
public class LoginLogger {
	
	static {
		try {
			String workingDir = System.getProperty("user.dir");
			FileOutputStream os = new FileOutputStream(workingDir + "/lebah.login." + new SimpleDateFormat("ddMMyyyy").format(new Date()) + ".log");
			System.setErr(new PrintStream(os));
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public static void log(HttpServletRequest req, String userName, boolean success) {
		
		try {
			String uri = req.getRequestURI();
			String s1 = uri.substring(1);
			String appname =  s1.substring(0, s1.indexOf("/"));
			
			String remoteAddr = appname + ", " + req.getRemoteAddr();
			String logDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new Date());
	        
			System.err.println(logDate + ": " + remoteAddr + ", " + userName + ", " + success);
		
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}
