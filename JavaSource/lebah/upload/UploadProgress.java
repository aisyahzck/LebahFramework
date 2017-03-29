package lebah.upload;

import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.0
 */
public class UploadProgress {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private String status;
	
	private long totalBytes;
	private long currentBytes;
	
	public UploadProgress(HttpServletRequest req, HttpServletResponse res) {
		request = req;
		response = res;
		session = req.getSession();
	}
	
	public String getAboutMe() {
		return "I am Upload Progress"; 
	}

	public void setTotalBytes(long bytes) {
		totalBytes = bytes;
	}
	
	public void setCurrentBytes(long bytes) {
		currentBytes = bytes;
	}
	
	public long getTotalBytes() {
		return totalBytes;
	}
	
	public long getCurrentBytes() {
		return currentBytes;
	}
	
	public String getStatus() {
		String per = NumberFormat.getPercentInstance().format((double) currentBytes / (double) totalBytes);
		String pct = per.substring(0, per.length() - 1);
		if ( "finished".equals(status) || "100".equals(pct)) {
			return "finished";
		}
		else if ( "done".equals(status)) {
			return "done";
		}
		return pct;
	}
	
	public void setStatus(String s) {
		status = s;
	}
	
	public void finished() {
		status = "finished";
	}

}
