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

package lebah.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.util.*;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */


public class ConnectionPool {
	
	private static Vector<Connection> pools = new Vector<Connection>();
	
	public synchronized static void addConnection(Connection connection) {
		//System.out.println("add " + connection);
		pools.addElement(connection);
	}
	
	public synchronized static Connection getConnection() {
		Connection connection = takeFromPool();
		try {
			if ( connection.isClosed() ){
				releaseAll();
				connection = null;
			}
		} 
		catch ( Exception e ){
			//System.out.println("ERROR: " + e.getMessage());
			connection = null;
		}
		return connection;
	}

	private static Connection checkIsClosed(Connection connection) {
		try {
			if ( connection.isClosed() ) {
				connection = takeFromPool();
			}
		} catch ( SQLException e ) { }
		return connection;
	}

	private static Connection takeFromPool() {
		if ( pools.size() == 0 ) return null;
		Connection connection = pools.elementAt(0);
		pools.remove(0);
		return connection;
	}
	
	public synchronized static void releaseAll() {
		for ( int i=0; i < pools.size(); i++ ) {
			try {
				Connection conn = pools.elementAt(i);
				conn.close();
			} catch ( SQLException e ) { }
		}
		pools.removeAllElements();
	}
	
	public synchronized static void releaseConnection(Connection conn) {
		int i = pools.indexOf(conn);
		if ( i > -1 ) {
			Connection connection = (Connection) pools.elementAt(i);
			pools.removeElementAt(i);
			try {
				connection.close();
			} catch ( SQLException e ) {
				
			}
		}
	}
	
	public static Vector getConnectionList() {
		return pools;
	}

}
