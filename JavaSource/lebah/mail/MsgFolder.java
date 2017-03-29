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

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.sun.mail.pop3.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class MsgFolder implements Serializable {
	private List inbox;
	private List trash;
	private List sent;
	public MsgFolder() {
		inbox = new ArrayList();
		trash = new ArrayList();
		sent = new ArrayList();
	}
	public void addInbox(MsgInfo m) {
		inbox.add(m);
	}
	
	public void addInbox(Message m) throws MessagingException {
		inbox.add(wrap(m));
	}
	
	public void addTrash(MsgInfo m) {
		trash.add(m);
	}
	
	public void addTrash(Message m) throws MessagingException {
		trash.add(wrap(m));
	}
	
	public void addSent(MsgInfo m) {
		sent.add(m);
	}
	
	public void addSent(Message m) throws MessagingException {
		sent.add(wrap(m));
	}
	
	public void deleteFromInbox(int n) {
		deleteFrom(n, inbox);
	}
	
	public void deleteFromTrash(int n) {
		deleteFrom(n, trash);
	}
	
	public void deleteFromSent(int n) {
		deleteFrom(n, sent);
	}
	
	private void deleteFrom(int n, List list) {
		Iterator iter = list.iterator();
		int i = 0;
		while ( iter.hasNext() ) {
			MsgInfo m = (MsgInfo) iter.next();
			if ( m.getMessageNumber() == n ) {
				((ArrayList) list).remove(i);
				break;	
			}	
			i++;
		}
	}
	
	public Object[] getInbox() {
		Collections.sort(inbox, new MsgInfo.DateComparator());
		return inbox.toArray();
	}
	
	public Object[] getTrash() {
		Collections.sort(trash, new MsgInfo.DateComparator());
		return trash.toArray();
	}
	
	public Object[] getSent() {
		return sent.toArray();
	}	
	
	private MsgInfo wrap(Message msg) throws MessagingException {
		return new MsgInfo(msg);	
	}
	
	private MsgInfo convertToInfo(Message msg) throws MessagingException {
		
		int msgNumber = msg.getMessageNumber();
		java.util.Date date = msg.getSentDate();
		
		Address[] a;
		String from = "";
		if ((a = msg.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				from += a[j].toString();
		}
		String to = "";
		if ((a = msg.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
			to += a[j].toString();
		}		

		String subject = msg.getSubject();
		String contentType = msg.getContentType();
		
		int msgSize = msg.getSize();
		

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

		
		return new MsgInfo(date, msgNumber, from, to, subject, contentType, sb.toString(), msgSize);
	}		
		
	
}
