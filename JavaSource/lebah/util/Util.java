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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class Util {
	
	private static Util instance = null;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public Util() {
		
	}
	
	public static Util getInstance() {
		if ( instance == null ) instance = new Util();
		return instance;
	}
	
	
	public static void getServerRequestInfo(HttpServletRequest req, HttpSession session, VelocityContext context) {
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String reqUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString();
		//String http = reqUrl.substring(0, reqUrl.indexOf("://") + 3);
		String http = getHttp(req);
		String portalReqUrl = reqUrl + "?" + queryString;
		String s1 = req.getRequestURI().substring(1);
		String appname = s1.substring(0, s1.indexOf("/"));
        String server = serverPort != 80 ? serverName + ":" + serverPort : serverName;
        
		context.put("serverUrl", http + server + "/" + appname);
		context.put("appname", appname);
        context.put("server", serverPort != 80 ? serverName + ":" + serverPort : serverName);
		
		session.setAttribute("_portal_server", serverPort != 80 ? serverName + ":" + serverPort : serverName );
		session.setAttribute("_portal_appname", appname);
		session.setAttribute("_portal_reqUrl", portalReqUrl);
	}
	
	public static String getHttp(HttpServletRequest req) {
		return req.getRequestURL().toString().substring(0, req.getRequestURL().toString().indexOf("://") + 3);
	}
	
	public static String dateToString(Date date) {
		if ( date == null ) return "";
		return dateFormat.format(date);
	}
	
	public static Date stringToDate(String str) {
		try {
			return dateFormat.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String replace(String s, String charToReplace, String replaceWith) {
		String result = s.replaceAll(charToReplace, replaceWith);
		return result;
	}
	
	public static String replaceSp(String s) {
		String str = replace(s, " ", "_");
		return str;
	}
	
	public static int getNumber(String num) {
		if ( num == null || "".equals(num)) return 0;
		try {
			return Integer.parseInt(num);
		} catch ( Exception e ) {
			return 0;
		}
	}
	
    public static String putLineBreak(String str) {
        if (str != null) {
            StringBuffer txt = new StringBuffer(str);
            char c = '\n';
            while (txt.toString().indexOf(c) > -1) {
                int pos = txt.toString().indexOf(c);
                txt.replace(pos, pos + 1, "<br>");
            }
            return txt.toString();
        } else {
            return "";
        }
    }
    
    public static String getDateTime(Date date, String format) {
    	return new java.text.SimpleDateFormat (format).format(date);
    }
    
    public boolean isImageFile(String fileName) {
    	return isTypeOfFile(fileName, ".jpg .jpeg .gif .png .JPG .JPEG .GIF .PNG");
    }
    
    public boolean isHtmlFile(String fileName) {
    	return isTypeOfFile(fileName, ".htm .html .HTM .HTML");
    }
    
    public boolean isXMLFile(String fileName) {
    	return isTypeOfFile(fileName, ".xml .XML");
    }
    
    public boolean isFlashFile(String fileName) {
    	return isTypeOfFile(fileName, ".swf .SWF");
    }

	private boolean isTypeOfFile(String fileName, String allowedExt) {
        try {
            String ext = fileName.substring(fileName.lastIndexOf('.'));
        	if ( allowedExt.indexOf(ext) > -1 ) return true;
        	else return false;
        } catch ( Exception e ) {
            return false;
        }
	}
    
    public static DateValue getDateByTimeZone(String timeZoneId) {
        Calendar calendar = null;
        if ( timeZoneId != null && !"".equals(timeZoneId)) {
            calendar = new GregorianCalendar(TimeZone.getTimeZone(timeZoneId));
        }
        else {
            calendar = new GregorianCalendar();
            timeZoneId = calendar.getTimeZone().getID();
        }
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour12 = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        boolean am = calendar.get(Calendar.AM_PM) == Calendar.AM;
        DateValue d = new DateValue();
        d.setYear(year);
        d.setMonth(month);
        d.setDay(day);
        d.setMinutes(minutes);
        d.setAmpm(am);
        d.setTimeZoneId(timeZoneId);
        return d;
    }
    
    public static String[] getTimeZoneIds() {
        return TimeZone.getAvailableIDs();
    }
    
    public static Vector getTimeZoneData() {
        Vector list = new Vector();
        String ids[] = TimeZone.getAvailableIDs();
        for ( String id : ids ) {
            TimeZone tz = TimeZone.getTimeZone(id);
            String displayName = tz.getDisplayName();
            // Get the number of hours from GMT
            int rawOffset = tz.getRawOffset();
            int hour = rawOffset / (60*60*1000);
            int min = Math.abs(rawOffset / (60*1000)) % 60;
            String fromGMT = hour > 0 ? "+" + Integer.toString(hour) : Integer.toString(hour);
            
            TimeZoneValue t = new TimeZoneValue();
            t.setId(id);
            t.setName(displayName);
            t.setHourFromGMT(hour);
            t.setMinuteFromGMT(min);
            t.setFromGMT(fromGMT);
            
            list.addElement(t);
            
        }
        Collections.sort(list);
        return list;
    }
    
	public static String getDateTime(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int ampm = c.get(Calendar.AM_PM);
		return  year + "-" + month + "-" + day + " " 
				+ hour + ":" + 
				(min < 10 ? "0" + min : Integer.toString(min)) + 
				(ampm == 0 ? " AM" : " PM");
	}
	
	public static String getDate(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return  year + "-" + month + "-" + day;
	}	
	
	public static String getTime(Calendar c) {
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int ampm = c.get(Calendar.AM_PM);
		return  hour + ":" + 
				(min < 10 ? "0" + min : Integer.toString(min)) + 
				(ampm == 0 ? " AM" : " PM");
	}
	
	public static String formatDecimal(float number) {
		try {
			return new java.text.DecimalFormat("#,###,###.00").format(number);
		} catch ( Exception e ) {
			return Float.toString(number);
		}
	}
	
	public static String formatDecimal(Float number) {
		if ( number == null ) return  "";
		try {
			return new java.text.DecimalFormat("#,###,###.00").format(number);
		} catch ( Exception e ) {
			return Float.toString(number);
		}
	}	
	
	public static String formatDecimal(double number) {
		try {
			return new java.text.DecimalFormat("#,###,###.00").format(number);
		} catch ( Exception e ) {
			return Double.toString(number);
		}
	}	
	
	public static String formatDecimal(Double number) {
		if ( number == null ) return  "";
		try {
			return new java.text.DecimalFormat("#,###,###.00").format(number);
		} catch ( Exception e ) {
			return Double.toString(number);
		}
	}
	
	
	public static String capitalizedFirstCharacter(String str) {
		boolean cap = true;
		StringBuffer s = new StringBuffer();
		for ( int i=0; i < str.length(); i++ ) {
			char c = str.charAt(i);
			if ( cap ) s.append(String.valueOf(c).toUpperCase());
			else s.append(String.valueOf(c).toLowerCase()); 
			cap = false;
			if ( c == ' ' ) cap = true;
		}
		return s.toString();
	}
	
	public static String capitalized(String str) {
		StringTokenizer t = new StringTokenizer(str, " ");
		String n = "";
		while ( t.hasMoreTokens() ) {
			n += StringUtils.capitalize(t.nextToken().toLowerCase()) + " ";
		}
		return n.trim();
	}
	
	public static String subString(String str, int n) {
		if ( str == null ) return "";
		if ( str.length() > n ) return str.substring(0, n) + "...";
		return str;
	}
	
	public static void main(String[] args) {
		String s ="FACUsdf sdfsdf sdfsdsdf";
		System.out.println(Util.subString(s, 5));
	}
    
	public static String urlEncode(String str) {
		try {
			return java.net.URLEncoder.encode(str, "UTF-8");
		} catch ( Exception e ) {
			return str;
		}
	}
  

}
