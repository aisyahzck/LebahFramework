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

package lebah.util;

import lebah.portal.velocity.*;

import java.util.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 */
public class LogActivity {
	
	static Vector<LogActivity> logs = new Vector<LogActivity>();
	private String user;
	private String remarks;
	
	private LogActivity(String user) {
		this.user = user;
	}
	
	private static LogActivity getLogInstance(String user) {
		LogActivity log = new LogActivity(user);
		int i = logs.indexOf(log);
		if ( i > -1 ) {
			log = logs.get(i);
		}
		else {
			logs.addElement(log);
		}
		return log;
	}	
	
	public static void log(VTemplate module, String submit, String user) throws Exception {
		getLogInstance(user).saveLog(module, submit);
	}
	
	public static void setRemarks(String user, String remarks) {
		getLogInstance(user).setRemarks(remarks);
	}
	
	private void saveLog(VTemplate module, String submit) {
		String name = module.getClass().getName();
		//System.out.println(name + ", " + submit + ", " + user + ", " + remarks);		
	}
	
	private void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	


	private String getUser() {
		return user;
	}
	
	public boolean equals(Object o) {
		if ( o == null ) return false;
		LogActivity log = (LogActivity) o;
		return user != null ? user.equals(log.getUser()) : false;
	}

}
