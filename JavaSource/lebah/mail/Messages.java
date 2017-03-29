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

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.pop3.POP3Folder;


public class Messages {
    
    String username, password, host, protocol;
    Folder inboxFolder;
    
   
    public Messages(String un, String pw, String hs, String pr) {
        username = un;
        password = pw;
        host = hs;
        protocol = pr;
    }
    
    public Folder getInboxFolder() {
    	return inboxFolder;
    }
    
    public Object[] getMessages() throws Exception {
        return getMessages(username, password, host, protocol);
    }

    public Object[] getMessages(String username, String password, String host, String protocol) throws Exception {
        
        Store store = null;        
        try {
        	System.out.println("authenticating mail...");
            User user = new User(username, password, host, protocol);
            String url = user.getUrl();
            //try connect to email server
            Properties props = System.getProperties();
            Session msgSession = Session.getDefaultInstance(props, null);
            URLName urln = new URLName(url);
            store = msgSession.getStore(urln);
            System.out.println("connecting....");
            store.connect();  //may throw AuthenticationFailedException
            MsgFolder msgFolder = getMessageFolder(store);
            Object[] msgInfos = msgInfos = msgFolder.getInbox();
            return msgInfos;
        } catch ( javax.mail.AuthenticationFailedException e ) {
            throw new Exception("Authentication Failed!");  
        } finally {
            if ( store != null ) store.close();
        }
    }
    
    MsgFolder getMessageFolder(Store store) {
        try {
            //  open the folder
            inboxFolder = store.getDefaultFolder();
            inboxFolder = inboxFolder.getFolder("INBOX");
            
            // try to open read/write and if that fails try read-only
            try {
                inboxFolder.open(Folder.READ_WRITE);
            } catch (MessagingException ex) {
                inboxFolder.open(Folder.READ_ONLY);
            }
            if ( inboxFolder instanceof POP3Folder ) {
                inboxFolder = (POP3Folder) inboxFolder;
            }
            
            //int totalMessages = folder.getMessageCount();
            //get messages from this folder
            Message[] msgs = inboxFolder.getMessages();
            //create new msgFolder object
            MsgFolder msgFolder = new MsgFolder();
            for ( int i = 0; i < msgs.length; i++ ) {
                int msgNumber = msgs[i].getMessageNumber();
                Message msg = inboxFolder.getMessage(msgNumber);
                msgFolder.addInbox(msg);
            }
            return msgFolder;
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }


}
