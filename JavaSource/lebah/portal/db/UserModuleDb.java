/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;
import lebah.portal.element.Module;
import lebah.portal.element.Module2;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.0
 */

public class UserModuleDb {
	
	public static void main(String[] args) {
		String portletsSequence = "lebah_app_RegisterNewModule;lebah_util_RepositoryModule;-;mobile_helloworld;lebah_app_ModuleEditor;lebah_app_RegisterUserModule;-";
		StringTokenizer st = new StringTokenizer(portletsSequence, ";");
		String sql = "";
		String action = "123";
		String usrlogin = "anon";
		String tbl = "tab_user";
		int seq = 0;
		while ( st.hasMoreTokens() ) {
			String s = st.nextToken();
			if ( !"-".equals(s)) {
				sql = "update " + tbl + " set sequence = " + seq + " where module_id = '" + s + "' and tab_id = '" + action + "' and user_login = '" + usrlogin + "' ";
				System.out.println(sql);
			} else {
				seq++;
			}
		}

	}
	
	public static void savePortletsSequence(String usrlogin, String portletsSequence, String action, String usrRole, String sessionId) throws Exception {
			
			Db db = null;
			try {
				db = new Db();
				String sql = "";
				if ( action == null || "".equals(action)) {
					//get first tab id for this user
					sql = "select tab_id from tab_user where user_login = '" + usrlogin + "' order by sequence";
					ResultSet rs = db.getStatement().executeQuery(sql);
					if ( rs.next() ) {
						action = rs.getString(1);
					}
					if ( "".equals(action)) {
						sql = "select tab_id from tab_template where user_login = '" + usrRole + "' order by sequence";
						rs = db.getStatement().executeQuery(sql);
						if ( rs.next() ) {
							action = rs.getString(1);
						}
						
					}
				}
				
				StringTokenizer st = new StringTokenizer(portletsSequence, ";");
				String tbl = "user_module";
				int seq = 0;
				int col = 0;
				
				//check to see if visitor
				if ( !"anon".equals(usrlogin)) {
					while ( st.hasMoreTokens() ) {
						String s = st.nextToken();
						if ( !"-".equals(s)) {
							sql = "update " + tbl + " set sequence = " + seq + ", column_number = " + col + " where module_id = '" + s + "' and tab_id = '" + action + "' and user_login = '" + usrlogin + "' ";
							db.getStatement().executeUpdate(sql);
						} else {
							col++;
						}
						seq ++;
					}					
				} else {
					while ( st.hasMoreTokens() ) {
						String s = st.nextToken();
						if ( !"-".equals(s)) {
							sql = "update " + tbl + " set sequence = " + seq + ", column_number = " + col + " where module_id = '" + s + "' and tab_id = '" + action + "' and user_login = '" + sessionId + "' ";
							db.getStatement().executeUpdate(sql);
						} else {
							col++;
						}
						seq ++;
					}					
					
				}
			} catch (Exception e ) {
				e.printStackTrace();
			
			} finally {
				
			}
			
		
	}
	
    public static Vector retrieve(String usrlogin, String tab) throws DbException {
        Db db = null;
        String sql = "";
        try { 
            db = new Db();
            Statement stmt = db.getStatement();

            sql = "SELECT m.module_id, m.module_title, m.module_class, u.module_custom_title, u.column_number " +
            "FROM module m, user_module_template u " +
            "WHERE m.module_id = u.module_id " +
            "AND u.user_login = '" + usrlogin + "' " +
            "AND u.tab_id = '" + tab + "' order by u.sequence";

            ResultSet rs = stmt.executeQuery(sql);
            
            Vector v = new Vector();
            while ( rs.next() ) {
                String id = rs.getString("module_id");
                String title = rs.getString("module_title");
                String mclass = rs.getString("module_class");
                String custom_title = rs.getString("module_custom_title");
                int col = rs.getInt("column_number");
                if ( custom_title == null || "".equals(custom_title) ) custom_title = title;
                v.addElement(new Module2(id, title, mclass, custom_title, col));
            }
            return v;
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }
    }
    
