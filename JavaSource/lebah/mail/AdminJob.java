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

import java.net.*;
import java.io.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class AdminJob {
	private String host, login, password;
	int port;
	
	public AdminJob(String host, int port, String login, String password) {
		this.host = host;
		this.port = port;
		this.login = login;
		this.password = password;
	}
	
	public String doTask(String cmd) throws JobFailedException {
		return doTask(host, port, login, password, cmd);	
	}
	
	public static String doTask(String host, int port, String login, String password, String cmd) throws JobFailedException {
		String result = "";
		Socket s = null;
		BufferedReader r = null;
		PrintWriter w = null;
		try {

			s = new Socket(host, port);
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			w = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			String str = "";
			//login id
			while ( !((str = r.readLine()).indexOf("Login id") == 0) );
			w.println(login);
			w.flush();
			//password
			while ( !((str = r.readLine()).indexOf("Password") == 0) );
			w.println(password);
			w.flush();
			//get input whether this login successfull or not
			str = r.readLine();
			int i = str.indexOf("Login failed");
			//if login failed throw an exception
			if ( i == 0 ) throw new JobFailedException("Login failed for " + login);

			//do the command and return the result
			result = doCmd(r, w, cmd);
		} catch ( ConnectException cex ) {
			throw new JobFailedException(cex.getMessage());
		} catch ( IOException ioex ) {
			throw new JobFailedException(ioex.getMessage());
		} finally {
			if ( s != null ) try { s.close(); } catch ( IOException sx ){}
			if ( r != null ) try { r.close(); } catch ( IOException rx ){}
			if ( w != null ) w.close();
		}
		return result;
	}
	

	private static String doCmd(BufferedReader r, PrintWriter w, String cmd) throws JobFailedException, java.io.IOException {
			w.println(cmd);
			w.flush();
			String str = r.readLine();
			return str;
	}
	
	public void adduser(String login, String password) throws JobFailedException {
		String result = doTask("adduser " + login + " " + password);	
		if ( result.indexOf("already exist") > -1 ) 
			throw new JobFailedException("User " + login + " already exist");
	}
	
	public void deleteuser(String login) throws JobFailedException {
		String result = doTask("deluser " + login);
	}
	
	public void setpassword(String login, String password) throws JobFailedException {
		String result = doTask("setpassword " + login + " " + password);
		if ( result.indexOf("No such user") > -1 ) 
			throw new JobFailedException("No such user");
	}
	
}
