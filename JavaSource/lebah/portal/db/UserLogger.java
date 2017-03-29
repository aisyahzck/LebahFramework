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
package lebah.portal.db;

import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import lebah.db.Db;
import lebah.db.SQLRenderer;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class UserLogger {

	public static void save(HttpServletRequest req, String username) {
		String remoteAddr = req.getRemoteAddr();
		java.util.Calendar cal = new java.util.GregorianCalendar();
        int year = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH) + 1;
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        //int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);	
        int hour12 = cal.get(java.util.Calendar.HOUR);
        //int hour24 = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int min = cal.get(java.util.Calendar.MINUTE);        
        int sec = cal.get(java.util.Calendar.SECOND);        
        String ampm = cal.get(java.util.Calendar.AM_PM) == 0 ? "AM" : "PM";         	
        
        String logString = "[" + remoteAddr + "] - " + year + "/" + month + "/" + day + " " + hour12 + ":" + min + " " + ampm;
        logString += " " + username;
		
		Db db = null;
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("remote_add", remoteAddr);
			r.add("log_string", logString);
			r.add("user_name", username);
			r.add("log_year", year);
			r.add("log_month", month);
			r.add("log_day", day);
            r.add("log_date", r.unquote("now()"));
			String sql = r.getSQLInsert("web_logger");
			stmt.executeUpdate(sql);
		} catch ( Exception ex ) {
			System.out.println("WEB LOGGER EXCEPTION: " + logString);
		} finally {
			if ( db != null ) db.close();
		}
			
	}	
	
}
