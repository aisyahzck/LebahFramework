/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.db;

import java.net.ConnectException;
import java.net.SocketException;
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
public class ConnectionSource
{ 

    private static ConnectionSource instance;
    private String driver;
    private String url;
    private String userid;
    private String password;
    private String lookupName;
    
    private static boolean usePool = true;
    

    private ConnectionSource(String s, String s1, String s2, String s3, boolean pooled)
    { 
        driver = s;
        url = s1;
        userid = s2;
        password = s3;
        lookupName = "";
        usePool = pooled;
    }

    private ConnectionSource(String s, String s1)
    {
        this(s, s1, "", "", false);
    }
    
	private ConnectionSource(String lookupName) {
		this.lookupName = lookupName; 
	}    

	
	
    private Connection newConnection()
        throws DbException
    {
        Connection connection = null;
        try
        {
			if ( lookupName.equals("") ) {
				
				if ( usePool ) {
					connection = ConnectionPool.getConnection();
					if ( connection == null ) connection = createConnection();
				}
				else {
					connection = createConnection();
				}
			} else {
				
				InitialContext initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				DataSource datasource = (DataSource) envCtx.lookup(lookupName);
				connection = datasource.getConnection();				
			}
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new DbException("ConnectionSource - ClassNotFoundException : " + classnotfoundexception.getMessage());
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new DbException("ConnectionSource - IllegalAccessException:  - " + illegalaccessexception.getMessage());
        }
        catch(InstantiationException instantiationexception)
        {
            throw new DbException("ConnectionSource - InstantiationException:  - " + instantiationexception.getMessage());
		} 
		catch(javax.naming.NamingException nex) 
		{
			throw new DbException ("ConnectionSource - NamingException :" + lookupName + " - " + nex.getMessage());
		}
        catch(SocketException sock ) {
        	throw new DbException("ConnectionSource - SocketException: " + sock.getMessage());
        }
        catch(SQLException sqlexception)
        {
            throw new DbException("ConnectionSource -SQLException:  - " + sqlexception.getMessage());
        }        
        return connection;
    }

	private Connection createConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, SocketException, ConnectException {
		Connection connection;
		Class.forName(driver).newInstance();
		if ( (userid.equals("")) && (password.equals("")) )
			connection = DriverManager.getConnection(url);
		else
			connection = DriverManager.getConnection(url, userid, password);
		
		return connection;
	}

    public static synchronized ConnectionSource getInstance(String s, String s1, String s2, String s3, boolean pooled)
    {
       	if ( instance == null )
	    		instance = new ConnectionSource(s, s1, s2, s3, pooled);  
        return instance;
    }
    
    
    
    public static synchronized ConnectionSource getNewInstance(String s, String s1, String s2, String s3, boolean pooled)
    {
        return new ConnectionSource(s, s1, s2, s3, pooled);  
    }    
    
    public static synchronized ConnectionSource getInstance(String lookupName)
    {
       	if ( instance == null )
	    		instance = new ConnectionSource(lookupName);  
        return instance;
    }    

    public synchronized Connection getConnection()
        throws DbException
    {
        return newConnection();
    }

    public synchronized static void close(Connection connection)
    {
        if ( usePool ) {
        	ConnectionPool.addConnection(connection);
        }
        else {
	    	try
	        {
	    		//ConnectionPool.addConnection(connection); //for demo purposes only just to show how many has been created
	            connection.close();
	        }
	        catch(SQLException sqlexception)
	        {
	            Log.print(sqlexception.getMessage());
	        }
        }
    }
}
