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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.0
 */

public abstract class DataHelper {
	
	protected String fieldtxt = "";
	protected String tabletxt = "";
	
	public DataHelper() {
		
	}
	
    //this method must be overridden for single SQL statment
    protected String doSQL() { return null; }
    //this method must be overdidden for multiple SQL statements
    protected String[] doManySQL() { return null; }
	//this method must be overidden if using the getObjectList
	protected Object createObject(ResultSet rs) throws Exception {return null;}
//	this object must be overidden if using the getHashtableList()
	protected Hashtable createHashtable(ResultSet rs) throws Exception {return null; }
	//
	protected void doMappings(ResultSet rs, Hashtable h) throws Exception { } 
	
    public Vector getHashtableList(String[][] fields) throws Exception { 
        String sql = "";
        Db db = null;
        try {
            db = new Db();
            sql = doSQL();
            ResultSet rs = db.getStatement().executeQuery(sql);
            Vector list = new Vector(); 
            while ( rs.next() ) {
                Hashtable h = new Hashtable();
                populateHashtable(fields, rs, h);
                list.addElement(h);
            }
            return list;
        } finally {
            if ( db != null ) db.close();
        }
    }
    
    public Hashtable getHashtable(String[][] fields) throws Exception { 
        String sql = "";
        Db db = null;
        try {
            db = new Db();
            sql = doSQL();
            ResultSet rs = db.getStatement().executeQuery(sql);
            if ( rs.next() ) {
                Hashtable h = new Hashtable();
                populateHashtable(fields, rs, h);
                return h;
            }
            else 
            	return null;
        } finally {
            if ( db != null ) db.close();
        }
    } 
    
    public Vector getHashtableList() throws Exception { 
        String sql = "";
        Db db = null;
        try {
            db = new Db();
            sql = doSQL();
            ResultSet rs = db.getStatement().executeQuery(sql);
            Vector list = new Vector(); 
            while ( rs.next() ) {
                Hashtable h = createHashtable(rs);
                list.addElement(h);
            }
            return list;
        } finally {
            if ( db != null ) db.close();
        }
    }    

	private void populateHashtable(String[][] fields, ResultSet rs, Hashtable h) throws SQLException {
		for ( int i=0; i < fields.length; i++ ) {
		    if ( "string".equals(fields[i][1]))
		        h.put(fields[i][0], rs.getString(fields[i][0]));
		    else if ( "int".equals(fields[i][1]))
		        h.put(fields[i][0], new Integer(rs.getInt(fields[i][0])));
		    else if ( "float".equals(fields[i][1]))
		        h.put(fields[i][0], new Float(rs.getFloat(fields[i][0])));
		    else if ( "date".equals(fields[i][1])) {
                try {
                    h.put(fields[i][0], rs.getDate(fields[i][0]));
                } catch ( SQLException e ) {
                    h.put(fields[i][0], new Date());
                }
            }
		}
	}
	

	
    public Vector getObjectList() throws Exception { 
        String sql = "";
        Db db = null;
        try {
            db = new Db();
            sql = doSQL();
            ResultSet rs = db.getStatement().executeQuery(sql);
            Vector list = new Vector(); 
            while ( rs.next() ) {
                Object object = createObject(rs);
                if ( object != null ) list.addElement(object);
            }
            return list;
        } finally {
            if ( db != null ) db.close();
        }
    }  
    
    public Object getObject() throws Exception { 
        String sql = "";
        Db db = null;
        try {
            db = new Db();
            sql = doSQL();
            ResultSet rs = db.getStatement().executeQuery(sql);
            if ( rs.next() ) {
                return createObject(rs);
            }
            else
            	return null;
        } finally {
            if ( db != null ) db.close();
        }
    } 
    
    public Hashtable getMapping() throws Exception { 
        String sql = "";
        Db db = null;
        try {
            db = new Db();
            sql = doSQL();
            ResultSet rs = db.getStatement().executeQuery(sql);
            Hashtable h = new Hashtable();
            while ( rs.next() ) {
                doMappings(rs, h);
            }
            return h;
        } finally {
            if ( db != null ) db.close();
        }
    }     
    
