/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.app;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.DataHelper;
import lebah.db.Db;
import lebah.db.SQLPStmtRenderer;
import lebah.db.SQLRenderer;

public class ForumData {
	
	public static Hashtable getTopicCount(String[] catIds) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			r
			.add("count(category_id) as cnt")
			.add("parent_id", "0")
			.add("category_id", "")
			.add("is_delete", 0)
			;
			r.getPStmtSelect(db.getConnection(), "forum");
			ResultSet rs = null;
			Hashtable h = new Hashtable();
			for ( int i=0; i < catIds.length; i++ ) {
				
				System.out.println(catIds[i]);
				
				r
				.set("category_id", catIds[i])	
				.set("parent_id", "0")
				;
				rs = r.getPStmt().executeQuery();
				if ( rs.next() ) {
					h.put(catIds[i], new Integer(rs.getInt(1) - 1));
				}
			}
			return h;
		} finally {
			if ( db != null ) db.close();
		}
		

	}
	
	public static Hashtable getReplyCount(Vector list) throws Exception {
		String[] as = new String[list.size()];
		for ( int i=0; i < list.size(); i++ ) {
			Forum f = (Forum) list.elementAt(i);
			as[i] = f.getId();
		}
		return getReplyCount(as);
	}
	
	public static Hashtable getReplyCount(String[] pIds) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			r
			.add("count(parent_id) as cnt")
			.add("parent_id", "")
			.add("is_delete", 0)
			;
			r.getPStmtSelect(db.getConnection(), "forum");
			ResultSet rs = null;
			Hashtable h = new Hashtable();
			for ( int i=0; i < pIds.length; i++ ) {
				r
				.set("parent_id", pIds[i])	
				;
				rs = r.getPStmt().executeQuery();
				if ( rs.next() ) {
					h.put(pIds[i], new Integer(rs.getInt(1)));
				}
			}
			return h;
		} finally {
			if ( db != null ) db.close();
		}
	}	
	
	public static void getPostingCount(String classroom_id, String user_id) throws Exception {
		//for each lessons in the classroom
		Db db = null;
		try {
			db = new Db();
			//get lesson_ids in this classroom
			Vector<String> lessons = new Vector<String>();
			{
				String sql = "select lesson_id from lesson where subject_id = '" + classroom_id + "'";
				ResultSet rs = db.getStatement().executeQuery(sql);
				while ( rs.next() ) {
					String lesson_id = rs.getString(1);
					lessons.addElement(lesson_id);
				}
			}
			
			for ( String lesson_id : lessons ) {
				String sql = "select count(*) as posting, sum(rate) as rating " +
						"from forum where category_id = '" + "for_" + lesson_id + "'" +
						"and is_delete = 0 " +
						"and member_id = '" + user_id + "'";
				
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) {
					int posting = rs.getInt("posting");
					int rating = rs.getInt("rating");
					System.out.println("lesson = " + lesson_id + ", posting = " + posting + ", rating = " + rating);
				}
				
				
			}
			
		} finally {
			if ( db != null ) db.close();
		}
	}

}
