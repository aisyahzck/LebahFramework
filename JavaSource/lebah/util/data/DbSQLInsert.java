package lebah.util.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import lebah.db.Db;
import lebah.db.SQLRenderer;

public class DbSQLInsert {
	
	static String outfile = "sql/insert_new.sql";

	public static void main(String[] args) throws Exception {
		String catalog = "lebah_db";
		String[] tables = {
				"module",
				"module_htmlcontainer",
				"nationality_code",
				"page_css",
				"race_code",
				"religion_code",
				"role",
				"role_module",
				"state_code",
				"tab_template",
				"tab_user",
				"tabs",
				"user_css",
				"user_module",
				"user_module_template",
				"user_role",
				"users",
				"users_data"
				};
		
		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new FileWriter(outfile));
			for ( String table : tables ) {
				out.write("DELETE FROM " + table + ";\n");
				Vector<String> sqlv = getInsertSQL(catalog, table, "");
				for ( String sql : sqlv ) {
					System.out.println(sql);
					out.write(sql + "\n");
				}
			}
		} finally {
			if ( out != null ) out.close();
		}
	}
	
	static Vector<String> getInsertSQL(String catalog, String table, String limit_size) throws Exception {
		
		Vector<Hashtable> columns = getColumns(catalog, table);
		
		Db db = null;
		String sql = "";
		try {
			
			db = new Db();
			Statement stmt = db.getStatement();
            Vector<String> data = new Vector<String>();	
         
            SQLRenderer r = new SQLRenderer();
            String sqli = "";
            sql = "select ";
            for ( int i=0; i < columns.size(); i++ ) {
	            if ( i > 0 ) sql += ", ";
	            sql += columns.elementAt(i).get("name");
            }
            sql += " from " + table;
            if ( !"".equals(limit_size) ) sql += " limit " + limit_size;
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	r.reset();
	            for ( int i=0; i < columns.size(); i++ ) {
	            	String column_name = (String) columns.elementAt(i).get("name");
					String column_type = (String) columns.elementAt(i).get("type_name");
					if ( "varchar".equals(column_type)
						||	"text".equals(column_type)
						||	"datetime".equals(column_type)
						||	"date".equals(column_type)
							
					) {
						r.add(column_name, Db.getString(rs, column_name));
					}
					else if ("int".equals(column_type) 
							 || "bigint".equals(column_type)) {
						r.add(column_name, rs.getInt(column_name));
					}
					else if ("float".equals(column_type)) {
						r.add(column_name, rs.getFloat(column_name));
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
	
	static Vector<Hashtable> getColumns(String catalog, String table) throws Exception {
		Db db = null;
		try {
			db = new Db();
			Connection conn = db.getConnection();
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getColumns(catalog, "", table, "");
            Vector<Hashtable> columns = new Vector<Hashtable>();	
            while ( rs.next() ) {
	            Hashtable<String, String> h = new Hashtable<String, String>();
	            String column_name = rs.getString("COLUMN_NAME");
	            h.put("name", column_name);
	            h.put("type", rs.getString("DATA_TYPE"));
	            h.put("size", rs.getString("COLUMN_SIZE"));
	            h.put("type_name", rs.getString("TYPE_NAME"));
	            columns.addElement(h);
            }
			return columns;		
		} finally {
			if ( db != null ) db.close();
		}
		
	}

}
