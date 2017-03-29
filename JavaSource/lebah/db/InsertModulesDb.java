package lebah.db;

import lebah.db.*;

import java.sql.*;
import java.io.*;

public class InsertModulesDb {
	
	public static void main(String[] args) throws Exception {
		String fileName ="e:/meccax.txt";
		create(fileName);
	}
	
	public static void create(String fileName) throws Exception {
		BufferedReader reader = null;
		String data = "";
		Db db = null;
		try {
			db = new Db();
			delete(db);
			reader = new BufferedReader(new FileReader(fileName));
			while ( ( data = reader.readLine() ) != null ) {
				System.out.println(data);
				insert(db, data);
				createRole(db, data, "root");
				createRole(db, data, "admin");
				createRole(db, data, "academic_affair");
			}
		} catch ( IOException e ) {
			System.out.println( e.getMessage() );
		} finally {
			if ( db != null ) db.close();
			try {
				if ( reader != null ) reader.close();
			} catch ( IOException e ) {}
		}
	}
	
	static void insert(Db db, String module) throws Exception {
		String sql = "";
		{
			sql ="INSERT INTO module (module_id, module_title, module_class, module_group) VALUES " + 
			"('" + module + "', " +
			"'" + module + "', " +
			"'" + module + "', "+
			"'X')";
			db.getStatement().executeUpdate(sql);
		}
	}

	private static void createRole(Db db, String module, String role) throws SQLException {
		String sql;
		boolean found = false;
		{
			sql ="SELECT module_id FROM role_module WHERE module_id = " + 
			"'" + module + "' AND user_role = '" + role + "'";
			ResultSet rs = db.getStatement().executeQuery(sql);
			if ( rs.next() ) found = true;
		}
		if ( !found ){
			sql ="INSERT INTO role_module (module_id, user_role) VALUES " + 
			"('" + module + "', " +
			"'" + role + "')";
			db.getStatement().executeUpdate(sql);
		}
	}
	
	static void delete(Db db) throws Exception {
		String sql = "DELETE FROM module WHERE module_group = 'X'";
		db.getStatement().executeUpdate(sql);
	
	}

}