    public void execute() throws Exception { 
        String sql = "";
        Db db = null;
        try {
            db = new Db();
            sql = doSQL();
            db.getStatement().executeUpdate(sql);
        } finally {
            if ( db != null ) db.close();
        }
    } 	
    
    public void executeMany() throws Exception { 
        String[] sql = null;
        Db db = null;
        try {
            db = new Db();
            sql = doManySQL();
            
            for ( int i=0; i < sql.length; i++ ) {
                db.getStatement().executeUpdate(sql[i]);    
            }
            
        } finally {
            if ( db != null ) db.close();
        }
    } 
    
    public Vector<Hashtable> getSELECT() throws Exception {
    	return getSELECT(fieldtxt);
    }
    
    public Vector<Hashtable> getSELECT(String field) throws Exception { 
    	Vector<String> fields = new Vector<String>();
    	if ( field.indexOf(",") > 0 ) {
    		StringTokenizer tokenizer = new StringTokenizer(field, ",");
    		while ( tokenizer.hasMoreTokens()) {
    			fields.addElement(tokenizer.nextToken().trim());
    		}
    	}
        Db db = null;
        try {
            db = new Db();
            ResultSet rs = db.getStatement().executeQuery(doSQL());
            Vector<Hashtable> list = new Vector<Hashtable>();
            while ( rs.next() ) {
            	Hashtable<String, Object> h = new Hashtable<String, Object>();
            	for ( int i=0; i < fields.size(); i++ ) {
            		Object o = rs.getObject(i+1);
            		if ( o instanceof String ) {
            			h.put(fields.elementAt(i), rs.getString(i+1));
            		} else if( o instanceof Integer) {
            			h.put(fields.elementAt(i), rs.getInt(i+1));
            		} else if( o instanceof Double) {
            			h.put(fields.elementAt(i), rs.getDouble(i+1));
            		} else if( o instanceof Float) {
            			h.put(fields.elementAt(i), rs.getFloat(i+1));
            		} else if( o instanceof Date) {
            			h.put(fields.elementAt(i), rs.getDate(i+1));
            		} else if( o instanceof Boolean) {
            			h.put(fields.elementAt(i), rs.getBoolean(i+1));
            		}
            	}
            	list.addElement(h);
            }
            return list;
        } finally {
            if ( db != null ) db.close();
        }
    } 
    
    public static void executeSQL(final String sql) throws Exception {
		new DataHelper() {
			public String doSQL() {
				return sql;
			}
		}.execute();
    }
    
  
    public static Vector<Hashtable> executeSELECT(final String field, final String table) throws Exception {
		return new DataHelper() {
			public String doSQL() {
				return "SELECT " + field + " FROM " + table;
			}
		}.getSELECT(field);
    }
    
    public static Vector<Hashtable> executeSELECT(final String sql) throws Exception {
		return new DataHelper() {
			public String doSQL() {
				fieldtxt = sql.substring(0, sql.indexOf("FROM")).substring("SELECT".length()+1);;
		    	int ifrom = sql.indexOf(" from ");
		    	if ( ifrom < 0 ) ifrom = sql.indexOf(" FROM ");
		    	int iwhere = sql.indexOf(" where ");
		    	if ( iwhere < 0 ) iwhere = sql.indexOf(" WHERE ");
		    	if ( iwhere < 0 ) 
		    		tabletxt = sql.substring(ifrom + 6);
		    	else
		    		tabletxt = sql.substring(ifrom + 6, iwhere);
		    	
				return "SELECT " + fieldtxt + " FROM " + tabletxt;
			}
		}.getSELECT();
    }    
    
    public static void main(String[] args) throws Exception {
    	Vector<Hashtable> list = DataHelper.executeSELECT("SELECT id, name, price FROM items");
    	for (Hashtable h : list ) {
	    	String id = (String) h.get("id");
	    	String name =  (String) h.get("item_name");
	    	float price = (Float) h.get("item_price");
	    	System.out.println(id + ", " + name + ", " + price);
    	}
    }
    

    
}
