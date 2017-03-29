/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.portal.users;

import java.sql.Statement;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.portal.db.RegisterUser;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class RegisterModule extends lebah.portal.velocity.VTemplate  {
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		String template_name = "vtl/admin/user_register.vm";
		String submit = getParam("command");
		
		if ( !"".equals(submit)) {
		    String user_name = getParam("user_name");
		    String user_login = getParam("user_login");
		    String user_password = getParam("password");
		    String address = getParam("address");
		    String user_role = "user";
		    String page_style = "default.css";
		    Hashtable h = new Hashtable();
		    h.put("name", user_name);
		    h.put("login", user_login);
		    h.put("address", address);
		    context.put("userInfo", h);
		
	        if ( "add".equals(submit) ) {
	            template_name = "vtl/admin/user_register_done.vm";
	            if ( RegisterUser.add(user_name, user_login, user_password, user_role, page_style) ) {
	                context.put("success", new Boolean(true));
	                updateRegisterInfo(user_login);
	            }
	            else 
	            {
	                context.put("success", new Boolean(false));
	            }
	        } else if ("update".equals(submit) ) {
	            RegisterUser.update(user_name, user_login, user_password, user_role, user_role, "");
	            context.put("success", new Boolean(true));
	        }		
        
		}
		
		
		Template template = engine.getTemplate(template_name);	
		return template;		
	}
	
	/*
	user_login  		varchar  100      
	user_password  		varchar  100      
	user_name  		varchar  200      
	user_role  		varchar  100      
	user_login_alt  	varchar  50      
	user_address  		varchar  255      
	user_ip_address  	varchar  50   
	date_registered  datetime  19  	
	*/
	
	void updateRegisterInfo(String login_id) throws Exception {
	    Hashtable h = getLogInfo();
	    String address = getParam("address");
	    String ip_address = (String) h.get("remoteAddr");
	    String date = (String) h.get("date");
	    
	    Db db = null;
	    try {
	        db = new Db();
	        Statement stmt = db.getStatement();
	        SQLRenderer r = new SQLRenderer();
	        r.update("user_login", login_id);
	        r.add("user_address", address);
	        r.add("user_ip_address", ip_address);
	        r.add("date_registered", date);
	        String sql = r.getSQLUpdate("users");
	        stmt.executeUpdate(sql);
	    } finally {
	        if ( db != null ) db.close();
	    }
	    
	    
	}
	
    Hashtable getLogInfo() throws Exception {
		String remoteAddr = request.getRemoteAddr();
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
	    String fmtDate = year + "-" + fmt(month) + "-" + fmt(day);
	    
	    System.out.println("date = " + fmtDate);
	    
	    String h12 = hour12 < 10 ? "0" + hour12 : "" + hour12;
	    String h24 = hour24 < 10 ? "0" + hour24 : "" + hour24;
	    String m = min < 10 ? "0" + min : "" + min;
	    String s = sec < 10 ? "0" + sec : "" + sec;
	    String time12 = h12 + ":" + m + " " + ampm;
	    String time24 = h24 + "" + m;
	    
	    Hashtable h = new Hashtable();
	    h.put("remoteAddr", remoteAddr);
	    h.put("date", fmtDate);
	    
	    return h;
    }  
    
	public static String fmt(String s) {
		s = s.trim();
		if ( s.length() == 1 ) return "0".concat(s);
		else return s;	
	}
	
	public static String fmt(int i) {
	    String s = Integer.toString(i);
		s = s.trim();
		if ( s.length() == 1 ) return "0".concat(s);
		else return s;	
	}
	
	public static String fmt(Integer i) {
	    String s = Integer.toString(i.intValue());
		s = s.trim();
		if ( s.length() == 1 ) return "0".concat(s);
		else return s;	
	}    
    
    
}