    public static void changeSequence(String usrlogin, String tab, String module, String pos) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            
            int sequence = 0;
            //get current sequence number
            {
                sql = "SELECT sequence FROM user_module_template WHERE module_id = '" + module + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                //Log.print(sql);
                ResultSet rs = stmt.executeQuery(sql);
                if ( rs.next() ) {
                    sequence = rs.getInt("sequence");   
                }
            }
            String module2 = "";
            if ( "down".equals(pos) ) { //find the module after this sequence

                sql = "SELECT module_id FROM user_module_template WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "' AND sequence = " + Integer.toString(++sequence);  
                //Log.print(sql);
                ResultSet rs = stmt.executeQuery(sql);
                if ( rs.next() ) module2 = rs.getString("module_id");

                if ( !"".equals(module2) ) {
                    sql = "UPDATE user_module_template SET sequence = " + sequence + " WHERE module_id = '" + module + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    //Log.print(sql);
                    stmt.executeUpdate(sql);        
                                        
                    sql = "UPDATE user_module_template SET sequence = " + Integer.toString(--sequence) + " WHERE module_id = '" + module2 + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";  
                    //Log.print(sql);
                    stmt.executeUpdate(sql);                            
                }

            } else if ( "up".equals(pos) ) { //find the tab before this sequence

                sql = "SELECT module_id FROM user_module_template WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "' AND sequence = " + Integer.toString(--sequence);  
                //Log.print(sql);
                ResultSet rs = stmt.executeQuery(sql);
                if ( rs.next() ) module2 = rs.getString("module_id");

                if ( !"".equals(module2) ) {
                    sql = "UPDATE user_module_template SET sequence = " + sequence + " WHERE module_id = '" + module + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    //Log.print(sql);
                    stmt.executeUpdate(sql);                        
                    
                    sql = "UPDATE user_module_template SET sequence = " + Integer.toString(++sequence) + " WHERE module_id = '" + module2 + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";  
                    //Log.print(sql);
                    stmt.executeUpdate(sql);                            
                }
            }

