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
package lebah.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class DateTool {
	private static String[] month_short = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private static String[] month_long = {"January", "February", "Marc", "April", "May", "Jun", "July", "August", "September", "October", "November", "December"};
    private static String[] day_name = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static String[] hour_name = {"12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM",
                                                        "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM"};

    public static String[] getMonthArray() {
        return month_long;
    }
	
	public static String asShortMonth(int year, int month, int day) {
		return day + " " + month_short[month-1] + ", " + year;		
	}
	
	public static String asLongMonth(int year, int month, int day) {
		return day + " " + month_long[month-1] + ", " + year;		
	}	
	
	public static String asShortMonth(Integer year, Integer month, Integer day) {
		return day.intValue() + " " + month_short[month.intValue()-1] + ", " + year.intValue();		
	}	
	
	public static String asLongMonth(Integer year, Integer month, Integer day) {
		return day.intValue() + " " + month_long[month.intValue()-1] + ", " + year.intValue();		
	}
	
	public static String asShortMonth(String year, String month, String day) {
		return (!"".equals(day) ? day : "1") + " " + month_short[!"".equals(month) ? Integer.parseInt(month)-1 : 0] + ", " + (!"".equals(year) ? year : "2005");		
	}
	
	public static String asLongMonth(String year, String month, String day) {
	    return (!"".equals(day) ? day : "1") + " " + month_long[!"".equals(month) ? Integer.parseInt(month)-1 : 0] + ", " + (!"".equals(year) ? year : "2005");
	}	
	
	/*
	
	example usage:
		String date = DateTool.getDateFormatted(rs.getDate("receipt_date"));
	
	*/
	
	public static String getDateFormatted(java.util.Date date) {
	    if ( date == null ) return "";
	    else
	    	return new java.text.SimpleDateFormat ("d MMM, yyyy").format(date);
	    	//return new java.text.SimpleDateFormat ("d MMM, yyyy").format(date);
		//return new java.text.SimpleDateFormat ("EEE, d MMM, yyyy").format(date);
 	}	
    
    public static String getDateTimeFormatted(java.util.Date date) {
        if ( date == null ) return "";
        else
            return new java.text.SimpleDateFormat ("d MMM, yyyy HH:MM:SS").format(date);
            //return new java.text.SimpleDateFormat ("d MMM, yyyy").format(date);
        //return new java.text.SimpleDateFormat ("EEE, d MMM, yyyy").format(date);
    }    
	
	public static String getCurrentDate() {
		//YY-MM-DD HH:MM:SS
		java.util.Calendar cal = new java.util.GregorianCalendar();
	    int year = cal.get(java.util.Calendar.YEAR);
	    int month = cal.get(java.util.Calendar.MONTH) + 1;
	    int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
	    int hour12 = cal.get(java.util.Calendar.HOUR);
	    int hour24 = cal.get(java.util.Calendar.HOUR_OF_DAY);
	    int min = cal.get(java.util.Calendar.MINUTE);        
	    int sec = cal.get(java.util.Calendar.SECOND);        
	    String ampm = cal.get(java.util.Calendar.AM_PM) == 0 ? "AM" : "PM";     
	    String time = hour24 < 10 ? "0" + hour24 : "" + hour24;
	    time += ":";
	    time += min < 10 ? "0" + min : "" + min;
	    time += ":";
	    time += sec < 10 ? "0" + sec : "" + sec;
	    return year + "-" + fmt(Integer.toString(month)) + "-" + fmt(Integer.toString(day)) + " " + time;
	}
	
	public static String getCurrentDatetime() {
		//YY-MM-DD HH:MM:SS
		java.util.Calendar cal = new java.util.GregorianCalendar();
	    int year = cal.get(java.util.Calendar.YEAR);
	    int month = cal.get(java.util.Calendar.MONTH) + 1;
	    int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
	    int hour12 = cal.get(java.util.Calendar.HOUR);
	    int hour24 = cal.get(java.util.Calendar.HOUR_OF_DAY);
	    int min = cal.get(java.util.Calendar.MINUTE);        
	    int sec = cal.get(java.util.Calendar.SECOND);        
	    String ampm = cal.get(java.util.Calendar.AM_PM) == 0 ? "AM" : "PM";     
	    String time = hour24 < 10 ? "0" + hour24 : "" + hour24;
	    time += ":";
	    time += min < 10 ? "0" + min : "" + min;
	    time += ":";
	    time += sec < 10 ? "0" + sec : "" + sec;
	    return year + "-" + fmt(Integer.toString(month)) + "-" + fmt(Integer.toString(day)) + " " + time;
	}
	
	public static Hashtable getCurrentDateTime() {
	    Hashtable h = new Hashtable();
		java.util.Calendar cal = new java.util.GregorianCalendar();
	    int year = cal.get(java.util.Calendar.YEAR);
	    int month = cal.get(java.util.Calendar.MONTH) + 1;
	    int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
	    int hour12 = cal.get(java.util.Calendar.HOUR);
	    int hour24 = cal.get(java.util.Calendar.HOUR_OF_DAY);
	    int min = cal.get(java.util.Calendar.MINUTE);        
	    int sec = cal.get(java.util.Calendar.SECOND);        
	    String ampm = cal.get(java.util.Calendar.AM_PM) == 0 ? "AM" : "PM";
	    h.put("year", new Integer(year));
	    h.put("month", new Integer(month));
	    h.put("day", new Integer(day));
	    h.put("hour12", new Integer(hour12));
	    h.put("hour24", new Integer(hour24));
	    h.put("min", new Integer(min));
	    h.put("sec", new Integer(sec));
	    h.put("ampm", ampm);
	    h.put("calendar", cal);
	    return h;
	}
	
	public static Hashtable getYMD(java.util.Date date) {
	    Hashtable h = new Hashtable();
		java.util.Calendar cal = new java.util.GregorianCalendar();
		cal.setTime(date);
	    int year = cal.get(java.util.Calendar.YEAR);
	    int month = cal.get(java.util.Calendar.MONTH) + 1;
	    int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
	    int hour12 = cal.get(java.util.Calendar.HOUR);
	    int hour24 = cal.get(java.util.Calendar.HOUR_OF_DAY);
	    int min = cal.get(java.util.Calendar.MINUTE);        
	    int sec = cal.get(java.util.Calendar.SECOND);        
	    String ampm = cal.get(java.util.Calendar.AM_PM) == 0 ? "AM" : "PM";
	    h.put("year", new Integer(year));
	    h.put("month", new Integer(month));
	    h.put("day", new Integer(day));
	    h.put("hour12", new Integer(hour12));
	    h.put("hour24", new Integer(hour24));
	    h.put("min", new Integer(min));
	    h.put("sec", new Integer(sec));
	    h.put("ampm", ampm);
	    return h;	    
	}
	
	public static String getDateStr(java.util.Date date) {
		java.util.Calendar cal = new java.util.GregorianCalendar();
		cal.setTime(date);
	    int year = cal.get(java.util.Calendar.YEAR);
	    int month = cal.get(java.util.Calendar.MONTH) + 1;
	    int day = cal.get(java.util.Calendar.DAY_OF_MONTH);		
	    return getDateStr(year, month, day);
	}	
	
    
    /*
     * We have a problem here!
     * If using MySQL the default date format is DD-MM-YYYY example 12-07-2005
     * but if using ORACLE the default is YYYY-MMM-DD, where MMM is the month's name, 
     * for examle 12-Jun-2004
     */
    
	public static String getDateStr(int year, int month, int day) {
		if ( year == 0 || month == 0 ) 
			return "";
		else {
			//return fmt(Integer.toString(day)) + "-" + month_short[month-1] + "-" + Integer.toString(year).substring(2);
            return year + "-" + fmt(Integer.toString(month)) + "-" + fmt(Integer.toString(day));
        }
	}
	
	public static String getDateStr(String year, String month, String day) {
		return getDateStr(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
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
	
	public static void main(String[] args) {
	    Format formatter;
	    Date date = new Date();
	    formatter = new SimpleDateFormat("yy");
	    String yr = formatter.format(date);
	    System.out.println(yr);
	    formatter = new SimpleDateFormat("MM");
	    String mn = formatter.format(date);
	    System.out.println(mn);
	    
	}
    
	
}
