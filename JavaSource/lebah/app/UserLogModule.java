/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.app; 


import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.db.UserTrackerLog;
import lebah.util.Paging;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
 
public class UserLogModule extends lebah.portal.velocity.VTemplate {
	
	private String timeZoneId = "";
    
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		String template_name = "vtl/admin/tracker.vm";
		String submit = getParam("command");
		
		
		int year1 = 0, month1 = 0, day1 = 0;
		java.util.Calendar calendar = "".equals(timeZoneId) ? 
									  new java.util.GregorianCalendar() :
									  new java.util.GregorianCalendar(java.util.TimeZone.getTimeZone(timeZoneId));
		calendar.setTime(new java.util.Date());		
		
		String userLogin = request.getParameter("user_login") != null ? 
							!"".equals(request.getParameter("user_login")) ?
							        request.getParameter("user_login") : "" : "";
		int yr = request.getParameter("year") != null ? 
							!"".equals(request.getParameter("year")) ?
							        Integer.parseInt(request.getParameter("year")) : 
							        calendar.get(calendar.YEAR) : calendar.get(calendar.YEAR);
		int mn = request.getParameter("month") != null ? 
							!"".equals(request.getParameter("month")) ?
							        Integer.parseInt(request.getParameter("month")) : 
							        calendar.get(calendar.MONTH) + 1 : calendar.get(calendar.MONTH) + 1;		
		int dy = request.getParameter("day") != null ? 
							!"".equals(request.getParameter("day")) ?
							        Integer.parseInt(request.getParameter("day")) : 
							        calendar.get(calendar.DAY_OF_MONTH) : calendar.get(calendar.DAY_OF_MONTH);							        					        
		int yr2 = request.getParameter("year2") != null ? 
							!"".equals(request.getParameter("year2")) ?
							        Integer.parseInt(request.getParameter("year2")) : 
							        calendar.get(calendar.YEAR) : calendar.get(calendar.YEAR);
		int mn2 = request.getParameter("month2") != null ? 
							!"".equals(request.getParameter("month2")) ?
							        Integer.parseInt(request.getParameter("month2")) : 
							        calendar.get(calendar.MONTH) + 1 : calendar.get(calendar.MONTH) + 1;		
		int dy2 = request.getParameter("day") != null ? 
							!"".equals(request.getParameter("day2")) ?
							        Integer.parseInt(request.getParameter("day2")) : 
							        calendar.get(calendar.DAY_OF_MONTH) : calendar.get(calendar.DAY_OF_MONTH);							        					        
							        
		if ( !"".equals(userLogin)) {
			context.put("userLogin", userLogin);
			context.put("year", new Integer(yr));
			context.put("month", new Integer(mn));
			context.put("day", new Integer(dy));
			context.put("year2", new Integer(yr2));
			context.put("month2", new Integer(mn2));
			context.put("day2", new Integer(dy2));			
		    Vector logList = UserTrackerLog.getUserTrackerList(userLogin, yr, mn, dy, yr2, mn2, dy2);
			int pageNum = request.getParameter("pageNum") != null ?
							Integer.parseInt(request.getParameter("pageNum")) : 1;	
			int numOfRows = request.getParameter("rows") != null ?
			        		Integer.parseInt(request.getParameter("rows")) : 5;
			
			Paging paging = null;
			if ( "getLog".equals(submit) ) {
				paging = new Paging(session, logList, numOfRows);
				session.setAttribute("logPaging", paging);
			}
			else {			        		
				//get paging from session, else create new Paging
				paging = session.getAttribute("logPaging") != null ?
								(Paging) session.getAttribute("logPaging") :
								new Paging(session, logList, numOfRows);
				//put paging into session
				session.setAttribute("logPaging", paging);
			}			
			Vector items = paging.getPage(pageNum);
			context.put("items", items);	
			context.put("pageNum", new Integer(pageNum));
			context.put("numOfPages", new Integer(paging.getTotalPages()));
			context.put("startNumber", new Integer(paging.getTopNumber()));
		}
		Template template = engine.getTemplate(template_name);	
		return template; 
	}
}		
