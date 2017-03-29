package lebah.listeners;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class ServerStatusModule extends LebahModule {
	
	private String path = "vtl/monitor/";

	@Override
	public String start() {
		return path + "start.vm";
	}
	
	@Command("app_status")
	public String appStatus() throws Exception {
		
		context.put("dateFormat", new SimpleDateFormat("dd-MM-yyyy hh:mm a"));
		context.put("timeFormat", new SimpleDateFormat("hh:mm a"));	
		context.put("date_now", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

		
		MessageService s = MessageService.getInstance();
		
		int garbageInterval = s.getGarbageInterval();
		int garbageTreshold = s.getGarbagePercentageTreshold();
		Date upDateTime = s.getUpDateTime();
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(upDateTime);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date());
		
		Long diff = Util.difference(c1, c2, Calendar.MILLISECOND);
		context.put("diff_in_millis", diff);
		
		String upTime = Util.convertMilliseconds(diff);
		context.put("uptime", upTime);
		
		String garbageInterval_ = request.getParameter("garbage_interval");
		String garbageTreshold_ = request.getParameter("garbage_treshold");
		
		try {
			garbageInterval = Integer.parseInt(garbageInterval_);
		} catch ( Exception e ) {}
		try {
			garbageTreshold = Integer.parseInt(garbageTreshold_);
		} catch ( Exception e ) {}
		
		
		
		s.setGarbageInterval(garbageInterval);
		s.setGarbagePercentageTreshold(garbageTreshold);
		
		long freeMem = s.getFreeMem();
		long totalMem = s.getTotalMem();
		float p = s.getPercentageMem();
		int p2 = s.getPercentageMem2();
		context.put("bar_size", p2);
		context.put("percentage_memory", p);
		context.put("app_memory_status", freeMem + "/" + totalMem + " = " + p + "%");
		context.put("free_memory", freeMem);
		context.put("total_memory", totalMem);
		context.put("garbage_treshold", s.getGarbagePercentageTreshold());
		context.put("garbage_interval", s.getGarbageInterval());
		context.put("upDateTime", upDateTime);
		context.put("now", new Date());
		return path + "app_status.vm";
	}

}
