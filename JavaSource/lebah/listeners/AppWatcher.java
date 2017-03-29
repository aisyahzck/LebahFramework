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

package lebah.listeners;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public final class AppWatcher implements Runnable {
    
    private static AppWatcher instance = null;
    
	private Thread thread;
	private long seconds, totalMem, freeMem;
    
    private AppWatcher() {
        
    }
    
    public static AppWatcher getInstance() {
        if ( instance == null ) instance = new AppWatcher();
        return instance;
    }
	
	public void run() {
		System.out.println("AppWatcher is running..");
		while ( thread != null ) {
			try {
				Thread.sleep(5000);  
				seconds++;
				totalMem = Runtime.getRuntime().totalMemory();
				freeMem = Runtime.getRuntime().freeMemory();
				//System.gc();
				System.out.println(seconds + ") free memory = " + freeMem + " / " + totalMem);					
			} catch ( InterruptedException e ) {}			
		}
		System.out.println("AppWatcher is stopped!");
	}
	
	public void start() {
		thread = new Thread(this);
		thread.setName("Lebah Watcher.");
		thread.start();	
        System.out.println("Started!");
	}
	
	public void stop() {
		thread = null;
        System.out.println("Stopped!");
	}
    
    public static void main(String[] args) {
        AppWatcher w = AppWatcher.getInstance();
        w.start();
    }

    public long getFreeMem() {
        return freeMem;
    }

    public void setFreeMem(long freeMem) {
        this.freeMem = freeMem;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(long totalMem) {
        this.totalMem = totalMem;
    }
	
}
