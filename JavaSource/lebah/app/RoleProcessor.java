/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.object.Module;
import lebah.portal.element.Role;


/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class RoleProcessor
{
    private String className = "mecca.app.RoleProcessor";
    private Hashtable conProp;
    
    public RoleProcessor()
    {
        conProp = new Hashtable();
    }
    
    public RoleProcessor(Hashtable conProp)
    {
        this.conProp = new Hashtable();
        this.conProp = conProp;
    }

    private Db getDb() throws Exception
    {
        Db db = null;
        if (conProp.isEmpty())
        {
            db = new Db();
        } else {
            db = new Db(conProp);
        }
        return db;
    }

/**
 * This method adds role into the database.
 */
    public void addRole(String name, String description) throws Exception
    {
        String sql = "insert into role (name,description) values ('"+name+"','"+description+"')";
        Db database = getDb();
        try
        {
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }
        finally
        {
            if ( database != null ) database.close();
        }
    }
    
/**
 * This method delete role from the database.
 */
    public void deleteRole(String name) throws Exception
    {
        String sql = "delete from role where name = '"+name+"'";
        Db database = getDb();
        try
        {
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }
        finally
        {
            if ( database != null ) database.close();
        }
    }
    
/**
 * This method updates role in the database.
 */
    public void updateRole(String oldName, String name, String description) throws Exception
    {
        String sql = "update role set "+
                        "name = '"+name+"', "+
                        "description = '"+description+"' "+
                        "where name = '"+oldName+"'";
        Db database = getDb();
        try
        {
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql);
        }
        finally
        {
            if ( database != null ) database.close();
        }
    }
    
/**
 * This method gets all roles in the database and returns a Vector object.
 */
    public Vector getRoles() throws Exception
    {
        String sql = "select name, description from role order by name";
        Vector list = new Vector();
        Db database = getDb();
        try
        {
            Role obj = null;
            Statement stmt = database.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                obj = new Role();
                obj.setName(rs.getString("name"));
                obj.setDescription(rs.getString("description"));
                
                list.addElement(obj);
            }
        }
        finally
        {
            if ( database != null ) database.close();
        }
        return list;
    }
    
/**
 * This method gets all modules in the database and returns a Vector object.
 * The modules will be marked as selected or not.
 */
    public Vector getModules(String role) throws Exception
    {
        String sql = "select module_id, module_title, module_class, "+
            "module_group, module_description from module order by module_group";
        Vector list = new Vector();
        Db database = getDb();
        try
        {
            Module obj = null;
            Statement stmt = database.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                obj = new Module();
                obj.setId(rs.getString("module_id"));
                obj.setTitle(rs.getString("module_title"));
                obj.setClassName(rs.getString("module_class"));
                obj.setGroup(rs.getString("module_group"));                
                obj.setDescription(rs.getString("module_description"));
                obj.setSelected(checkModule(role,obj.getId()));
                
                list.addElement(obj);
            }
        }
        finally
        {
            if ( database != null ) database.close();
        }
         return list;        
    }
    
/**
 * This method determines if a module is selected or not for a role.
 */
    private boolean checkModule(String role, String moduleId) throws Exception
    {
        String sql = "select module_id from role_module where "+
            "user_role = '"+role+"' and module_id = '"+moduleId+"'";
        boolean selected = false;
        Db database = getDb();
        try
        {
            Statement stmt = database.getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                selected = true;
            }
        }
        finally
        {
            if ( database != null ) database.close();
        }
        return selected;        
    }
    
/**
 * This method updates the modules assigned to a role.
 */
    public void updateRoleModule(String role, String[] modules) throws Exception
    {
        String sql1 = "delete from role_module where user_role = '"+role+"'";
        Db database = getDb();        
        try
        {
            Statement stmt = database.getStatement();
            stmt.executeUpdate(sql1);
            if(modules != null)
            {
                for(int i = 0; i < modules.length; i++)
                {
                    String sql2 = "insert into role_module (module_id, user_role) "+
                        "values ('"+modules[i]+"','"+role+"')";
                    stmt.executeUpdate(sql2);
                }
            }
        }
        finally
        {
            if ( database != null ) database.close();
        }
    }
}
