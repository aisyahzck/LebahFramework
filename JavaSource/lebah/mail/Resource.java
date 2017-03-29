/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.mail;
import java.io.*;
import java.util.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class Resource {
	private static ResourceBundle rb;
	private static String ATTACHMENT_DIR;
	private static String OBJECT_DIR;
	private static String APPNAME;
	private static String HOST;
	private static String PROTOCOL;
	
	static {
		try {
			rb = ResourceBundle.getBundle("common");
			read();
		} catch ( MissingResourceException e ) {
			System.out.println(e.getMessage());	
		}
	}

	public static String getAttachmentDir() { return ATTACHMENT_DIR; }
	public static String getObjectDir() { return OBJECT_DIR; }
	public static String getAppName() { return APPNAME; }
	public static String getHost() { return HOST; }
	public static String getProtocol() { return PROTOCOL; }

	public static void read() {
		readATTACHMENTDIR();
		readOBJECTDIR();
		readAPPNAME();
		readHOST();
		readPROTOCOL();
	}

	private static void readATTACHMENTDIR() {
		try {
			ATTACHMENT_DIR = rb.getString("attach");
		} catch ( MissingResourceException e ) { 
			System.out.println("Recource - " + e.getMessage());	
		}
	}
	
	private static void readOBJECTDIR() {
		try {
			OBJECT_DIR = rb.getString("object");
		} catch ( MissingResourceException e ) { 
			System.out.println("Recource - " + e.getMessage());	
		}
	}	
	
	private static void readAPPNAME() {
		try {
			APPNAME = rb.getString("appname");
		} catch ( MissingResourceException e ) { 
			System.out.println("Recource - " + e.getMessage());	
		}
	}	
	
	private static void readHOST() {
		try {
			HOST = rb.getString("host");
		} catch ( MissingResourceException e ) { 
			System.out.println("Recource - " + e.getMessage());	
		}
	}	

	private static void readPROTOCOL() {
		try {
			PROTOCOL = rb.getString("protocol");
		} catch ( MissingResourceException e ) { 
			System.out.println("Recource - " + e.getMessage());	
		}
	}	

	
}
