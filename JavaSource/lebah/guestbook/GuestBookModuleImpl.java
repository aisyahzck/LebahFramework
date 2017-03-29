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

package lebah.guestbook;

import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.util.Util;

import org.apache.velocity.Template;

public class GuestBookModuleImpl  extends lebah.portal.AjaxBasedModule {
	protected boolean isLastPage = false;
	protected int LIST_ROWS = 5;	
	
	/*
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(submit, session);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    */
    
    public String doTemplate2() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(submit, session);
        
        return template_name;        
    }    
    
    
    String prepareTemplate(String submit, HttpSession session) throws Exception {
        String template_name = "";
        if ( "".equals(submit)) submit = "listGuestBookEntries";
        if ( "listGuestBookEntries".equals(submit)) {
        	prepareList(session, GuestBookData.getList(getId()));
        }
		else if ( "goPage".equals(submit) ) {
			int page = Integer.parseInt(getParam("pagenum"));
			getRows(session, page);
			session.setAttribute("page_number", Integer.toString(page));
		}
        else if ("addGuestBookEntry".equals(submit)) {
        	GuestBook gb = new GuestBook();
        	gb.setRemoteAddress(request.getRemoteAddr());
        	gb.setCategoryId(getId());
        	gb.setPostedBy(getParam("posted_by"));
        	gb.setMessage(getParam("message"));
        	gb.setEmail(getParam("email"));
        	gb.setHomepage(getParam("homepage"));
        	GuestBookData.add(gb);
        	prepareList(session, GuestBookData.getList(getId()));
        }
        else if ( "deleteGuestBookEntry".equals(submit)) {
        	GuestBookData.delete(getParam("uid"));
        	prepareList(session, GuestBookData.getList(getId()));
        }
        context.put("util", new Util());
    	template_name = "vtl/guestbook/list_entries.vm";      
        
        return template_name;
    }
    
	//-- start paging methods
	
	void prepareList(HttpSession session, Vector list) throws Exception {
		int countEntries = list.size();
		int pages =  list.size() / LIST_ROWS;
		double leftover = ((double)list.size() % (double)LIST_ROWS);
		if ( leftover > 0.0 ) ++pages;
		context.put("countEntries", new Integer(countEntries));
		context.put("pages", new Integer(pages));	
		session.setAttribute("pages", new Integer(pages));
		session.setAttribute("itemList", list);
		getRows(session, 1);
		session.setAttribute("page_number", "1");
		context.put("page_number", new Integer(1));
	}
	
	void getRows(HttpSession session, int page) throws Exception {
		Vector list = (Vector) session.getAttribute("itemList");
		Vector items = getPage(page, LIST_ROWS, list);
		context.put("items", items);	
		context.put("page_number", new Integer(page));
		context.put("pages", (Integer) session.getAttribute("pages" ));			
	}
	
	Vector getPage(int page, int size, Vector list) throws Exception {
		int elementstart = (page - 1) * size;
		int elementlast = 0;
		if ( page * size < list.size() ) {
			elementlast = (page * size) - 1;
			isLastPage = false;
			context.put("eol", new Boolean(false));
		} else {
			elementlast = list.size() - 1;
			isLastPage = true;
			context.put("eol", new Boolean(true));
		}
		if ( page == 1 ) context.put("bol", new Boolean(true));
		else context.put("bol", new Boolean(false));
		Vector v = new Vector();
		for ( int i = elementstart; i < elementlast + 1; i++ ) {
			v.addElement(list.elementAt(i));
		}
		return v;
	}		
	
	//--- end paging methods    
	


}