            conn.commit();
        } catch ( SQLException ex ) {
            try {
                conn.rollback();
            } catch ( SQLException exr ) {}
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }   
    }   
    
    public static void fixModuleSequence(String usrlogin, String tab) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();     
            boolean fix = true;
            /*
            {
                sql = "SELECT sequence FROM user_module_template WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "' AND sequence = 0";
                ResultSet rs = stmt.executeQuery(sql);
                if ( rs.next() ) fix = true;
                else fix = false;
            }
            */
            if ( fix ) {
                Vector v = new Vector();
                sql = "SELECT module_id FROM user_module_template WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    String module_id = rs.getString("module_id");
                    v.addElement(module_id);    
                }
                for ( int i=0; i < v.size(); i++ ) {
                    String module_id = (String) v.elementAt(i);
                    sql = "UPDATE user_module_template SET sequence = " + Integer.toString(i + 1) + " WHERE module_id = '" + module_id + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
                    stmt.executeUpdate(sql);
                }
            }
            conn.commit();
        } catch ( SQLException ex ) {
            try {
                conn.rollback();
            } catch ( SQLException exr ) {}
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }   
    }
    
    /**
     * This method gets the list of all available modules.
     */
    public static Vector getListOfModules() throws DbException {
        return getListOfModules("");    
    }
    
    /**
     * This method gets the list of module assigned to a role.
     */
    public static Vector getListOfModules(String role) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            
            if ( !"".equals(role) ) 
                sql = "SELECT m.module_id AS module_id, module_title, module_class, module_group FROM module m, role_module r " +
                "WHERE m.module_id = r.module_id AND user_role = '" + role + "' " +
                "ORDER BY module_title";
            else
                sql = "SELECT module_id, module_title, module_class, module_group FROM module ORDER BY module_group, module_title";             
            
            
            ResultSet rs = stmt.executeQuery(sql);
            Vector v = new Vector();
            while ( rs.next() ) {
                String id = rs.getString("module_id");
                String title = rs.getString("module_title");
                String klazz = rs.getString("module_class");
                String module_group = rs.getString("module_group");
                Module module = new Module(id, title, klazz);
                module.setGroupName(module_group); 
                v.addElement(module);
            }
            return v;
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }
    }
    
    /**
     * This method gets the list of modules assigned to a tab.
     */
    public static Vector getListOfModules(String role, String usrlogin, String tab) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            
            Vector vu = new Vector();
            {
                sql = "SELECT module_id FROM user_module_template WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tab + "'"; 
                
                ResultSet rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    String id = rs.getString("module_id");
                    vu.addElement(id);                      
                }
            }
            
            Vector v = new Vector();
            {           
                sql = "SELECT m.module_id AS module_id, module_title, " +
                		"module_class, module_group FROM module m, role_module r " +
                "WHERE m.module_id = r.module_id AND user_role = '" + role + "' " +
                "ORDER BY module_group, module_title";

                ResultSet rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    String id = rs.getString("module_id");
                    String title = rs.getString("module_title");
                    String klazz = rs.getString("module_class");
                    String group = rs.getString("module_group");
                    boolean marked = vu.contains(id) ? true : false;
                    Module module = new Module2(id, title, klazz, marked);
                    module.setGroupName(group != null ? group : "");
                    v.addElement(module);
                }
            }
            return v;
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }
    }
    
    /*
     * In this case, usrlogin is equal to role
     */
    
    public static Vector getListOfModules(String usrlogin, String tab) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            
            Vector vu = new Vector();
            {
                sql = "SELECT module_id FROM user_module_template WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tab + "'"; 
                
                ResultSet rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    String id = rs.getString("module_id");
                    vu.addElement(id);                      
                }
            }
            
            Vector v = new Vector();
            {           
                sql = "SELECT m.module_id AS module_id, module_title, module_class, module_group FROM module m, role_module r " +
                "WHERE m.module_id = r.module_id AND user_role = '" + usrlogin + "' " +
                "ORDER BY module_group, module_title";

                ResultSet rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    String id = rs.getString("module_id");
                    String title = rs.getString("module_title");
                    String klazz = rs.getString("module_class");
                    String group = rs.getString("module_group");
                    boolean marked = vu.contains(id) ? true : false;
                    Module module = new Module2(id, title, klazz, marked);
                    module.setGroupName(group != null ? group : "");
                    v.addElement(module);
                }
            }
            return v;
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }
    }    
    
    public static void saveModules(String usrlogin, String tabid, Vector allmodules) throws DbException {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
        	//Vector allModules = UserModuleDb.getListOfModules(role, tabId);
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            conn.setAutoCommit(false);
            
            //check allmodules for checked items
            Vector checkedModules = new Vector();
            for ( int i=0; i < allmodules.size(); i++ ) {
                Module2 module = (Module2) allmodules.elementAt(i);
                if ( module.getMarked() ) {
                    //put checked in this vector
                    checkedModules.addElement(module.getId());
                }   
            }
            
            //get current user modules from database
            Vector userModules = new Vector();
            {
                sql = "SELECT module_id FROM user_module_template WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    String id = rs.getString("module_id");
                    userModules.addElement(id);
                }
            }
            
            Vector deletedModules = new Vector();
            //delete the 'unchecked' first
            //by comparing the userModules with checkedModules
            //if exist in userModules but not in checkedModules then delete in userModules
            for ( int i=0; i < userModules.size(); i++ )
            {
                 if ( !checkedModules.contains((String) userModules.elementAt(i)) ) {
                    deletedModules.addElement((String) userModules.elementAt(i));
                }
            }
            
            //determine added modules
            Vector addedModules = new Vector();
            for ( int i=0; i < checkedModules.size(); i++ ) {
                if ( !userModules.contains( (String) checkedModules.elementAt(i)) ) {
                    addedModules.addElement((String) checkedModules.elementAt(i));
                }
            }
            
            //deleted modules list
            for ( int i=0; i < deletedModules.size(); i++ ) {
                String id = (String) deletedModules.elementAt(i);
                //get the sequence number first
                int sequence = 0;
                sql = "SELECT sequence FROM user_module_template WHERE module_id = '" + id + "' AND user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                ResultSet rs = stmt.executeQuery(sql);
                if ( rs.next() ) sequence = rs.getInt("sequence");
                
                //delete this module
                sql = "DELETE FROM user_module_template WHERE module_id = '" + id + "' AND user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                //Log.print("Deleting = " + id);
                //Log.print(sql);
                stmt.executeUpdate(sql);
                
                //update the sequence for other modules
                sql = "UPDATE user_module_template SET sequence = sequence - 1 WHERE sequence > " + sequence + " AND user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                //Log.print(sql);
                stmt.executeUpdate(sql);
            }
            
            //insert new modules
            {
                //get max sequence number
                int maxseq = 0;
                sql = "SELECT MAX(sequence) AS seq FROM user_module_template WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
                ResultSet rs = stmt.executeQuery(sql);
                if ( rs.next() ) maxseq = rs.getInt("seq");
                
                //add modules
                for ( int i=0; i < addedModules.size(); i++ ) {
                    String id = (String) addedModules.elementAt(i);
                    sql = "INSERT INTO user_module_template (tab_id, module_id, user_login, sequence) VALUES (" +
                    "'" + tabid + "', '" + id + "', '" + usrlogin + "', " + Integer.toString(++maxseq) + ")";   
                    //Log.print(sql);
                    stmt.executeUpdate(sql);
                }
            }
            
            conn.commit();
            
        } catch ( SQLException ex ) {
            try {
                conn.rollback();
            } catch ( SQLException exr ) {}
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }           
    }  
    
    
    
    public static Module getModuleById(String module_id) throws DbException {
        Module module = null;
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = null;
            {
                r = new SQLRenderer();
                r.add("module_title");
                r.add("module_class");
                r.add("module_group");
                r.add("module_description");
                r.add("module_id", module_id);
                sql = r.getSQLSelect("module");
                
                //-
                
                ResultSet rs = stmt.executeQuery(sql);
                if ( rs.next() ) {
                    String module_title = rs.getString("module_title");
                    String module_class = rs.getString("module_class");
                    String module_group = rs.getString("module_group");
                    String module_description = rs.getString("module_description");
                    module = new Module(module_id, module_title, module_class);
                    module.setGroupName(module_group != null ? module_group: "");
                    module.setDescription(module_description != null ? module_description: "");
                }
            }
            
            if ( module != null ) {
                r = new SQLRenderer();
                r.add("user_role");
                r.add("module_id", module_id);
                sql = r.getSQLSelect("role_module");
                ResultSet rs = stmt.executeQuery(sql);
                while ( rs.next() ) {
                    module.addRole(rs.getString("user_role"));                      
                }
            }
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }           
        return module;
    }
    
    public static void saveCustomTitles(String usrlogin, String tabid, String[] custom_titles) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            int seq = 0;
            for ( int i = 0; i < custom_titles.length; i++ ) {
                seq++;
                //sql = "UPDATE user_module_template SET module_custom_title = '" + custom_titles[i] + "' " +
                //"WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' AND sequence = " + seq;
                r.clear();
                r.add("module_custom_title", custom_titles[i]);
                r.update("user_login", usrlogin);
                r.update("tab_id", tabid);
                r.update("sequence", seq);
                sql = r.getSQLUpdate("user_module_template");
                stmt.executeUpdate(sql);
            }
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }               
    }
    
    
    public static void saveCustomTitlesAndColumnNumbers(String usrlogin, String tabid, String[] custom_titles, String[] column_numbers) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            int seq = 0;
            for ( int i = 0; i < custom_titles.length; i++ ) {
                seq++;
                //sql = "UPDATE user_module_template SET module_custom_title = '" + custom_titles[i] + "', column_number = " + column_numbers[i] +
                //" WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' AND sequence = " + seq;
                //Log.print(sql);
                r.clear();
                r.add("module_custom_title", custom_titles[i]);
                r.add("column_number", column_numbers[i]);
                r.update("user_login", usrlogin);
                r.update("tab_id", tabid);
                r.update("sequence", seq);
                sql = r.getSQLUpdate("user_module_template");
                stmt.executeUpdate(sql);
            }
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }           
        
    }
    
    public static void saveModules(String usrlogin, String tabid, String[] moduleIds, String[] custom_titles, String[] column_numbers) throws DbException {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            for ( int i = 0; i < moduleIds.length; i++ ) {
                r.clear();
                r.add("module_custom_title", custom_titles[i]);
                r.add("column_number", Integer.parseInt(column_numbers[i]));
                r.add("sequence", i+1);
                r.update("module_id", moduleIds[i]);
                r.update("user_login", usrlogin);
                r.update("tab_id", tabid);
                sql = r.getSQLUpdate("user_module_template");
                stmt.executeUpdate(sql);
            }
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }           
        
    }    
    
    //************
    
    
    
    //backup method
    public static Vector retrieve2(String usrlogin, String tab) throws DbException {
        Db db = null;
        String sql = "";
        try { 
            db = new Db();
            Statement stmt = db.getStatement();

            sql = "SELECT m.module_id, m.module_title, m.module_class " +
            "FROM module m, user_module_template u " +
            "WHERE m.module_id = u.module_id " +
            "AND u.user_login = '" + usrlogin + "' " +
            "AND u.tab_id = '" + tab + "' order by u.sequence";

            ResultSet rs = stmt.executeQuery(sql);
            
            Vector v = new Vector();
            while ( rs.next() ) {
                String id = rs.getString("module_id");
                String title = rs.getString("module_title");
                String mclass = rs.getString("module_class");
                v.addElement(new Module(id, title, mclass));
            }
            return v;
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }
    } 
    
    public static void saveModules(String role, String tab, String[] moduleIds, String[] moduleTitles) throws Exception {
    	if ( moduleIds == null ) return;
    	Db db = null;
    	String sql = "";
    	try {
    		db = new Db();
    		for ( int i=0; i < moduleIds.length; i++ ) {
    			sql = "update user_module_template set sequence = " + Integer.toString(i+1) + ", " +
    			" module_custom_title = '" + moduleTitles[i] + "' " +
    			" where tab_id = '" + tab + "' and module_id = '" + moduleIds[i] + "'" +
    			" and user_login = '" + role + "'";
    			db.getStatement().executeUpdate(sql);
    			
    			
    			
    		}
    		
    	} finally {
    		if ( db != null ) db.close();
    	}
    }
    
}
