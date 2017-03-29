package lebah.test;

import java.sql.*;
import java.util.StringTokenizer;

import lebah.db.*;
import java.io.*;

public class TestDb {
	
	static String sqlfile = "sql/create_table_hsql.sql";
	
	public static void main(String[] args) throws Exception {
		Db db = null;
		try{
			BufferedReader in = new BufferedReader(new FileReader(sqlfile));
			db = new Db();
            String str;
            while ((str = in.readLine()) != null) {
            	System.out.println(str);
            	db.getStatement().execute(str);
            }
            in.close();			
			
			
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	

}
