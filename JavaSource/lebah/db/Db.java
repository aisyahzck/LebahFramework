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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import lebah.util.CDate;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Db
{

    protected Connection conn;
    protected Statement stmt;
    protected String sql;
    protected String connectionURL;
    



	/**
     * 
     * @throws DbException
     */
    
    public Db() throws DbException {
		this("");   
    }    
    

    
    /**
     * A constructor to create a singleton ConnectonSource object.  This will
     * result a connection object with same value.
     * @param resource
     * @throws DbException
     */

    public Db(String resource) throws DbException
    {
        try
        {
            ConnectionProperties connectionproperties = ConnectionProperties.getInstance(resource);
            if (connectionproperties != null) { 
            	try {
            		getConnection(connectionproperties);
            	} catch ( Exception e ) {
            		e.printStackTrace();
            		//try again
            		getConnection(connectionproperties);
            	}
            } else {
            	System.out.println("Can't get dbconnection.properties");
            	//try again
            	getConnection(connectionproperties);
            }
            if (connectionproperties == null ) {
            	throw new DbException("Db(resource) : Error getting connection properties.");
            }
        }
        catch(SQLException sqlexception) {
            throw new DbException("Db - SQLException : " + sqlexception.getMessage());
        }
    }



	private void getConnection(ConnectionProperties connectionproperties) throws DbException, SQLException {
		connectionproperties.read();
		if ( connectionproperties.isUseLookup() ) {
			String lookupName = connectionproperties.getLookup();
			conn = ConnectionSource.getInstance(lookupName).getConnection();
		} else {                
		    String s = connectionproperties.getDriver();
		    String s1 = connectionproperties.getUrl();
		    String s2 = connectionproperties.getUser();
		    String s3 = connectionproperties.getPassword();
		    //if does not specifically defined pooled as true then get pooled status from properties
		    boolean pooled = connectionproperties.isUseConnectionPool();
		    connectionURL = s1;
		    conn = ConnectionSource.getInstance(s, s1, s2, s3, pooled).getConnection();
		}
		conn.setAutoCommit(true);
		stmt = conn.createStatement();
	}
    
    
    /**
     * A constructor to create a new instance of ConnectionSource, thus shall
     * create a different connection values each time this constructor
     * is called.
     * The Hashtable argument consistes of driver, url, user, and password.
     * @param prop
     * @throws DbException
     */
    
    public Db(Hashtable prop) throws DbException
    {
        try
        {
	        if ( prop != null ) 
	        {
	            conn = ConnectionSource.getNewInstance((String) prop.get("driver"), 
					    (String) prop.get("url"), 
					    (String) prop.get("user"), 
					    (String) prop.get("password"), false).getConnection();
	            conn.setAutoCommit(true);
                stmt = conn.createStatement();
            } else
            {
                throw new DbException("Db : properties table is null");
            }
        }
        catch(SQLException sqlexception)
        {
            throw new DbException("Db - SQLException : " + sqlexception.getMessage());
        }
    }    
    


    public Connection getConnection()
    {
        return conn;
    }

    public String getSQL()
    {
        return sql;
    }

    public Statement getStatement()
    {
        return stmt;
    }

    public void close()
    {
        if(conn != null)
        {
            try
            {
                stmt.close();
            }
            catch(SQLException sqlexception) { }
            ConnectionSource.close(conn);
        }
    }

    protected String dblch(String s, char c)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        if(s != null)
        {
            for(int i = 0; i < s.length(); i++)
            {
                char c1 = s.charAt(i);
                if(c1 == c)
                    stringbuffer.append(c).append(c);
                else
                    stringbuffer.append(String.valueOf(c1));
            }

        } else
        {
            stringbuffer.append("");
        }
        return stringbuffer.toString();
    }

    protected String replace(String s)
    {
        return dblch(s, '\'');
    }
    
    public static String getString(ResultSet rs, String name) throws Exception {
		//String data = rs.getString(name);
		//if ( data != null ) return data;
		//else return "";
        try {
        	String data = rs.getString(name);
    		if ( data != null ) return data;
        } catch (SQLException e ) {
            System.out.println("Date column throw Exception: " + e.getMessage());
        }
        return "";		
    }
    
    public static java.util.Date getDate(ResultSet rs, String name) throws Exception {
        try {
            CDate date = new CDate(rs.getDate(name) != null ? rs.getDate(name) : new java.util.Date());
            return date;
        } catch (SQLException e ) {
            //System.out.println("Date column throw Exception: " + e.getMessage());
        }
        //if null then return this date 1 jan 1
        return new CDate(new GregorianCalendar(1, 0, 1).getTime());
    }  
    
    public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

}
