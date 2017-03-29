/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or





but WITHOUT ANY WARRANTY; without even the implied warranty of
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
 */
public class SentFolder implements Serializable {
	private List sent;
	public SentFolder() {
		sent = new ArrayList();
	}
	public void add(MsgInfo m) {
		sent.add(m);
	}
	
	public void add(Message m) throws MessagingException {
		sent.add(wrap(m));
	}
	
	public void delete(int n) {
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
	
	public Object[] get() {
		return sent.toArray();
	}	
	
	private MsgInfo wrap(Message msg) throws MessagingException {
		return new MsgInfo(msg);	
	}
	
}
