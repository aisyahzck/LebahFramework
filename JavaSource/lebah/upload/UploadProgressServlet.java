package lebah.upload;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import lebah.servlets.IServlet;

/**
 * @author Shamsul Bahrin Abd Mutalb
 * @version 1.0
 */
public class UploadProgressServlet implements IServlet {
	
	UploadProgress progress;
	
    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	HttpSession session = request.getSession();
    	String sessionId = session.getId();
    	progress = (UploadProgress) session.getAttribute("upload_progress_" + sessionId);
   	
		String userid = (String) session.getAttribute("_portal_login");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");

        if ( "getAboutMe".equals(action)) {
        	out.print(progress.getAboutMe());
        }
        else if ( "init".equals(action)) {
        	//System.out.println("Instance of UploadProgress created.");
    		progress = new UploadProgress(request, response);
    		session.setAttribute("upload_progress_" + sessionId, progress);
        	out.print("");
        }
        else if ( "getCurrentBytes".equals(action)) {
        	out.print(progress.getCurrentBytes());
        }
        else if ( "getTotalBytes".equals(action)) {
        	out.print(progress.getTotalBytes());
        }
        else if ( "getStatus".equals(action)) {
        	/*
        	if ( "finished".equals(progress.getStatus())) {
        		progress = null;
        		session.setAttribute(sessionId, progress);
        	}tl
        	*/
        	//System.out.println("Status now = " + progress.getStatus());
       		out.print(progress.getStatus());

        }

        
    }


}
