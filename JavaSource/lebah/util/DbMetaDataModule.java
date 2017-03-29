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

import java.sql.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.db.ConnectionProperties;
import lebah.db.ConnectionSource;
import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;
import lebah.portal.Attributable;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class DbMetaDataModule extends lebah.portal.velocity.VTemplate implements Attributable {
	 
	protected String[] names = {"driver", "url", "user", "password"};
	protected Hashtable values = new Hashtable();	
	//protected Connection conn;
	//protected Statement stmt;
	protected String driver = "", url = "", user = "", password = "", url_catalog = "";
	
    public DbMetaDataModule() throws DbException {
//        try
//        {
//            ConnectionProperties connectionproperties = ConnectionProperties.getInstance("dbconnection");
//            if(connectionproperties != null)
//            {
//                connectionproperties.read();
//				if ( connectionproperties.isUseLookup() ) {
//					String lookupName = connectionproperties.getLookup();
//					//conn = ConnectionSource.getInstance(lookupName).getConnection();
//				}
//				else
//				{                
//	                driver = connectionproperties.getDriver();
//	                url = connectionproperties.getUrl();
//	                user = connectionproperties.getUser();
//	                password = connectionproperties.getPassword();
//	                url_catalog = connectionproperties.getUrlCatalog();
//	                //if does not specifically defined pooled as true then get pooled status from properties
//	                boolean pooled = connectionproperties.isUseConnectionPool();
//	                
//	                //conn = ConnectionSource.getInstance(driver, url, user, password, pooled).getConnection();
//                }
//               
//				//conn.setAutoCommit(true);
//                //stmt = conn.createStatement();
//            } else
//            {
//                throw new DbException("Db : dbconnection.properties file error");
//            }
//        } finally{
//        	
//        }
    	
    	
    	
        driver = "org.hsqldb.jdbcDriver";
        url = "jdbc:hsqldb:file:data/portaldb";
        user = "sa";
        password = "";
    	
    	
    	
    }
	
	public String[] getNames() {
		return names;
	}
	
	public java.util.Hashtable getValues() {
		return values;
	}
	
	public void setValues(java.util.Hashtable hashtable) {
		values = hashtable;
	}
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		String template_name = "vtl/dba/dbmetadata.vm";
		String submit = getParam("command");
		if ( "".equals(submit) ) submit = "tables";
		
		String catalog_name = !"".equals(getParam("catalog")) ? getParam("catalog") : "";

        String driver2 = "org.hsqldb.jdbcDriver";
        String url2 = "jdbc:hsqldb:file:data/portaldb";
        String user2 = "sa";
        String password2 = "";

	
//		String driver2 = values.get("driver") != null ? (String) values.get("driver") : driver;
//		String url2 = (values.get("url") != null ? (String) values.get("url") : url_catalog) + catalog_name;
//		String user2 = values.get("user") != null ? (String) values.get("user") : user;
//		String password2 = values.get("password") != null ? (String) values.get("password") : password;
//		

		
		Hashtable prop = new Hashtable();
		prop.put("driver", driver2);
		prop.put("url", url2);
		prop.put("user", user2);
		prop.put("password", password2);
		
	
		context.put("jdbc_driver", driver2);
		context.put("jdbc_url", url2);
		
		System.out.println("submit=" + submit);
		
		if ( "addtable".equals(submit) ) {
			submit = "tables";
			String table_name = getParam("table_name");
			String column_name = getParam("column_name");
			String column_type = getParam("column_type");			
			addTable(prop, table_name, column_name, column_type);
		}		
		else if ( "droptable".equals(submit) ) {
			submit = "tables";
			String table_name = getParam("table_name");
			dropTable(prop, table_name);
		}		
		else if ( "addcolumn".equals(submit) ) {
			submit = "columns";
			String table_name = getParam("table");
			String column_name = getParam("column_name");
			String column_type = getParam("column_type");
			addColumn(prop, table_name, column_name, column_type);
		}		
		else if ( "dropcolumn".equals(submit) ) {
			submit = "columns";
			String table_name = getParam("table");
			String column_name = getParam("column_name");
			dropColumn(prop, table_name, column_name);
		}
		else if ("insertdata".equals(submit) ) {
			submit = "viewdata";
			String[] colnames = request.getParameterValues("colnames");
			String table_name = getParam("table");
			String catalog = getParam("catalog");
			Vector columns = getCheckedColumns(prop, catalog, table_name, colnames);
			insertData(prop, table_name, columns);
			
		}		
		else if ("deletedata".equals(submit) ) {
			submit = "viewdata";
			
			String table_name = getParam("table");
			String catalog = getParam("catalog");
			String del_value = getParam("del_value");
			String del_col = getParam("del_col");
			deleteData(prop, table_name, del_col, del_value);
		}		
		
		//----
		if ( "catalogs".equals(submit) ) {
			template_name = "vtl/dba/list_catalogs.vm";
			Vector catalogs = getCatalogs(prop);
			context.put("catalogs", catalogs);
		}
		else if ( "tables".equals(submit) ) {
			template_name = "vtl/dba/list_tables.vm";
			String catalog = getParam("catalog");
			Vector tables = getTables(prop, catalog);
			context.put("catalog", catalog);
			context.put("tables", tables);
		}
		else if ( "columns".equals(submit) ) {
			template_name = "vtl/dba/list_columns.vm";
			String table = getParam("table");
			String noRecs = countRecs(table, prop); //red1
			String catalog = getParam("catalog"); 
			Vector columns = null;
			
			String[] colnames = request.getParameterValues("colnames");
			if ( colnames != null ) {
				columns = getColumns(prop, catalog, table, colnames);
			} else {
				columns = getColumns(prop, catalog, table, null);
			}
			context.put("catalog", catalog);
			context.put("table", table);
			context.put("columns", columns);
			context.put("noRecs", noRecs); //red1
		}	
		else if ( "sql".equals(submit) ) {
			template_name = "vtl/dba/sql.vm";
			String table = getParam("table");
			String catalog = getParam("catalog");
			String targetdb = getParam("targetdb");
			context.put("targetdb", targetdb);
			Vector columns = null;
			
			String[] colnames = request.getParameterValues("colnames");
			if ( colnames != null ) {
				columns = getColumns(prop, catalog, table, colnames);
			} else {
				columns = getColumns(prop, catalog, table, null);
			}
			String sql = sqlCreateTable(targetdb, table, columns);
			context.put("catalog", catalog);
			context.put("table", table);
			context.put("columns", columns);
			context.put("sql", sql);
		}	
		else if ( "sqltable".equals(submit) ) {
			template_name = "vtl/dba/sql.vm";
			String catalog = getParam("catalog");
			Vector tables = getTables(prop, catalog);
			String targetdb = getParam("targetdb");
			String sql = "";
			String table = "";
			for ( int i=0; i < tables.size(); i++ ) {
				Hashtable h = (Hashtable) tables.elementAt(i);
				table = (String) h.get("name");
				Vector columns = getColumns(prop, catalog, table, null);
				sql += sqlCreateTable(targetdb, table, columns);
			}
			sql += "\n\n";
			
			context.put("catalog", catalog);
			context.put("table", table);
			context.put("sql", sql);
		}			
		else if ( "viewdata".equals(submit) ) {
			template_name = "vtl/dba/view_data.vm";
			String table = getParam("table");
			String catalog = getParam("catalog");
			String[] colnames = request.getParameterValues("colnames");
			String orderBy = getParam("orderBy");
			String limit_size = getParam("limit_size");
			Vector data = getData(orderBy, prop, catalog, table, colnames, limit_size);
			context.put("colnames", colnames);
			context.put("catalog", catalog);
			context.put("table", table);
			context.put("data", data);
		}	
		else if ( "createInsertSQL".equals(submit)) {
			template_name = "vtl/dba/sql_insert.vm";
			String targetdb = getParam("targetdb");
			String table = getParam("table");
			String catalog = getParam("catalog");
			String[] colnames = request.getParameterValues("colnames");
			String orderBy = getParam("orderBy");
			String limit_size = getParam("limit_size");
			Vector sqldata = getInsertSQL(orderBy, prop, catalog, table, colnames, limit_size);
			context.put("sqldata", sqldata);
		}

		Template template = engine.getTemplate(template_name);	
		return template;
	}
	
	String sqlCreateTable(String targetdb, String table, Vector columns) {

		
		String sql = "";
		sql = "DROP TABLE IF EXISTS " + table + ";\n";
		sql += "CREATE TABLE " + table + " (";
		for ( int i=0; i < columns.size(); i++ ) {
			Hashtable h = (Hashtable) columns.elementAt(i);
			String name = (String) h.get("name");
			String type = (String) h.get("type_name");
			String size = (String) h.get("size");
			sql += " " + name + " ";
			if ( "varchar".equalsIgnoreCase(type) ) {
				sql += "varchar(" + size + ")";	
			} else if ( "hsql".equals(targetdb) && "text".equalsIgnoreCase(type)) {
				sql += "varchar(" + Integer.MAX_VALUE + ")";	
			} else if ( "hsql".equals(targetdb) && "mediumtext".equalsIgnoreCase(type)) {
				sql += "varchar(" + Integer.MAX_VALUE + ")";
			} else if ( "hsql".equals(targetdb) && "longblob".equalsIgnoreCase(type)) {
				sql += "int";				
			} else {
				sql += type;
			}
			if ( i < columns.size() - 1 ) sql += ", ";
		}
		sql += ");\n";
		
		return sql;
	}	
	

	Vector getCatalogs(Hashtable prop) throws Exception {
		Db db = null;
		try {
			db = new Db(prop);
			Connection conn = db.getConnection();
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getCatalogs();
			Vector catalogs = new Vector();
			while ( rs.next() ) {
				catalogs.addElement( rs.getString("TABLE_CAT") );
			}
			return catalogs;		
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	Vector getTables(Hashtable prop, String catalog) throws Exception {
		Db db = null;
		try {
			db = new Db(prop);
			Connection conn = db.getConnection();
			DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(catalog, "", "", null);		
            Vector tables = new Vector();	
            while ( rs.next() ) {
	            Hashtable h = new Hashtable();
	            h.put("name", rs.getString("TABLE_NAME"));
	            h.put("type", rs.getString("TABLE_TYPE"));
	            tables.addElement(h);
            }
			return tables;		
		} finally {
			if ( db != null ) db.close();
		}
		
	}
	
	Vector<Hashtable> getColumns(Hashtable prop, String catalog, String table, String[] colnames) throws Exception {
		Db db = null;
		try {
			db = new Db(prop);
			Connection conn = db.getConnection();
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getColumns(catalog, "", table, "");
            Vector<Hashtable> columns = new Vector<Hashtable>();	
            List list = null;
            if ( colnames != null ) list = Arrays.asList(colnames);
            while ( rs.next() ) {
	            Hashtable<String, String> h = new Hashtable<String, String>();
	            String column_name = rs.getString("COLUMN_NAME");
	            h.put("name", column_name);
	            h.put("type", rs.getString("DATA_TYPE"));
	            h.put("size", rs.getString("COLUMN_SIZE"));
	            h.put("type_name", rs.getString("TYPE_NAME"));
	            h.put("check", "");
	            if ( colnames != null && list.contains(column_name) ) {
	            	h.put("check", "checked");
            	}
	            columns.addElement(h);
            }
			return columns;		
		} finally {
			if ( db != null ) db.close();
		}
		
	}	
	
	Vector getCheckedColumns(Hashtable prop, String catalog, String table, String[] colnames) throws Exception {
		Db db = null;
		try {
			db = new Db(prop);
			Connection conn = db.getConnection();
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getColumns(catalog, "", table, "");
            Vector columns = new Vector();	
            List list = null;
            if ( colnames != null ) list = Arrays.asList(colnames);
            while ( rs.next() ) {
	            Hashtable h = new Hashtable();
	            String column_name = rs.getString("COLUMN_NAME");
	            if ( colnames != null && list.contains(column_name) ) {
		            h.put("name", column_name);
		            h.put("type", rs.getString("DATA_TYPE"));
		            h.put("size", rs.getString("COLUMN_SIZE"));
		            h.put("type_name", rs.getString("TYPE_NAME"));
		            h.put("check", "");		            
	            	h.put("check", "checked");
	            	columns.addElement(h);
            	}
            }
			return columns;		
		} finally {
			if ( db != null ) db.close();
		}
		
	}	
	
	Vector getData(String orderBy, Hashtable prop, String catalog, String table, String[] cols, String limit_size) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
            Vector data = new Vector();	
            
            sql = "select ";
            for ( int i=0; i < cols.length; i++ ) {
	            if ( i > 0 ) sql += ", ";
	            sql += cols[i];
            }
            sql += " from " + table;
            if (orderBy != "")  
            sql += " order by " + orderBy + " DESC";
            
            if ( !"".equals(limit_size) ) sql += " limit " + limit_size;
            
            
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
				Hashtable h = new Hashtable();
	            for ( int i=0; i < cols.length; i++ ) {
					h.put(cols[i], Db.getString(rs, cols[i]));
	            }
	            data.addElement(h);
            }
			return data;		
		} finally {
			if ( db != null ) db.close();
		}
		
	}
	
	Vector getInsertSQL(String orderBy, Hashtable prop, String catalog, String table, String[] cols, String limit_size) throws Exception {
		
		Vector<Hashtable> columns = getColumns(prop, catalog, table, cols);
		
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
            Vector data = new Vector();	
         
            SQLRenderer r = new SQLRenderer();
            String sqli = "";
            sql = "select ";
            for ( int i=0; i < cols.length; i++ ) {
	            if ( i > 0 ) sql += ", ";
	            sql += cols[i];
            }
            sql += " from " + table;
            if (orderBy != "")  
            sql += " order by " + orderBy + " DESC";
            if ( !"".equals(limit_size) ) sql += " limit " + limit_size;
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	r.reset();
	            for ( int i=0; i < cols.length; i++ ) {
					String column_type = (String) columns.elementAt(i).get("type_name");
					if ( "varchar".equals(column_type)
						||	"text".equals(column_type)
						||	"datetime".equals(column_type)
						||	"date".equals(column_type)
							
					) {
						r.add(cols[i], Db.getString(rs, cols[i]));
					}
					else if ("int".equals(column_type) 
							 || "bigint".equals(column_type)) {
						r.add(cols[i], rs.getInt(cols[i]));
					}
					else if ("float".equals(column_type)) {
						r.add(cols[i], rs.getFloat(cols[i]));
					}					
					
	            }
	            sqli = r.getSQLInsert(table);
	            data.addElement(sqli + ";");
            }
			return data;
		} finally {
			if ( db != null ) db.close();
		}
		
	}	
	
	void addTable(Hashtable prop, String table_name, String column_name, String column_type) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
			sql = "create table " + table_name;
			sql += " (  ";
			sql += column_name + " " + column_type;
			sql += " not null)";
			stmt.executeUpdate(sql);

		} finally {
			if ( db != null ) db.close();
		}
		
	}		
	
	void dropTable(Hashtable prop, String table_name) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
			sql = "drop table " + table_name;

			stmt.executeUpdate(sql);

		} finally {
			if ( db != null ) db.close();
		}
		
	}		
	
	void addColumn(Hashtable prop, String table_name, String column_name, String column_type) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
			sql = "alter table " + table_name;
			sql += " add ";
			sql += column_name + " " + column_type + " not null";
			stmt.executeUpdate(sql);

		} finally {
			if ( db != null ) db.close();
		}
		
	}
	
	void dropColumn(Hashtable prop, String table_name, String column_name) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
			sql = "alter table " + table_name;
			sql += " drop ";
			sql += column_name + " ";
			stmt.executeUpdate(sql);

		} finally {
			if ( db != null ) db.close();
		}
		
	}
	
	void insertData(Hashtable prop, String table, Vector columns) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			
            for ( int i=0; i < columns.size(); i++ ) {
	            Hashtable col = (Hashtable) columns.elementAt(i);
	            String name = (String) col.get("name");
	            String type = (String) col.get("type_name");
	            String val = getParam(name);
	            if ( "int".equals(type) ) {
		            r.add(name, Integer.parseInt(val) );
	            }
	            else if( "float".equals(type)) {
		            r.add(name, Float.parseFloat(val) );
	            } 
	            else {
	            	r.add(name, val);
            	}
	            
            }
            
            sql = r.getSQLInsert(table);
			stmt.executeUpdate(sql);            
            
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	void deleteData(Hashtable prop, String table, String del_col, String del_val) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db(prop);
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();

			r.add(del_col, del_val);			
            
            sql = r.getSQLDelete(table);
			stmt.executeUpdate(sql);            
            
		} finally {
			if ( db != null ) db.close();
		}
	}	

	//red1 - return count of noRecs to vm.
	String countRecs(String table, Hashtable prop) throws Exception
	{ 
	String count = null;
	Db db = null;
	String sql = "";
	try 
	{
		db = new Db(prop);
		Statement stmt = db.getStatement();
        Vector data = new Vector();	
        
        sql = "select count(*) AS count from "+table;
        
        ResultSet rs = stmt.executeQuery(sql);
        while ( rs.next() ) {
        	count = Db.getString(rs, "count");
        }
		return count;		
	} finally {
		if ( db != null ) db.close();
	}
	

	}
}
