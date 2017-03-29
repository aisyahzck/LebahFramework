/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
/*
 * Created on Apr 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lebah.log;

import java.sql.ResultSet;
import java.sql.Statement;

import lebah.db.Db;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IP2Country {
    
    private static IP2Country instance = null;
    private static Vector ipList = null;
    
    private IP2Country() {
        getIPList();
    }
    
    public static IP2Country getInstance() {
        if ( instance == null ) {
            instance = new IP2Country();
        }
        return instance;
    }
    
    static void getIPList() {
        Db db = null;
        ipList = new Vector();
        try {
            db = new Db();
            String sql = "select ip_from, ip_to, country_name from ip2country order by ip_from";
            Statement stmt = db.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Hashtable h = null;
            while ( rs.next() ) {
                h = new Hashtable();
                h.put("min", new Double(rs.getDouble("ip_from")));
                h.put("max", new Double(rs.getDouble("ip_to")));
                h.put("country", rs.getString("country_name"));
                ipList.addElement(h);
            }
        } catch ( Exception e ) {
            System.out.println(e);
        } finally {
            if ( db != null) db.close();
        }
    }

    static Vector getIP2CountryList() {
        return ipList;
    }    
    
    static double ip2number(String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".");
        int p = 0;
        double x1 = 0, x2 = 0, x3 = 0, x4 = 0;
        while ( st.hasMoreTokens() ) {
            double i = Double.parseDouble(st.nextToken());
            if ( p == 0 ) {
                x1 = i * (256*256*256);
            }
            else if ( p == 1 ) {
                x2 = i * (256*256);
            }
            else if ( p == 2 ) {
                x3 = i * (256);
            }            
            else if ( p == 3 ) {
                x4 = i;
            }
            p++;
        }
        return x1 + x2 + x3 + x4;
    }    
    
    public static String getCountryName(String ip) throws Exception {
        String country = "";
        double d = ip2number(ip);
        Vector v = getIP2CountryList();
        Hashtable h = null;
        for ( int i=0; i < v.size(); i++ ) {
            h = (Hashtable) v.elementAt(i);
            double min = ((Double) h.get("min")).doubleValue();
            double max = ((Double) h.get("max")).doubleValue();
            country = (String) h.get("country");
            if ( d >= min && d <= max ) {
                break;
            }
        }
        return country;
    }
    
    public static void main(String args[]) throws Exception {
        IP2Country ip2 = IP2Country.getInstance();
        String country = ip2.getCountryName("221.127.101.82");
        System.out.println(country);
    }
}
