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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ConnectionProperties
{

    private static ConnectionProperties instance;
    private ResourceBundle rb;
    private String driver;
    private String url;
    private String user;
    private String password;
    private String lookupName;
    private String dbserver;
    private String useConnectionPool;
    private boolean useLookup;
    
    private String url_catalog;
    



    private ConnectionProperties(String resource) throws MissingResourceException
    {
        rb = ResourceBundle.getBundle(resource);
        read();
    }    
    

    public static synchronized ConnectionProperties getInstance(String resource)
    {
	    if ( "".equals(resource) ) resource = "dbconnection";
        if(instance == null)
            try
            {
                instance = new ConnectionProperties(resource);
            }
            catch(MissingResourceException missingresourceexception)
            {
                instance = null;
                missingresourceexception.printStackTrace();
            }
           
        
        return instance;
    }
    
    public static ConnectionProperties getInstance() {
			return getInstance("");
    }

    public static ConnectionProperties refreshInstance(){
	    String resource = "dbconnection";
	    try {
	    	instance = new ConnectionProperties(resource);
	    }
	    catch(MissingResourceException missingresourceexception)
	    {
	    	instance = null;
	    }
        return instance;
    }
    
    public String getLookup()
    {
        return lookupName;
    }

    public String getDriver()
    {
        return driver;
    }

    public String getUrl()
    {
        return url;
    }
    
    public String getUrlCatalog()
    {
        return url_catalog;
    }

    public String getUser()
    {
        return user;
    }

    public String getPassword()
    {
        return password;
    }

    public String getDbserver()
    {
        return dbserver;
    }

    public boolean isUseLookup()
    {
        return useLookup;
    }
    
    public boolean isUseConnectionPool()
    {
        return "true".equals(useConnectionPool) ? true : false;
    }    

    
	public void read() {
		useLookup = true;
		readLookup();
		if ( (lookupName == null) || (lookupName.equals("")) ) {
			useLookup = false;
			readDriver();
			readUrl();
			readUser();
			readPassword();
			readUseConnectionPool();
			
			readUrlCatalog();
		}
	} 
	
	private void readUseConnectionPool() {
		try {
			useConnectionPool = rb.getString("ConnectionPool");
		} 
		catch ( MissingResourceException ex ) 
		{ 
			//Log.print(ex.getMessage()); 
			useConnectionPool = "false";
		}
	}
	
	private void readLookup() {
		try {
			lookupName = rb.getString("lookupname");
		} 
		catch ( MissingResourceException ex ) 
		{ 
			//Log.print(ex.getMessage()); 
		}
	}	   

    private void readDriver()
    {
        try
        {
            driver = rb.getString("driver");
        }
        catch(MissingResourceException missingresourceexception)
        {
            //Log.print(missingresourceexception.getMessage());
        }
    }

    private void readUrl()
    {
        try
        {
            url = rb.getString("url");
        }
        catch(MissingResourceException missingresourceexception)
        {
            //Log.print(missingresourceexception.getMessage());
        }
    }
    
    private void readUrlCatalog()
    {
        try
        {
            url_catalog = rb.getString("url_catalog");
        }
        catch(MissingResourceException missingresourceexception)
        {
            //Log.print(missingresourceexception.getMessage());
        }
    }

    private void readUser()
    { 
        try
        {
            user = rb.getString("user");
        }
        catch(MissingResourceException missingresourceexception)
        {
            //Log.print(missingresourceexception.getMessage());
        }
    }

    private void readPassword() {
        try
        {
            password = rb.getString("password");
        }
        catch(MissingResourceException missingresourceexception)
        {
            //Log.print(missingresourceexception.getMessage());
        }
    }
    
    

}
