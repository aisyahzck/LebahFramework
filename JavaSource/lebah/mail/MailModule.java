/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin







but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.mail;

import java.util.Vector;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import java.io.*;
import javax.mail.internet.*;
import javax.mail.event.*;
import javax.mail.*;


import com.sun.mail.pop3.POP3Folder;


import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MailModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(session, submit);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    
    String prepareTemplate(HttpSession session, String submit) throws Exception {
        String template_name = "";
        if ( "listMessages".equals(submit)) {
            template_name = "vtl/mail/list.vm";
            String username = getParam("username");
            String password = getParam("password");
            String host = getParam("host");
            String protocol =  "pop3";
            try {
                Messages messages = new Messages(username, password, host, protocol);
                Object[] msgObjects = messages.getMessages();
                session.setAttribute("msgInfos", msgObjects);
                Vector messageList = getMessages(msgObjects);
                context.put("messageList", messageList);
                context.put("errorMessage", "");
                
                //
                Folder inboxFolder = messages.getInboxFolder();
                session.setAttribute("inboxFolder", inboxFolder);
                
            } catch ( Exception e ) {
                context.put("errorMessage", e.getMessage());
                context.put("messageList", new Vector());
            }
        }
        else if ( "readMessage".equals(submit)) {
        	template_name = "vtl/mail/read.vm";
        	readMessage(session);
        }
        else {
            template_name = "vtl/mail/login.vm";
        }
        return template_name;
    }
    


	Vector getMessages(Object[] msgInfos) throws Exception {
        int startno = 0;
        int msglength = 20;
        int i = 0, cnt = 0;
        Vector list = new Vector();
        for ( cnt = 0, i = startno; cnt < msglength && i < msgInfos.length; i++, cnt++ ) {
            list.addElement((MsgInfo) msgInfos[i]);
        }
        return list;
    }
    
    Vector getMessages(HttpSession session, int startno) throws Exception {
        Object[] msgInfos = (Object[]) session.getAttribute("msgInfos");
        int msglength = 20;
        int i = 0, cnt = 0;
        Vector list = new Vector();
        for ( cnt = 0, i = startno; cnt < msglength && i < msgInfos.length; i++, cnt++ ) {
            list.addElement((MsgInfo) msgInfos[i]);
        }
        return list;
    }
    
    String getBody(Message message) throws MessagingException, java.io.IOException {
    	Object content = message.getContent();
    	if (message.isMimeType("text/plain")) {
    		return getContent(content, "plain");
    	} else if (message.isMimeType("text/html")) {
    		return getContent(content, "html");
    	} else if (message.isMimeType("multipart/alternative")) {
    		Multipart mp = (Multipart)message.getContent();
    		int numParts = mp.getCount();
    		for (int i = 0; i < numParts; ++i) {
    			if (mp.getBodyPart(i).isMimeType("text/plain"))
    				return getContent(mp.getBodyPart(i).getContent(), "plain");
    			else if (mp.getBodyPart(i).isMimeType("text/html"))
    				return getContent(mp.getBodyPart(i).getContent(), "html");
    				
    		}
    		return "";   
    	} else if (message.isMimeType("multipart/*")) { 
       		Multipart mp = (Multipart)content;
          	if (mp.getBodyPart(0).isMimeType("text/plain"))
          		return getContent(mp.getBodyPart(0).getContent(), "plain");
          	else if (mp.getBodyPart(0).isMimeType("text/html"))
          		return getContent(mp.getBodyPart(0).getContent(), "html");	
          	else
          		return "";
       } else
       		return "";
    }

    String getContent(Object content, String type) {
    	String s = "";
    	if ( type.equals("plain") ) {
    		s = "<font face='courier' size='-1'>" + putLineBreak((String)content) + "</font>";
    	} else if ( type.equals("html") ) {
    		s = (String)content;
    	}
    	return s;	
    }

    String putLineBreak(String str) {
    	//first look for href
    	if ( str == null || str.equals("") ) return "";
    	str = Hyperlinker.convert(str, request);
    	StringBuffer txt = new StringBuffer(str);
    	char c = '\r';
    	while (txt.toString().indexOf(c) > -1) {
    		int pos = txt.toString().indexOf(c);
    		txt.replace(pos, pos + 1, "<br>");
    	}
    	return txt.toString();
    }

    boolean hasAttachments(Message message) throws java.io.IOException, 
                                            MessagingException {
    	boolean hasAttachments = false;
       	if (message.isMimeType("multipart/*")) {
       	Multipart mp = (Multipart)message.getContent();
       	if (mp.getCount() > 1)
          	hasAttachments = true;
         	}
    	return hasAttachments;
    }

    private FileInfo saveFile(Part p, String username) throws java.io.IOException, MessagingException {
    	
    	String filename = p.getFileName();

    	if ( filename ==  null ) filename = "attachment";
    	File dir = new File(Resource.getAttachmentDir() + "/" + username);
    	if ( !dir.exists() ) dir.mkdirs();
    	File f = new File(Resource.getAttachmentDir() + "/" + username + "/" + filename);
    	//if (f.exists()) throw new IOException("file exists");
    	OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
    	InputStream is = p.getInputStream();
    	int c;
    	while ((c = is.read()) != -1) {
    		os.write(c);
    	}
       	os.close();

      	//to determine the physical file size   	
      	f = new File(Resource.getAttachmentDir() + "/" + username + "/" + filename);
    	long size = f.length();
       	
    	FileInfo info = new FileInfo();
    	info.filename = filename;
    	info.size = size;
      	return info;
    }

    class FileInfo {
    	String filename;
    	long size;
    }
    
    private void readMessage(HttpSession session) throws Exception {
		
    	Folder folder = (Folder) session.getAttribute("inboxFolder");
    	int num = Integer.parseInt(getParam("msgNum"));
    	
    	//User user = (User) session.getAttribute("user");
    	String subject = "";
    	java.util.Date date = null;
    	String contentType = "";
    	int msgSize = 0;
    	String strflag = "";
    	String body = "";
    	String from = "";
    	String to = "";
    	String cc = "";
    	String bcc = "";
    	Vector files = new Vector();
    	try {
    		Message msg = folder.getMessage(num);
    		
    		subject = msg.getSubject();
    		date = msg.getSentDate();
    		msgSize = msg.getSize();
    		
    		context.put("message", msg);
    		
    		
    		contentType = msg.getContentType();
    		Flags flags = msg.getFlags();
    		Flags.Flag[] sf = flags.getSystemFlags();
    		StringBuffer sb = new StringBuffer();
    		
    		boolean first = true;
    		for (int k = 0; k < sf.length; k++) {
    			String s;
    		   Flags.Flag f = sf[k];
    		   if (f == Flags.Flag.ANSWERED)
    				s = "Answered";
    		   else if (f == Flags.Flag.DELETED)
    				s = "Deleted";
    		   else if (f == Flags.Flag.DRAFT)
    				s = "Draft";
    		   else if (f == Flags.Flag.FLAGGED)
    				s = "Flagged";
    		   else if (f == Flags.Flag.RECENT)
    				s = "Recent";
    		   else if (f == Flags.Flag.SEEN)
    				s = "Seen";
    		   else
    			continue;	// skip it
    		   if (first)
    				first = false;
    		   else
    				sb.append(' ');
    		    sb.append(s);
    		}	
    		strflag = sb.toString();	
    		
    		Address[] a;
    		// FROM 
    		
    		if ((a = msg.getFrom()) != null) {
    			for (int j = 0; j < a.length; j++)
    				from += a[j].toString();
    		}
    		context.put("addressFrom", from);
    		// TO
    		/*
    		if ((a = msg.getRecipients(Message.RecipientType.TO)) != null) {
    			to += a[0].toString();
    			for (int j = 1; j < a.length; j++)
    				to += ", " + a[j].toString();
    		}
    		*/		
    		
    		// CC
    		/*
    		if ((a = msg.getRecipients(Message.RecipientType.CC)) != null) {
    			cc += a[0].toString();
    			for (int j = 1; j < a.length; j++)
    				cc += ", " + a[j].toString();
    		}	
    		*/
    		// BCC
    		/*
    		if ((a = msg.getRecipients(Message.RecipientType.BCC)) != null) {
    			bcc += a[0].toString();
    			for (int j = 1; j < a.length; j++)
    				bcc += ", " + a[j].toString();
    		}		
    		*/
    		
    		body = getBody(msg);
    		context.put("messageBody", body);
    		
    		if ( hasAttachments(msg) ) {
    			Vector filenames = new Vector();
    			Multipart multipart = (Multipart) msg.getContent();
    			for (int i=0, n= multipart.getCount(); i<n; i++) {
    				Part part = multipart.getBodyPart(i);
    				String disposition = part.getDisposition();
    				if ( (disposition != null) && ( disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE) ) ) {
    					//FileInfo fileInfo = saveFile(part, user.getUsername());
    					//files.add(fileInfo);
    					//save filename into vector for forwarding purpose
    					//filenames.add(fileInfo.filename);
    			  }
    			}
    			//put this vector into session
    			//in case for forwarding message
    			session.setAttribute("attachment", filenames);
    		}

    
    		
    		/*

    	%>
    	<html>
    	<link rel="stylesheet" type="text/css" href="/easymail/common.css" />
    	<body bgcolor="#FFFFCC" topmargin="0">
    	<form name="f" method="post">
    	<table width="100%" cellpadding=1 cellspacing=1 border=0>
    	<tr><td class="bigtitle"><%=(String) session.getAttribute("appName")%></td><td class="module" align="right">Read message</td></tr>
    	</table>


    	<TABLE WIDTH="100%" CELLPADDING=2 CELLSPACING=2 BORDER=1 BGCOLOR="SILVER"><TR><TD>
    	<table width="100%" cellpadding=1 cellspacing=1 border=0 bgcolor="silver">
    	<tr><td><b><%=user.getEmail()%></b></td></tr></table>
    	</TD></TR></TABLE>


    	<table width="100%" cellpadding=1 cellspacing=1 border=0>
    	<tr><td width="80">From :</td><td bgcolor="white"><%=from%></td></tr>
    	<tr><td width="80">To :</td><td bgcolor="white"><%=to%></td></tr>
    	<tr><td width="80">Cc :</td><td bgcolor="white"><%=cc%></td></tr>
    	<tr><td width="80">Subject :</td><td bgcolor="white"><%=subject%></td></tr>
    	<tr><td width="80">Date :</td><td bgcolor="white"><%=date%></td></tr>	
    	<!--
    	<tr><td width="80" bgcolor="white">Content Type :</td><td bgcolor="white"><%=contentType%></td></tr>
    	<tr><td width="80" bgcolor="white">Size :</td><td bgcolor="white"><%=msgSize%>bytes</td></tr>
    	-->
    	<!--
    	<tr><td width="80" bgcolor="white">Flag :</td><td bgcolor="white"><%=strflag%></td></tr>
    	-->
    	</table>

    	<table width="100%" cellpadding=2 cellspacing=2 border=0 bgcolor="silver">
    	<tr><td>
    	<input type="button" value="Close" onclick="goMsg()">
    	<input type="button" value="Reply" onclick="sendmessage('reply')">
    	<input type="button" value="Reply All" onclick="sendmessage('replyall')">
    	<input type="button" value="Forward" onclick="sendmessage('forward')">
    	<input type="button" value="Delete" onclick="goDelete()">
    	<!--
    	<input type="button" value="Next" onclick="goNext()">
    	<input type="button" value="Previous" onclick="goPrevious()">
    	-->
    	</td></tr>
    	</table>

    	<table width="100%" cellpadding=1 cellspacing=1 border=0 bgcolor="white">
    	<tr><td><%=body%></td></tr></table>

    	<%if ( !files.isEmpty() ) {%>
    	<table cellpadding=1 cellspacing=1 border=0>
    	<tr><td><i>Attachment(s) : </i></td></tr>
    	<%
    	for (int i = 0; i < files.size(); i++) {
    		FileInfo info = (FileInfo) files.elementAt(i);
    	%>
    	<tr><td>
    	<%=i+1%> ) 
    	<%=info.filename%></td><td align="right">&nbsp;&nbsp;<%=info.size%> bytes
    	</td><td><input type="button" value="Get File" onclick="downloadfile('<%=info.filename%>')"></td></tr>
    	<%
    	}
    	%>
    	</table>
    	<%}%>
    	
		*/
    		
    	} finally {
    		
    	}
    	
    }



}
