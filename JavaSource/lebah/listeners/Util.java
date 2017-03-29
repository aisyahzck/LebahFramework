package lebah.listeners;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
	
	private static Util instance = null;
	
	public static Util getInstance() {
		if ( instance == null ) instance = new Util();
		return instance;
	}
	
	public String fileName(String str) {
		String filename = "";
		if ( str != null && !"".equals(str)) {
			filename = str.substring(str.lastIndexOf("/")+1);
		} else {
			filename = str;
		}
		
		return filename;
	}
	
	public static int getDaysBetween(Calendar d1, Calendar d2) {
	    if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
	        java.util.Calendar swap = d1;
	        d1 = d2;
	        d2 = swap;
	    }
	    int days = d2.get(java.util.Calendar.DAY_OF_YEAR) -
	               d1.get(java.util.Calendar.DAY_OF_YEAR);
	    int y2 = d2.get(java.util.Calendar.YEAR);
	    if (d1.get(java.util.Calendar.YEAR) != y2) {
	        d1 = (java.util.Calendar) d1.clone();
	        do {
	            days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
	            d1.add(java.util.Calendar.YEAR, 1);
	        } while (d1.get(java.util.Calendar.YEAR) != y2);
	    }
	    return days;
	} 
	
	public static long difference(Calendar c1, Calendar c2, int unit) { 
		differenceCheckUnit(unit); 
		Map<Integer, Long> unitEstimates = differenceGetUnitEstimates(); 
		Calendar first = (Calendar) c1.clone(); 
		Calendar last = (Calendar) c2.clone(); 
		long difference = c2.getTimeInMillis() - c1.getTimeInMillis(); 
		long unitEstimate = unitEstimates.get(unit).longValue(); 
		long increment = (long) Math.floor((double) difference / (double) unitEstimate); increment = Math.max(increment, 1); 
		long total = 0; while (increment > 0) { add(first, unit, increment); 
		if (first.after(last)) { add(first, unit, increment * -1); 
		increment = (long) Math.floor(increment / 2); 
		} 
		else { total += increment; } } return total; 
		} 
	
	private static Map<Integer, Long> differenceGetUnitEstimates() { 
		Map<Integer, Long> unitEstimates = new HashMap<Integer, Long>(); 
		unitEstimates.put(Calendar.YEAR, 1000l * 60 * 60 * 24 * 365); 
		unitEstimates.put(Calendar.MONTH, 1000l * 60 * 60 * 24 * 30); 
		unitEstimates.put(Calendar.DAY_OF_MONTH, 1000l * 60 * 60 * 24); 
		unitEstimates.put(Calendar.HOUR_OF_DAY, 1000l * 60 * 60); 
		unitEstimates.put(Calendar.MINUTE, 1000l * 60); 
		unitEstimates.put(Calendar.SECOND, 1000l); 
		unitEstimates.put(Calendar.MILLISECOND, 1l); 
		return unitEstimates; 
	} 
	
	private static void differenceCheckUnit(int unit) { 
		List<Integer> validUnits = new ArrayList<Integer>(); 
		validUnits.add(Calendar.YEAR); 
		validUnits.add(Calendar.MONTH); 
		validUnits.add(Calendar.DAY_OF_MONTH); 
		validUnits.add(Calendar.HOUR_OF_DAY); 
		validUnits.add(Calendar.MINUTE); 
		validUnits.add(Calendar.SECOND); 
		validUnits.add(Calendar.MILLISECOND); 
		if (!validUnits.contains(unit)) { 
			throw new RuntimeException( "CalendarUtils.difference one of these units Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND." ); 
			
		} 
		
	} 
	
	public static void add(Calendar c, int unit, long increment) { 
		while (increment > Integer.MAX_VALUE) { 
			c.add(unit, Integer.MAX_VALUE); 
			increment -= Integer.MAX_VALUE; 
			
		} 
		
		c.add(unit, (int) increment); 
		
	}
	
	public static String convertMilliseconds(long ms) {
	    int seconds = (int) ((ms / 1000) % 60);
	    int minutes = (int) (((ms / 1000) / 60) % 60);
	    int hours = (int) ((((ms / 1000) / 60) / 60) % 24);

	    String sec, min, hrs;
	    if(seconds<10)  sec="0"+seconds;
	    else            sec= ""+seconds;
	    if(minutes<10)  min="0"+minutes;
	    else            min= ""+minutes;
	    if(hours<10)    hrs="0"+hours;
	    else            hrs= ""+hours;

	    if(hours == 0)  return min+":"+sec;
	    else    return hrs+":"+min+":"+sec;

	}


}
