package lebah.db.test;


import lebah.db.*;

import java.sql.*;
import java.util.*;



public class DbConnectionPoolTest2 implements Runnable {
	
	private String name;
	private int count = 100;
	
	public DbConnectionPoolTest2(String name, int count) {
		this.name = name;
		this.count = count;
	}
	
	public String getName(){
		return name;
	}
	
	public void run() {
		try {
			doTest();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		
		Thread t1 = new Thread(new DbConnectionPoolTest2("Thread 1", 50));
		Thread t2 = new Thread(new DbConnectionPoolTest2("Thread 2", 100));
		Thread t3 = new Thread(new DbConnectionPoolTest2("Thread 3", 200));
		Thread t4 = new Thread(new DbConnectionPoolTest2("Thread 4", 200));
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		

	}
	
	void doTest() throws Exception {
		long start = System.currentTimeMillis();
		for ( int i=0; i < count; i++ ) {
			Db db = null;
			try {
				db = new Db();
				System.out.println(name + " - " + i +") " + db.getConnection());
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
		System.out.println(name + " - Elapsed Time = " + elapsedTimeSecond + " seconds");
	}

}
