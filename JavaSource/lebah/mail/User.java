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
package lebah.mail;

import java.io.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class User implements Serializable {
	String username;
	String password;
	String host;
	String protocol;
	
	public User(String username, String password, String host, String protocol) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.protocol = protocol;
	}
	
	public void setPassword(String s) { this.password = s; }
	
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public String getHost() { return host; }
	public String getProtocol() { return protocol; }
	public String getEmail() { return username + "@" + host; }
	public String getUrl() { return protocol + "://"  + username + ":" + password + "@" + host; }

}
