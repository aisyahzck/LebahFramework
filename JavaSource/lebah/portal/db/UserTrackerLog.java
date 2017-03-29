/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import lebah.db.Db;
import lebah.db.SQLRenderer;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class UserTrackerLog {
	
	private static String fmt(String s) {
		s = s.trim();
		if ( s.length() == 1 ) return "0".concat(s);
		else return s;	
	}	
	
	//DISABLING USER TRACKER...23/11/2014
	public static void save(HttpServletRequest req, String login, String module) {
		/*
	    String moduleClass = "";
	    try {
	        moduleClass = CustomClass.getName(module);
	        if ( "mecca.app.UserLogModule".equals(moduleClass)) return;
	        if ( "mecca.app.UserLogStatModule".equals(moduleClass)) return;
	    } catch ( Exception e ) {
        
	    }	    
	    save2(req, login, module, moduleClass);
	    */
	}

	public static void save2(HttpServletRequest req, String login, String module, String moduleClass) {
	    

		String remoteAddr = req.getRemoteAddr();
		java.util.Calendar cal = new java.util.GregorianCalendar();
	    int year = cal.get(java.util.Calendar.YEAR);
	    int month = cal.get(java.util.Calendar.MONTH) + 1;
	    int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
	    //int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);	
	    int hour12 = cal.get(java.util.Calendar.HOUR);
	    int hour24 = cal.get(java.util.Calendar.HOUR_OF_DAY);
	    int min = cal.get(java.util.Calendar.MINUTE);        
	    int sec = cal.get(java.util.Calendar.SECOND);        
	    String ampm = cal.get(java.util.Calendar.AM_PM) == 0 ? "AM" : "PM";     
	    
	    String strDate = day + "/" + month + "/" + year;
	    String fmtDate = year + "-" + fmt(Integer.toString(month)) + "-" + fmt(Integer.toString(day));
	    
	    String h12 = hour12 < 10 ? "0" + hour12 : "" + hour12;
	    String h24 = hour24 < 10 ? "0" + hour24 : "" + hour24;
	    String m = min < 10 ? "0" + min : "" + min;
	    String s = sec < 10 ? "0" + sec : "" + sec;
	    
	    String time12 = h12 + ":" + m + " " + ampm;
	    String time24 = h24 + "" + m;
	    
		Db db = null;
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("remote_add", remoteAddr);
			r.add("user_login", login);
			r.add("log_year", year);
			r.add("log_month", month);
			r.add("log_day", day);
			r.add("time12", time12);
			r.add("time24", time24);
			r.add("str_date", strDate);
			r.add("log_date", r.unquote("now()"));
			r.add("module_id", module);
			r.add("module_class", moduleClass);
			String moduleName = getModuleName(module);
			r.add("module_name", !"".equals(moduleName) ? moduleName : module);
			
			String sql = r.getSQLInsert("user_tracker");
			//-
			stmt.executeUpdate(sql);
		} catch ( Exception ex ) {
			System.out.println("ERROR: " + ex.getMessage());
			//ex.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
			
	}	
	
	static String getModuleName(String id) throws Exception {
	    if ( "".equals(id)) return "";
	    String moduleName = "";
	    Db db = null;
	    try {
	        db = new Db();
	        Statement stmt = db.getStatement();
	        SQLRenderer r = new SQLRenderer();
	        r.add("module_title");
	        r.add("module_id", id);
	        String sql = r.getSQLSelect("module");
	        ResultSet rs = stmt.executeQuery(sql);
	        if ( rs.next() ) {
		        moduleName = rs.getString("module_title");
	        }

	    } finally {
	        if ( db != null ) db.close();
	    }
	    return moduleName;
	    
	}
	
	public static Vector getUserTrackerList(String user_login) throws Exception {
		return getUserTrackerList(user_login, 0, 0, 0);
	}
	
	public static Vector getUserTrackerList(String user_login, int year, int month, int day) throws Exception {
		return getUserTrackerList(user_login, year, month, day, 0, 0, 0);
	}	
	
	public static Vector getUserTrackerList(String user_login, 
											int year, int month, int day,
											int year2, int month2, int day2) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("user_login", user_login);
			if ( year > 0 && month > 0 && day > 0 && year2 > 0 && month2 > 0 && day2 > 0) {
				String fmtDate = year + "-" + fmt(Integer.toString(month)) + "-" + fmt(Integer.toString(day));
				String fmtDate2 = year2 + "-" + fmt(Integer.toString(month2)) + "-" + fmt(Integer.toString(day2));
				r.add("log_date", fmtDate, ">=");
				r.add("log_date", fmtDate2, "<=");			
			}
			else {
				if ( year > 0 ) r.add("log_year", year);
				if ( month > 0 ) r.add("log_month", month);
				if ( day > 0 ) r.add("log_day", day);	
			}
			r.add("remote_add");
			r.add("module_id");
			r.add("module_class");
			r.add("module_name");
			r.add("time12");
			r.add("time24");
			r.add("str_date");				
			sql = r.getSQLSelect("user_tracker", "log_year, log_month, log_day, time24");
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			while ( rs.next() ) {
				Hashtable h = new Hashtable();
				h.put("user_login", user_login);
				h.put("remote_add", rs.getString("remote_add"));
				h.put("module_id", rs.getString("module_id"));
				h.put("module_class", rs.getString("module_class"));
				h.put("module_name", rs.getString("module_name"));
				h.put("time12", rs.getString("time12"));
				h.put("time24", rs.getString("time24"));
				h.put("date", rs.getString("str_date"));	
				v.addElement(h);
			}
			return v;
		} catch ( Exception e ) {
		    e.printStackTrace();
		    throw e;
		} finally {
			if ( db != null ) db.close();
		}	
		
	}
	
	public static Vector getLogStatistic(String user_login, 
										    int year, int month, int day,
										    int year2, int month2, int day2) throws Exception {
												
		Vector logs = new Vector();
		Vector modules = getModuleList();
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			Hashtable module = null;
			for ( int i=0; i < modules.size(); i++ ) {
				module = (Hashtable) modules.elementAt(i);
				String module_id = (String) module.get("id");
				sql = "SELECT COUNT(module_id) AS cnt FROM user_tracker " +
				"WHERE module_id = '" + module_id + "' AND ";				
				if ( year > 0 && month > 0 && day > 0 && year2 > 0 && month2 > 0 && day2 > 0) {
					String fmtDate = year + "-" + fmt(Integer.toString(month)) + "-" + fmt(Integer.toString(day));
					String fmtDate2 = year2 + "-" + fmt(Integer.toString(month2)) + "-" + fmt(Integer.toString(day2));
					sql += " log_date >= '" + fmtDate + "' AND log_date <= '" + fmtDate2 + "'";
				}
				else {
					throw new Exception("Date input not complete!");
				}
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) {
					int cnt = rs.getInt("cnt");		
					if ( cnt > 0 ) {			
						Hashtable h = new Hashtable();
						h.put("module_id", module_id);
						h.put("module_name", (String) module.get("name"));
						h.put("count", new Integer(cnt));
						logs.addElement(h);
					}
				}
			}
			return logs;	
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	private static Vector getModuleList() throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("module_id");
			r.add("module_title");
			r.add("module_class");
			sql = r.getSQLSelect("module");
			ResultSet rs = stmt.executeQuery(sql);
			Vector list = new Vector();
			while ( rs.next() ) {
				Hashtable h = new Hashtable();
				h.put("id", rs.getString("module_id"));
				h.put("name", rs.getString("module_title"));
				h.put("class", rs.getString("module_class"));
				list.addElement(h);
			}
			return list;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	//This comparator will sort highest count first
	public static class CountComparator extends lebah.util.MyComparator {
		public int compare(Object o1, Object o2) {
			if ( o1 == null || o2 == null ) return 0;
			Hashtable h1 = (Hashtable) o1;
			Hashtable h2 = (Hashtable) o2;
			if ( h1.get("count") != null && h2.get("count") != null ) {;
				int cnt1 = ((Integer) h1.get("count")).intValue();
				int cnt2 = ((Integer) h2.get("count")).intValue();
				if ( cnt1 > cnt2 ) return -1;
				else return 1;
			} else
				return 0;
		}	
	}

	
}
