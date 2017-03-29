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

package lebah.module.theme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.Db;
import lebah.module.theme.object.PageStyle;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class PageStyleManagerModule extends VTemplate {
	
    public PageStyleManagerModule() {
        
    }       
    
    public PageStyleManagerModule(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
        super(engine, context, req, res);
    }
    public Template doTemplate() throws Exception
    {
        HttpSession session = request.getSession();
        doJob(session);
        Template template = engine.getTemplate("/vtl/module/themeManager/pagestyle.vm");
        return template;
    }
    
    public void doJob(HttpSession session) throws Exception
    {
        String action = request.getParameter("command");
        //System.out.println("action = "+action);
        if (action == null) action = "";
        
        if (action.equals("delete"))
        {
            String title = request.getParameter("deltitle");
            deletePageStyle(title);
        }
        else if (action.equals("add"))
        {
            String title = request.getParameter("pagetitle");
            String filename = request.getParameter("filename");
            addPageStyle(title,filename);
        }
        Vector list = new Vector();
        list = getPageStyleList();
        context.put("pageStyleList",list);
    }
    
    private void addPageStyle(String title, String filename) throws Exception
    {
        Db db = null;
        try
        {
            db = new Db();
            Connection con = db.getConnection();
            String sql = "insert into page_css (css_title,css_name) values (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, filename);
            ps.executeUpdate();
        }
        finally
        {
            if ( db != null ) db.close();
        }
    }
    
    private void deletePageStyle(String title) throws Exception
    {
        Db db = null;
        try
        {
            db = new Db();
            Connection con = db.getConnection();
            String sql = "delete from page_css where css_title = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.executeUpdate();
        }
        finally
        {
            if ( db != null ) db.close();
        }
    }
    
    private Vector getPageStyleList() throws Exception
    {
        Db db = null;
        Vector list = new Vector();
        try
        {
            db = new Db();
            Connection con = db.getConnection();
            String sql = "select css_title, css_name from page_css order by css_title";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            PageStyle obj = null;
            while (rs.next())
            {
                obj = new PageStyle();
                obj.setTitle(rs.getString("css_title"));
                obj.setFilename(rs.getString("css_name"));
                
                list.add(obj);
            }            
        }
        finally
        {
            if ( db != null ) db.close();
        }
        return list;
    }
}
