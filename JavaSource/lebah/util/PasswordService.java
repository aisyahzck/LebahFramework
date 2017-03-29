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

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public final class PasswordService {
	
	private static String testpwd;
	static {
		try {
	 		testpwd = encrypt("super");
 		} catch ( Exception e ) {}
 	}
	
	public static String encrypt(String txt) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA"); 
		md.update(txt.getBytes("UTF-8")); 
		byte raw[] = md.digest(); 
		String hash = (new BASE64Encoder()).encode(raw); 
		return hash; 
	}
	
	public static boolean compare(String userpwd, String storedpwd) throws Exception {
		return storedpwd.equals(encrypt(userpwd));
	}
	
	public static void main(String[] args) throws Exception {
		String txt = "secretpassword";
		MessageDigest md = MessageDigest.getInstance("SHA"); 
		md.update(txt.getBytes("UTF-8")); 
		byte raw[] = md.digest(); 
		String hash = (new BASE64Encoder()).encode(raw); 	
		System.out.println(hash);
	}
}
