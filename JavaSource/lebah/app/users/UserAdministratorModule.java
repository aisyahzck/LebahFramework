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

package lebah.app.users;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.app.RoleProcessor;
import lebah.portal.db.RegisterUser;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class UserAdministratorModule extends VTemplate {    
    private RoleProcessor processor2 = new RoleProcessor();
    private int itemsPerPage = 0;


    public Template doTemplate() throws Exception {
        String targetPage = "users/user_list.vm";  // DEFAULT PAGE
        HttpSession session = request.getSession();
        targetPage = doJob(session);
        Template template = engine.getTemplate(targetPage);
        return template;
    }
    
    public String doJob(HttpSession session) throws Exception
    {
        String targetPage = "";
        
        // GET NUMBER OF ITEMS PER PAGE
        String strItemsPerPage = request.getParameter("itemsPerPage");
        if ((strItemsPerPage == null) || (strItemsPerPage.equals("")))
        {
            strItemsPerPage = (String) session.getAttribute("_ITEMS_PER_PAGE");
            // DEFAULT ITEMS PER PAGE SET TO 20
            if ((strItemsPerPage == null) || (strItemsPerPage.equals(""))) strItemsPerPage = "20";
        }
        session.setAttribute("_ITEMS_PER_PAGE",strItemsPerPage);
        itemsPerPage = Integer.parseInt(strItemsPerPage);
        context.put("itemsPerPage",new Integer(itemsPerPage));        
        //System.out.println("items per page = "+strItemsPerPage);
        
        // GET LIST ORDER BY
        String orderBy = request.getParameter("orderBy");
        String orderByField = "user_name";
        if ((orderBy == null) || (orderBy.equals(""))) orderBy = "name"; // DEFAULT ORDER BY NAME
        context.put("orderBy",new String(orderBy));
        if (orderBy.equals("name"))
        {
            orderByField = "user_name";
        } else if (orderBy.equals("role"))
        {
            orderByField = "user_role";
        } else if (orderBy.equals("scheme"))
        {
            orderByField = "css_title";
        }
        
        String action = request.getParameter("command");
        if (action == null) action = "";
                
        if (action.equals("get_user_list"))
        {
            getPage(session, orderByField);
            targetPage = "users/user_list.vm";

        }
        else if ( action.equals("filter_by_role")) {
            getPage(session, orderByField);
            targetPage = "users/user_list.vm";        	
        }
        else if (action.equals("delete_user"))
        {
            String[] users = request.getParameterValues("users");
            if (users != null) UserAdministratorProcessor.deleteUser(users);
            getPage(session, orderByField);
            targetPage = "users/user_list.vm";

        } else if (action.equals("update_role"))
        {
            String[] users = request.getParameterValues("users");
            String role = request.getParameter("role");
            if (users != null)
            {
                UserAdministratorProcessor.updateRole(users,role);
                RegisterUser.setUserModule(users,role);
            }
            getPage(session, orderByField);
            targetPage = "users/user_list.vm";
            
        } else if (action.equals("update_scheme"))
        {
            String[] users = request.getParameterValues("users");
            String scheme = request.getParameter("scheme");
            if (users != null) UserAdministratorProcessor.updateScheme(users,scheme);
            getPage(session, orderByField);
            targetPage = "users/user_list.vm";

        } else {
            getPage(session, orderByField);
            targetPage = "users/user_list.vm";
        }
        return targetPage;
    }

/**
 * This method will generate the page for the file list.
 */    
    private void getPage(HttpSession session, String orderByField) throws Exception
    {
        try
        {
        	
        //filter by role
        	
        String filter_role = request.getParameter("filter_role");
        if ( filter_role == null ) filter_role = "";
        System.out.println("filter_role = " + filter_role);
        
        // DETERMINE TYPE OF NAVIGATION
        String navigation = request.getParameter("navigation");
        if (navigation == null) navigation = "";
        
        // GET THE STARTING INDEX
        String strStartIndex = "0";
        if (navigation.equals("goto_page"))
        {
            strStartIndex = request.getParameter("index");
            
        } else if ( (navigation.equals("next_page")) || (navigation.equals("previous_page")) )
        {
            strStartIndex = (String) session.getAttribute("_START_INDEX");
        }
        if(strStartIndex == null) strStartIndex = "0";
        int startIndex = Integer.parseInt(strStartIndex);

        if (navigation.equals("next_page"))
        {
            //NEXT PAGE
            if (startIndex >= 0)
            {
                startIndex = startIndex + itemsPerPage;
            } else {
                startIndex = 0;
            }
        } else if (navigation.equals("previous_page"))
        {
            //PREVIOUS PAGE
            if (startIndex >= itemsPerPage)
            {
                startIndex = startIndex - itemsPerPage;
            } else {
                startIndex = 0;
            }
        }   // ELSE NO CHANGES TO THE START INDEX
        //System.out.println("start index = "+Integer.toString(startIndex));
        
        // GET THE LAST INDEX
        int lastIndex = startIndex + itemsPerPage;
        //System.out.println("last index = "+Integer.toString(lastIndex));
        
        // GET THE LIST FOR ALL USERS
        Vector list = null;
        if ( filter_role.equals("")) list = UserAdministratorProcessor.getUserList(orderByField);
        else list = UserAdministratorProcessor.getUserList(filter_role, orderByField);
        
        // EXTRACT THE ROOT USER
        Vector list2 = new Vector();
        for(Enumeration e = list.elements(); e.hasMoreElements();)
        {
            lebah.object.User obj = (lebah.object.User) e.nextElement();
            if (!obj.getRole().equals("root"))
            {
                list2.addElement(obj);
            }
            
        }
        
        // GET THE TOTAL NUMBER OF USERS
        int totalItem = list2.size();
        //System.out.println("total page = "+Integer.toString(totalItem));
        
        // BUILD THE NEW LIST BY EXTRACTING USERS BETWEEN 
        // STARTING (INCLUSIVE) AND LAST (EXCLUSIVE) INDEX
        Vector newList = new Vector();
        for (int i = startIndex; i < lastIndex; i++)
        {
            // TO PREVENT FROM ARRAY OUT OF BOUNDS EXCEPTION
            // ONLY EXECUTE WHEN VARIABLE i IS WITHIN MAXIMUM INDEX LIMIT
            if (i <= totalItem-1)
            {
                //System.out.println("index taken = "+Integer.toString(i));
                newList.addElement(list2.get(i));
            }
        }
        
        // DETERMINE IF START-PAGE OR LAST-PAGE IS TRUE
        Boolean isLastPage = new Boolean(true);
        Boolean isStartPage = new Boolean(true);
        if (startIndex > 0) isStartPage = new Boolean(false);
        if (startIndex <= ((totalItem-itemsPerPage)-1)) isLastPage = new Boolean(false);

        // DETERMINE THE PAGE NUMBER        
        int totalPages = totalItem/itemsPerPage;
        if ( totalItem%itemsPerPage != 0 ) totalPages = totalPages + 1;
        
        int page = 0;
        String strPage = request.getParameter("page");
        if (strPage == null) strPage = "1";
        page = Integer.parseInt(strPage);
        
        // SET THE PAGE NUMBER
        if (navigation.equals("next_page"))
        {
            // NEXT PAGE
            page++;
        } else if (navigation.equals("previous_page")) {
            // PREVIOUS PAGE
            page--;
        }   // ELSE NO CHANGE TO THE PAGE NUMBER
        
        session.setAttribute("_START_INDEX", Integer.toString(startIndex));
        context.put("index",new Integer(startIndex));        
        context.put("page",new Integer(page));
        context.put("totalPages",new Integer(totalPages));
        context.put("users",newList);
        context.put("isLastPage",isLastPage);
        context.put("isStartPage",isStartPage);
        context.put("totalUsers",new Integer(totalItem));
        
        Vector roles = processor2.getRoles();
        context.put("roles",roles);
        
        Vector schemes = UserAdministratorProcessor.getSchemes();
        context.put("schemes",schemes);
        }
        catch (Exception ex)
        {
            throw new Exception("getPage(): "+ex.getMessage());
        }
    }
}
