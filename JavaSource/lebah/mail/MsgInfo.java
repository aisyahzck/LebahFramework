/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.mail;


import java.io.*;
import java.util.*;
import java.text.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.sun.mail.pop3.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 */
public class MsgInfo implements Serializable{
	private Date date;
	private int msgNum;
	private String from;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String contentType;
	private String flag;
	private int size;
	private Message message;
	
	public MsgInfo(Message msg) throws MessagingException {
		message = msg;
		getInfo(message);
	}
	
	public MsgInfo(Date date, int msgNum, String from, String to, String subject, String contentType, String flag, int size) {
		this.date = date;
		this.msgNum = msgNum;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.contentType = contentType;
		this.flag = flag;		
		this.size = size;
	}
	
	public Message getMessage() { return message; }
	public Date getUDate() { return date; }
	public String getDate() { 
		SimpleDateFormat df = new SimpleDateFormat("EE M/d/yy");
		return df.format(date);
	}
	public int getMsgNum() { return msgNum; }
	public int getMessageNumber() { return msgNum; }
	public String getFrom() { return from; }
	public String getTo() { return to; }
	public String getCc() { return cc; }
	public String getBcc() { return bcc; }
	public String getSubject() { return subject; }
	public String getContentType() { return contentType; }
	public String getFlag() { return flag; }
	public boolean hasAttachment() {
		if ( contentType.indexOf("multipart/mixed") > - 1) return true;
		else return false;
	}
	public int getSize() { return size; }
	
	private void getInfo(Message msg) throws MessagingException {
		
		if ( msg instanceof POP3Message ) {
			msg = (POP3Message) msg;	
		}
		
		msgNum = msg.getMessageNumber();
		date = msg.getSentDate();
		
		Address[] a;
        
		from = "";
		if ((a = msg.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				from += a[j].toString();
		}
		to = "";
        //Illegal semicolon, not in group in string 
		/*
		if ((a = msg.getRecipients(Message.RecipientType.TO)) != null) {
			to += a[0].toString();
			for (int j = 1; j < a.length; j++)
			to += ", " + a[j].toString();
		}
		*/
		cc = "";
        /*
		if ((a = msg.getRecipients(Message.RecipientType.CC)) != null) {
			cc += a[0].toString();
			for (int j = 1; j < a.length; j++)
			cc += ", " + a[j].toString();
		}
        */	
		bcc = "";
        /*
		if ((a = msg.getRecipients(Message.RecipientType.BCC)) != null) {
			bcc += a[0].toString();
			for (int j = 1; j < a.length; j++)
			bcc += ", " + a[j].toString();
		}			
		*/
		subject = msg.getSubject();
		contentType = msg.getContentType();
		
		size = msg.getSize();
		

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
		flag = sb.toString();
	}
	
	public static class DateComparator extends lebah.util.MyComparator {
		public int compare(Object o1, Object o2) {
			return ((MsgInfo) o2).getUDate().compareTo(((MsgInfo) o1).getUDate());
		}
	}
	
}	
