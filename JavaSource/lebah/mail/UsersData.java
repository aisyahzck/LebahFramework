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
package lebah.mail;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class UsersData {

	public static List get(String host, int port, String login, String password) throws JobFailedException {
		List list = new ArrayList();
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

			w.println("listusers");
			w.flush();
			str = r.readLine();
			int cnt = Integer.parseInt(str.substring("Existing accounts ".length()));

			for ( int j = 0; j < cnt; j++ ) {
				str = r.readLine();
				String usr = str.substring("user: ".length());
				list.add(usr);
			}
			//sort the list
			Collections.sort(list, new NameComparator());
			
		} catch ( ConnectException cex ) {
			throw new JobFailedException(cex.getMessage());
		} catch ( IOException ioex ) {
			throw new JobFailedException(ioex.getMessage());
		} finally {
			if ( s != null ) try { s.close(); } catch ( IOException sx ){}
			if ( r != null ) try { r.close(); } catch ( IOException rx ){}
			if ( w != null ) w.close();
		}
		return list;
	}
	
	public static class NameComparator extends lebah.util.MyComparator {
		public int compare(Object o1, Object o2) {
			String usr1 = (String) o1;
			String usr2 = (String) o2;
			return usr1.compareTo(usr2);
		}	
	}	
	
	public static void main(String args[]) {
		try {
			List list = UsersData.get("localhost", 4555, "root", "root");
			Iterator iter = list.iterator();
			while ( iter.hasNext() ) {
				String usr = (String) iter.next();
				System.out.println("User login = " + usr);	
			}
		} catch ( JobFailedException e ) {
			System.out.println( e.getMessage() );
		}
	}
}
