/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.db.UniqueID;
import lebah.util.DateTool;

public class ScheduleData {
    
    public static void addEvent(String subjectId, Hashtable s) throws Exception {
        s.put("subjectId", subjectId);
        doEvent("add", s);
    }

    public static void updateEvent(String subjectId, Hashtable s) throws Exception {
        s.put("subjectId", subjectId);
        doEvent("update", s);
    }

    public static void doEvent(String action, Hashtable s) throws Exception {
        String subjectId = (String) s.get("subjectId");
        String year1 = (String) s.get("year1");
        String month1 = (String) s.get("month1");
        String day1 = (String) s.get("day1");
        String year2 = (String) s.get("year2");
        String month2 = (String) s.get("month2");
        String day2 = (String) s.get("day2");     
        
        String start_date = DateTool.getDateStr(year1, month1, day1);
        String end_date = DateTool.getDateStr(year2, month2, day2);
        
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            
            r.add("subject_id", subjectId);
            r.add("start_date", start_date);
            r.add("end_date", end_date);
            r.add("start_date_year", Integer.parseInt(year1));
            r.add("start_date_month", Integer.parseInt(month1));
            r.add("start_date_day", Integer.parseInt(day1));
            r.add("end_date_year", Integer.parseInt(year2));
            r.add("end_date_month", Integer.parseInt(month2));
            r.add("end_date_day", Integer.parseInt(day2));
            
            if ( "add".equals(action) ) {
                String id = Long.toString(UniqueID.get());
                r.add("schedule_id", id);
                sql = r.getSQLInsert("subject_schedule");
            }
            else if ( "update".equals(action) ) {
                String id = (String) s.get("id");
                r.update("schedule_id", id);
                sql = r.getSQLUpdate("subject_schedule");
            }
            
            stmt.executeUpdate(sql);
            
        } finally {
            if ( db != null ) db.close();
        }
                
    }  
    
    public static Vector getList(String session_id, String direct) throws Exception {
        Db db = null;
        String sql = "";
        Vector v = new Vector();
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            String session_start_date = "";
            if ( !"".equals(session_id) ) {
                r.add("start_date");
                r.add("session_id", session_id);
                sql = r.getSQLSelect("sessions");
                
                //-
                
                ResultSet rs = stmt.executeQuery(sql);
                
                if ( rs.next() ) {
                    session_start_date = lebah.util.DateTool.getDateStr(rs.getDate("start_date"));
                }
            }
            
            {
                r.clear();
                r.add("session_id");
                r.add("session_name");
                
                r.add("start_date");
                r.add("end_date");
                
                r.add("session_code");
                
                
                if ( !"".equals(session_start_date)) {
                    r.add("start_date", session_start_date, ">=");  
                }
                
                if ( "desc".equals(direct))
                    sql = r.getSQLSelect("sessions", "start_date desc");
                else
                    sql = r.getSQLSelect("sessions", "start_date asc");
                
                //-
                ResultSet rs = stmt.executeQuery(sql);
                
                while ( rs.next() ) {
                    Hashtable h = new Hashtable();
                    h.put("id", rs.getString("session_id"));
                    h.put("name", rs.getString("session_name"));
                    h.put("code", Db.getString(rs, "session_code"));
                    
                    java.util.Date start_date = Db.getDate(rs, "start_date");
                    java.util.Date end_date = Db.getDate(rs, "end_date");

                    Calendar c = new java.util.GregorianCalendar();
                    c.setTime(start_date);  
                    int year1 = c.get(Calendar.YEAR);
                    int month1 = c.get(Calendar.MONTH) + 1;
                    int day1 = c.get(Calendar.DAY_OF_MONTH);
                    
                    c.setTime(end_date);    
                    int year2 = c.get(Calendar.YEAR);
                    int month2 = c.get(Calendar.MONTH) + 1;
                    int day2 = c.get(Calendar.DAY_OF_MONTH);

                    h.put("start_date_year", new Integer(year1));
                    h.put("start_date_month", new Integer(month1));
                    h.put("start_date_day", new Integer(day1));
                    h.put("end_date_year", new Integer(year2));
                    h.put("end_date_month", new Integer(month2));
                    h.put("end_date_day", new Integer(day2));
                    h.put("start_date", lebah.util.DateTool.getDateStr(start_date));
                    h.put("end_date", lebah.util.DateTool.getDateStr(end_date));
                    
                    v.addElement(h);
                }
            }
            return v;
            
        } finally {
            if ( db != null ) db.close();
            
        }   
    }

}
