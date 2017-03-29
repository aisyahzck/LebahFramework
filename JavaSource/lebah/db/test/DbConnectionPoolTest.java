package lebah.db.test;

import lebah.db.*;

import java.sql.*;
import java.util.*;

public class DbConnectionPoolTest {

	/*
	 * This program is a Proof on Concept for connection pooling.
	 * 
	 * Modify the ConnectionPool value in the dbconnection.properties
	 * First, set it to false, ConnectionPool=false,
	 * and run this program, and see how much time it tooks to finished.
	 * 
	 * Then, set it to true, ConnectionPool=true
	 * and run this program, then compare the result.
	 * 
	 * You can see that, how much faster it is when the connection pooling was enabled!
	 */
	
	public static void main(String[] args) throws Exception {
		int count = 10000;
		long start = System.currentTimeMillis();
		for ( int i=0; i < count; i++ ) {
			Db db = null;
			try {
				db = new Db();
				System.out.println(i +") " + db.getConnection());
				String sql ="select count(*) from student_subject";
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) {
					int total = rs.getInt(1);
				}
			} finally {
				if ( db != null ) db.close();
			}
		}
		long elapsedTimeMillis = System.currentTimeMillis() - start;
		float elapsedTimeSecond = elapsedTimeMillis/1000f;
		System.out.println("Elapsed Time = " + elapsedTimeSecond + " seconds");
	}

}
