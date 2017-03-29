package lebah.listeners;

import java.util.Date;

public class MessageService  implements Runnable {
	
	private static MessageService instance = null; 
	private Thread thread;
	private String name;
	private long totalMem;
	private long cnt = 0;
	private long freeMem;
	float percentageMem;
	private long gcount = 0;
	
	private int garbageInterval = 0; //seconds to garbage collector request
	//by default request gc only when memory is below 5% free
	private int garbagePercentageTreshold = 5; //in percentage
	
	
	private Date upDateTime;
	
	public static MessageService getInstance() {
		if ( instance == null ) {
			instance = new MessageService();
			instance.start();
		}
		return instance;
	}
	
	private MessageService() {
		upDateTime = new Date();
	}

	public void run() {
		
		//System.out.println("Display free memory / total memory: ");
		totalMem = Runtime.getRuntime().totalMemory();
		freeMem = Runtime.getRuntime().freeMemory();
		long diffMem = totalMem - freeMem;
		percentageMem = (float)freeMem/(float) totalMem * 100;
		//System.out.println("free/total memory = " + freeMem + " / " + totalMem + " : " + diffMem + "" + percentageMem);

		int garbageSeconds = garbageInterval*1000;
		
		while ( thread != null ) {
			try {
				
				Thread.sleep(10000);
				totalMem = Runtime.getRuntime().totalMemory();
				freeMem = Runtime.getRuntime().freeMemory();
				diffMem = totalMem - freeMem;
				percentageMem = (float)freeMem/(float) totalMem * 100;
				System.out.println(++cnt + " - free/total memory = " + freeMem + " / " + totalMem + " : " + diffMem + " , " + percentageMem);

				++cnt;
				if ( garbageInterval > 0 && cnt > garbageInterval ) {
					if ( ++gcount > 1000 ) gcount = 1;
					System.out.println(gcount + ") Request garbage collection by Interval = " + garbageInterval + " seconds");
					cnt = 0;
					System.gc();
				}
				
				if ( garbagePercentageTreshold > 0 && this.getPercentageMem2() <= garbagePercentageTreshold ) {
					if ( ++gcount > 1000 ) gcount = 1;
					System.out.println(gcount + ") Request garbage collection by Treshold = " + garbagePercentageTreshold + "%");
					System.gc();
				}
				
				if ( cnt > 1000000 ) cnt = 0;
			
			} catch ( InterruptedException e ) {}			
		}
	}
	
	public void start() {
		System.out.println("MessageService Started! " + this);
		thread = new Thread(this);
		thread.start();	
	}
	
	public void stop() {
		thread = null;
        System.out.println("MessageService Stopped!");
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public String getName() {
		return name;
	}

	public long getTotalMem() {
		return totalMem;
	}

	public void setTotalMem(long totalMem) {
		this.totalMem = totalMem;
	}

	public long getFreeMem() {
		return freeMem;
	}

	public void setFreeMem(long freeMem) {
		this.freeMem = freeMem;
	}

	public float getPercentageMem() {
		return percentageMem;
	}
	
	public int getPercentageMem2() {
		return (int) percentageMem;
	}

	public void setPercentageMem(float percentageMem) {
		this.percentageMem = percentageMem;
	}

	public int getGarbageInterval() {
		return garbageInterval;
	}

	public void setGarbageInterval(int interval) {
		if ( garbageInterval != interval ) System.out.println("Garbage Interval = " + interval);
		this.garbageInterval = interval;
	}

	public int getGarbagePercentageTreshold() {
		return garbagePercentageTreshold;
	}

	public void setGarbagePercentageTreshold(int garbagePercentageTreshold) {
		if ( this.garbagePercentageTreshold != garbagePercentageTreshold ) System.out.println("Garbage Treshold = " + garbagePercentageTreshold);
		this.garbagePercentageTreshold = garbagePercentageTreshold;
	}

	public Date getUpDateTime() {
		return upDateTime;
	}

	public void setUpDateTime(Date upDateTime) {
		this.upDateTime = upDateTime;
	}
	
	
	
	

}
