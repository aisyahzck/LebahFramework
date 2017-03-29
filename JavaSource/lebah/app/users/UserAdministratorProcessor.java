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

package lebah.app.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.object.Scheme;
import lebah.object.User;


/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class UserAdministratorProcessor
{
    public UserAdministratorProcessor()
    {
    }

/**
 * This method gets the list of users and returns a Vector.
 */
    public static Vector getUserList() throws Exception
    {
        return getUserList("user_name");
    }
/**
 * This method gets the list of users and returns a Vector.
 * The list will be ordered according to the orderBy attribute.
 */
    public static Vector getUserList(String orderBy) throws Exception
    {
        String sql = "select A.user_login, A.user_password, A.user_name, "+
                        "A.user_role, C.css_title from users as A, user_css as B, "+
                        "page_css as C where A.user_login = B.user_login and "+
                        "B.css_name = C.css_name order by " + orderBy;
        
        Db database = null;
        User obj = null;
        Vector userList = new Vector();
        try
        {
            database = new Db();
            Statement stmt = database.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                obj = new User();
                obj.setLogin(rs.getString("user_login"));
                obj.setPassword(rs.getString("user_password"));
                obj.setName(rs.getString("user_name"));
                obj.setRole(rs.getString("user_role"));
                obj.setScheme(rs.getString("css_title"));
                /*
                String css = rs.getString("css_name");
                if (css != null)
                {
                    css = css.substring(0, (css.length() - 4));
                } else {
                    css = "";
                }
                obj.setScheme(css);
                */
                userList.addElement(obj);
            }
            
        }
        catch ( DbException dbex )
        {
        }
        catch(SQLException ex)
        {
            throw new Exception("getUserList(): "+ex.getMessage());
        }
        finally
        {
            if ( database != null ) database.close();
        }
        return userList;        
    }
    
    public static Vector getUserList(String role, String orderBy) throws Exception
    {
        String sql = "select A.user_login, A.user_password, A.user_name, "+
                        "A.user_role, C.css_title from users as A, user_css as B, "+
                        "page_css as C where A.user_login = B.user_login and "+
                        " A.user_role = '" + role + "' and " +
                        "B.css_name = C.css_name order by " + orderBy;
        
        Db database = null;
        User obj = null;
        Vector userList = new Vector();
        try
        {
            database = new Db();
            Statement stmt = database.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                obj = new User();
                obj.setLogin(rs.getString("user_login"));
                obj.setPassword(rs.getString("user_password"));
                obj.setName(rs.getString("user_name"));
                obj.setRole(rs.getString("user_role"));
                obj.setScheme(rs.getString("css_title"));
                /*
                String css = rs.getString("css_name");
                if (css != null)
                {
                    css = css.substring(0, (css.length() - 4));
                } else {
                    css = "";
                }
                obj.setScheme(css);
                */
                userList.addElement(obj);
            }
            
        }
        catch ( DbException dbex )
        {
        }
        catch(SQLException ex)
        {
            throw new Exception("getUserList(): "+ex.getMessage());
        }
        finally
        {
            if ( database != null ) database.close();
        }
        return userList;        
    }    
    
    public static void deleteUser(String[] users) throws Exception
    {
        int len = users.length;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";
        String sql5 = "";
           
        Db database = new Db();
        Statement stmt = database.getStatement();
        
        for (int i = 0; i < len; i++)
        {
            //System.out.println(">> "+ users[i]);
            sql1 = "delete from planner_task where user_login = '"+users[i]+"'";
            sql2 = "delete from tab where user_login = '"+users[i]+"'";
            sql3 = "delete from user_module where user_login = '"+users[i]+"'";
            sql4 = "delete from user_css where user_login = '"+users[i]+"'";
            sql5 = "delete from users where user_login = '"+users[i]+"'";
            
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
            stmt.executeUpdate(sql5);
        }
        
       if ( database != null ) database.close();
    }
    
    public static void updateRole(String[] users, String role) throws Exception
    {
        Db database = new Db();
        Statement stmt = database.getStatement();
        for (int i = 0; i < users.length; i++)
        {
            String sql = "update users set user_role = '"+role+"' where user_login = '"+users[i]+"'";
            stmt.executeUpdate(sql);
        }
        if ( database != null ) database.close();
    }
    
    public static void updateScheme(String[] users, String scheme) throws Exception
    {
        Db database = new Db();
        Statement stmt = database.getStatement();
        for (int i = 0; i < users.length; i++)
        {
            String sql = "update user_css set css_name = '"+scheme+"' where user_login = '"+users[i]+"'";
            stmt.executeUpdate(sql);
        }
        if ( database != null ) database.close();
    }
    
    public static Vector getSchemes() throws Exception
    {
        String sql = "select css_title, css_name from page_css order by css_title";
        
        Db database = new Db();
        Vector list = new Vector();
        try
        {
            Statement stmt = database.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                Scheme obj = new Scheme();
                obj.setName(rs.getString("css_name"));
                obj.setTitle(rs.getString("css_title"));
                
                list.addElement(obj);
            }
        }
        catch (SQLException ex)
        {
            throw new Exception("getSchemes(): "+ex.getMessage());
        }
        finally
        {
            if (database!=null) database.close();
        }
        return list;        
    }
}
