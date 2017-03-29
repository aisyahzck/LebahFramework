package lebah.msg;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.servlets.IServlet;

public class ChatServlet implements IServlet {
	
	private static Hashtable<String, List<Message>> chatMessages = new Hashtable<String, List<Message>>();
	//private List<Message> messages = new ArrayList<Message>();
	

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	response.setContentType("text/javascrip");
    	HttpSession session = request.getSession();
    	PrintWriter out = response.getWriter();
    	
    	String action = request.getParameter("action");
    	if ( "doMessage".equals(action)) doChatMessage(out, request);
    	else if ( "sendMessage".equals(action)) sendMessage(out, request);
    	else if ( "listUsers".equals(action)) listUsers(out, request);
    	 
    }
    

	private void listUsers(PrintWriter out, HttpServletRequest request) {
		
		List<HttpSession> userSessions = lebah.listeners.SessionListener.getUserSessions();
		JSON jusers = new JSON();
		int counter = 0;
		List<String> userList = new ArrayList<String>();
		for ( HttpSession userSession : userSessions ) {
			String islogin = (String) userSession.getAttribute("_portal_islogin");
			if ( islogin != null && "true".equals(islogin)) {
				String userId = (String) userSession.getAttribute("_portal_login");
				String userName = (String) userSession.getAttribute("_portal_username");
				userList.add(userName + "::" + userId);
			}
		}
		Collections.sort(userList);
		for ( String s : userList ) {
			String userId = s.substring(s.indexOf("::")+2);
			String userName = s.substring(0, s.indexOf("::"));
			counter++;
			JSON u = new JSON();
			u.add("userId",userId).add("userName", userName);
			jusers.add("users", u);
		}
		jusers.add("userCounter", Integer.toString(counter-1));
    	out.print(jusers.getJSONElements());
	}


	private synchronized void doChatMessage(PrintWriter out, HttpServletRequest request) {
		String user = request.getParameter("user");
		List<Message> myMsgs = new ArrayList<Message>();
		DateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		
		int counter = 0;
		JSON json = new JSON();
		List<Message> messages = chatMessages.get(user);
		if ( messages != null ) {
			for ( Message msg : messages ) {
				//if ( msg.getTo().equals(user)) {
					myMsgs.add(msg);
				//}
			}
			
			
			for ( Message msg : myMsgs ) {
				counter++;
				messages.remove(msg);
				JSON m = new JSON();
				m.add("from", msg.getFrom()).add("fromName", msg.getFromName()).add("date-time", f.format(msg.getDate())).add("text", msg.getText());
				json.add("messages", m);
			}
		}
		
		json.add("messageCounter", Integer.toString(counter));
    	out.print(json.getJSONElements());
	}
	
	private void sendMessage(PrintWriter out, HttpServletRequest request) {
		String from = request.getParameter("from");
		String fromName = request.getParameter("fromName");
		String to = request.getParameter("to");
		String txt = request.getParameter("txt");

		putMessage(from, fromName, to, txt);
		//putMessage(from, fromName, from, txt);
		
		
		
	}


	private void putMessage(String from, String name, String to, String txt) {
		List<Message> messages = chatMessages.get(to);
		if ( messages == null ) {
			messages = new ArrayList<Message>();
			chatMessages.put(to, messages);
		}
		Message msg = new Message(new Date(), from, name, to, txt);
		messages.add(msg);
	}
	

}
