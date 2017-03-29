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
/*
 * Created on Apr 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lebah.log;

import lebah.db.Db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogIPCountModule extends lebah.portal.velocity.VTemplate {
    
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		String template_name = "vtl/admin/ip2country.vm";
		String submit = getParam("command");
		
		Vector ipList = doIPCount();
		context.put("ipList", ipList);
		if ( "saveHit".equals(submit)) {
		    saveCountryHits(ipList);
		}
		
		Template template = engine.getTemplate(template_name);	
		return template; 
	}
    
    static Vector doIPCount() throws Exception {
        IP2Country ip2 = IP2Country.getInstance();
        Db db = null;
        //Db db2 = null;
        Vector v = new Vector();
        try {
            db = new Db();
            //db2 = new Db();
            String sql = "select remote_add, count(*) as cnt from user_tracker " +
            "group by remote_add order by cnt desc";
            Statement stmt = db.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Hashtable h = null;
            while ( rs.next() ) {
                String remote_add = rs.getString("remote_add");
                int cnt = rs.getInt("cnt");
                h = new Hashtable();
                h.put("ip", remote_add);
                h.put("count", new Integer(cnt));
                if ( remote_add != null && !"61.6.67.99".equals(remote_add) && !"127.0.0.1".equals(remote_add)  ) {
                	System.out.println("remote addr = " + remote_add);
                    String country = ip2.getCountryName(remote_add);
	                if ( country != null ) {
	                    h.put("country", country);
	                }
	                else {
	                    h.put("country", "NULL");
	                }
	                v.addElement(h);
                }
            }
            return v;
        } finally {
            if ( db != null) db.close();
            //if ( db2 != null) db2.close();
        }
    }
    
    static void saveCountryHits(Vector v) throws Exception {
        Hashtable h = null;
        Hashtable h2 = new Hashtable();
        for ( int i=0; i < v.size(); i++ ) {
            h = (Hashtable) v.elementAt(i);
            //System.out.println((String) h.get("country"));
            Integer Itotal = (Integer) h2.get((String) h.get("country"));
            Integer Icount = (Integer) h.get("count");
            if ( Itotal == null ) {
                h2.put((String) h.get("country"), Icount);
            } else {
                int total = Itotal.intValue() + Icount.intValue();
                h2.put((String) h.get("country"), new Integer(total));
            }
        }
        //save into database
        Db db = null;
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            stmt.executeUpdate("delete from log_country");
            for ( Enumeration e = h2.keys(); e.hasMoreElements(); ) {
                String country = (String) e.nextElement();
                Integer Itotal = (Integer) h2.get(country);
                //System.out.println(country + " = " + Itotal.intValue());
                
                String sql = "insert into log_country (country_name, count) values ('" + replace(country) + "', " + Itotal.intValue() + ")";
                
                stmt.executeUpdate(sql);
            }            
        } finally {
            if ( db != null ) db.close();
        }

    }
    
    static String dblch(String s, char c)
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

    static String replace(String s)
    {
        return dblch(s, '\'');
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
    
    static String getCountry(Db db, String ip) throws Exception {
        double d = ip2number(ip);
        Statement stmt = db.getStatement();
        String sql = "select country_name from ip2country where " +
        Double.toString(d) + " >= ip_from and " + Double.toString(d) + " <= ip_to";
        ResultSet rs = stmt.executeQuery(sql);
        String country = "";
        if ( rs.next() ) {
            country = rs.getString("country_name");
        }
        return country;
    }
    
    static String getCountry(String ip, Statement stmt) throws Exception {
        double d = ip2number(ip);
        String sql = "select country_name from ip2country where " +
        Double.toString(d) + " >= ip_from and " + Double.toString(d) + " <= ip_to";
        ResultSet rs = stmt.executeQuery(sql);
        String country = "";
        if ( rs.next() ) {
            country = rs.getString("country_name");
        }
        return country;
    }    
    
    public static void main(String[] args) throws Exception {
        //Db db = new Db();
        //String country = getCountry(db, "221.127.101.82");
        //System.out.println(country);
        //if ( db != null ) db.close();
        
        Vector ipList = doIPCount();
        saveCountryHits(ipList);
    }

}
