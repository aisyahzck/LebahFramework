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

package lebah.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lebah.db.Db;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public final class SessionListener implements HttpSessionListener {
	private static long session_counter = 0;
	private static List<HttpSession> sessions = new ArrayList<HttpSession>();
	
	public SessionListener() {} 

	//Notification that a session was created. 
	public void sessionCreated(HttpSessionEvent event) {
		/*
		Object source = event.getSource();	
		String sourceClassName = source.getClass().getName();
		
		System.out.println(sourceClassName);
		*/
		
		session_counter++;
		HttpSession session = event.getSession();
		sessions.add(session);
		System.out.println("Session: " + session.getId() + " created.");

	}
          
	//Notification that a session was invalidated. 
	public void sessionDestroyed(HttpSessionEvent event) {
		
		//Object source = event.getSource();
		session_counter--;
		HttpSession session = event.getSession();
		String sessionId = session.getId();
		//remove from user_module
		Db db = null;
		try {
			db = new Db();
			String sql = "delete from user_module where user_login = '" + sessionId + "'";
			db.getStatement().executeUpdate(sql);
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		sessions.remove(session);
		lebah.portal.ClassLoadManager.clearCache(session.getId());

		System.out.println("Session: " + session.getId() + " destroyed.");
	}
	
	public static long getNumberOfSession() { 
		return session_counter;
	}

	public static List<HttpSession> getUserSessions() {
		return sessions;
	}   
	
	
}
          
