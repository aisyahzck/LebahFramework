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

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lebah.db.Database;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class CompareDb {
    
    public static void main(String[] args) throws Exception {

        compare();
    }
    
    public static void compare() throws Exception {
        
        Database db1 = null; //database to be based on
        Database db2 = null; //database to be modified
        
        try {
        	Hashtable xmlInfo = readXML("compareDb.xml");
        	String dbFromDriver = (String) xmlInfo.get("dbFromDriver");
        	String dbFromUrl = (String) xmlInfo.get("dbFromUrl");
        	String dbFromUser = (String) xmlInfo.get("dbFromUser");
        	String dbFromPassword = (String) xmlInfo.get("dbFromPassword");
        	String dbToDriver = (String) xmlInfo.get("dbToDriver");
        	String dbToUrl = (String) xmlInfo.get("dbToUrl");
        	String dbToUser = (String) xmlInfo.get("dbToUser");
        	String dbToPassword = (String) xmlInfo.get("dbToPassword");    	
        	
        	System.out.println(dbFromUrl + " > " + dbToUrl);
        	
            db1 = new Database(dbFromDriver, dbFromUrl, dbFromUser, dbFromPassword);
            db2 = new Database(dbToDriver,dbToUrl, dbToUser, dbToPassword);
            
            doCompare(db1, db2);
            
        } finally {
            if ( db1 != null ) db1.close();
            if ( db2 != null ) db2.close();
        }
        
    }

    private static void doCompare(Database db1, Database db2) throws Exception {
        Vector t1 = getTables(db1);
        Vector t2 = getTables(db2);
        
        for ( int t=0; t < t1.size(); t++ ) {
            String table = (String) t1.elementAt(t);
            if ( t2.contains(table)) {
                doAlterTable(db1, db2, table);        
            } else {
                Vector n1 = new Vector();
                Hashtable s1 = new Hashtable();
                getColumns(db1, table, n1, s1);
                doCreateTable(db2, table, n1, s1);
            }
        }
    }

    private static void doCreateTable(Database db2, String table, Vector n1, Hashtable s1) throws Exception {
        String sql = "";
        sql = "CREATE table " + table + " ( ";
        for ( int n=0; n < n1.size(); n++) {
            
            Hashtable h = (Hashtable) s1.get((String) n1.elementAt(n));
            String col_name = (String) h.get("name");
            String col_type = (String) h.get("type_name");
            String col_size = (String) h.get("size");
            sql += " `" + col_name + "` ";
            if ( "varchar".equalsIgnoreCase(col_type) ) {
                sql += "varchar(" + col_size + ") ";
            } else {
                sql += col_type;
            }
            if (n < n1.size()-1) sql += ", ";
        }
        sql += ")";
        
        db2.getStatement().executeUpdate(sql);
    }

    private static void doAlterTable(Database db1, Database db2, String table) throws Exception {
        Vector n1 = new Vector();
        Hashtable s1 = new Hashtable();
        getColumns(db1, table, n1, s1);
        
        Vector n2 = new Vector();
        Hashtable s2 = new Hashtable();
        getColumns(db2, table, n2, s2);            
        
        for ( int i=0; i < n1.size(); i++ ) {
            String name = (String) n1.elementAt(i);
            if ( !n2.contains(name)) {
                Hashtable h = (Hashtable) s1.get(name);
                String type = (String) h.get("type_name");
                String size = (String) h.get("size");
                String sql = "ALTER TABLE " + table + " ADD `" + name + "` ";
                if ( "varchar".equalsIgnoreCase(type) ) {
                    sql += "varchar(" + size + ")"; 
                } else {
                    sql += type;
                }
                //-
                db2.getStatement().executeUpdate(sql);
            }
        }
    }
    
    static Vector getTables(Database db) throws Exception {
        Connection conn = db.getConnection();
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, "", "", null);
        Vector tables = new Vector();   
        while ( rs.next() ) {
            tables.addElement(rs.getString("TABLE_NAME"));
        }
        return tables;      
    }

    static void getColumns(Database db, String table, Vector names, Hashtable structures) throws Exception {
        Connection conn = db.getConnection();
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getColumns(null, null, table, "");
        while ( rs.next() ) {
            Hashtable h = new Hashtable();
            String column_name = rs.getString("COLUMN_NAME");
            h.put("name", column_name);
            h.put("type", rs.getString("DATA_TYPE"));
            h.put("size", rs.getString("COLUMN_SIZE"));
            h.put("type_name", rs.getString("TYPE_NAME"));
            names.addElement(column_name);
            structures.put(column_name, h);
        }
    }
    
	public static Hashtable readXML(String xmlFileName) {
	    Hashtable info = new Hashtable();
	    File file = new File(xmlFileName);
	    try {
	        DocumentBuilder builder =  DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.parse(file);
	        
	        NodeList database = doc.getElementsByTagName("database");
	        for (int i = 0; i < database.getLength(); i++) {
	            Element element = (Element) database.item(i);
	            NodeList fromList = element.getElementsByTagName("from");
	            Element line = (Element) fromList.item(0);
	            String driver = line.getAttribute("driver");
	            String url = line.getAttribute("url");
	            String user = line.getAttribute("user");
	            String password = line.getAttribute("password");
	            
	            
	            info.put("dbFromDriver", driver);
	            info.put("dbFromUrl", url);
	            info.put("dbFromUser", user);
	            info.put("dbFromPassword", password);
	            
	            NodeList to = element.getElementsByTagName("to");
	            line = (Element) to.item(0);
	            driver = line.getAttribute("driver");
	            url = line.getAttribute("url");
	            user = line.getAttribute("user");
	            password = line.getAttribute("password");
	            
	            info.put("dbToDriver", driver);
	            info.put("dbToUrl", url);
	            info.put("dbToUser", user);
	            info.put("dbToPassword", password);
	        }	        
	        
	        return info;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	} 
	
	static Hashtable<String, String> setDatabaseInfo() throws Exception {
		Hashtable<String, String> info = new Hashtable<String, String>();
		String driver = "", url = "", user = "", password = "";
        info.put("dbFromDriver", driver);
        info.put("dbFromUrl", url);
        info.put("dbFromUser", user);
        info.put("dbFromPassword", password);	
        info.put("dbToDriver", driver);
        info.put("dbToUrl", url);
        info.put("dbToUser", user);
        info.put("dbToPassword", password);    
        return info;
	}
    
}
