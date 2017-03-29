package edb;

import java.sql.ResultSet;

import lebah.db.Db;

public class Testdb {
	
	public static void main(String[] args) throws Exception {
		
		Db db = null;
		try {
			db = new Db();
			//db.getStatement().execute(SQL.createTabUser);

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		
	}

}
