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

package lebah.repository;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.app.FilesRepository;

import org.apache.velocity.Template;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 */
public class ImageGalleryModule extends lebah.portal.velocity.VTemplate  implements lebah.portal.Attributable {
	boolean isLastPage = false;
	static int LIST_ROWS = 12;	
	
	private String[] names = {"Moderators"};
	private Hashtable values = new Hashtable();
	
	public String[] getNames() {
		return names;
	}
	
	public Hashtable getValues() {
		return values;
	}
	
	public void setValues(java.util.Hashtable hashtable) {
		values = hashtable;
	}	
		
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		String category_id  = getId();
		
		String moderators = values.get(names[0]) != null ? (String) values.get(names[0]) + "," : "";
		moderators = "root, " + moderators;
		String _portal_role = (String) session.getAttribute("_portal_role");
		String inCollabModule = session.getAttribute("inCollabModule") != null ? 
				(String) session.getAttribute("inCollabModule") : "false";
		String collab_role = "true".equals(inCollabModule) ? session.getAttribute("_collab_role") != null ? 
				(String) session.getAttribute("_collab_role") : "" : "";
		String roleToAllow = !"".equals(collab_role) ? collab_role : _portal_role;	
		if ( moderators.indexOf(roleToAllow + ",") > -1 ) {
			context.put("allowPost", new Boolean(true));		
		} else {
			context.put("allowPost", new Boolean(false));		
		}		
		
		String template_name = "vtl/repository/image_gallery.vm";
		String submit = getParam("command");
		
		String enable_edit = session.getAttribute("_enable_edit") != null ?
				         (String) session.getAttribute("_enable_edit") : "false";
		boolean editing = "true".equals(enable_edit) ? true : false;
		context.put("editing", new Boolean(editing));	
		
		if ( "UploadFile".equals(submit) ) {
			template_name = "vtl/repository/image_added.vm";
			Hashtable h = uploadFile(session, category_id);
			String group_id = (String) h.get("group_id");
			Item item = (Item) h.get("item");
			context.put("item", item);
		}	
		else if ( "goPage".equals(submit) ) {
			int page = Integer.parseInt(getParam("pagenum"));
			getRows(session, page);
			session.setAttribute("page_number", Integer.toString(page));			
		}
		else if ( "addGroup".equals(submit) ) {
			addGroup(category_id);
			submit = "";	
		}
		else if ( "editing".equals(submit) ) {
			if ( editing ) editing = false;
			else editing = true;
			session.setAttribute("_enable_edit", editing ? "true" : "false" );
			context.put("editing", new Boolean(editing));
			submit = "";	
		}	
		else if ( "deleteItem".equals(submit) ) {
			String item_id = getParam("item_id");
			ItemData.delete(item_id);
			submit = "";	
		}	
		else if ( "deleteChecked".equals(submit) ) {
			String[] item_ids = request.getParameterValues("item_ids");
			if ( item_ids != null ) ItemData.delete(item_ids);
			submit = "";	
		}	
		else if ( "deleteGroup".equals(submit) ) {
			String id = getParam("group_id");
			GroupData.delete(id);
			submit = "";	
		}
		else if ( "deleteCategory".equals(submit) ) {
			String id = getParam("category_id");
			CategoryData.delete(id);
			submit = "";	
		}
		else if ( "addImage".equals(submit)) {
			template_name = "vtl/repository/image_input.vm";
		}
		else if ( "editImage".equals(submit)) {
			template_name = "vtl/repository/image_edit.vm";
			String uid = getParam("item_id");
			Item item = ItemData.getItem(uid);
			context.put("item", item);
		}	
		else if ( "updateItem".equals(submit)) {
			template_name = "vtl/repository/image_edit.vm";
			String item_id = getParam("item_id");
			String description = getParam("description");
			ItemData.update(item_id, description);
			Item item = ItemData.getItem(item_id);
			context.put("item", item);			
		}		
		else {
			submit = "";	
		}	
		
			

		
		if ( "".equals(submit)) {
			prepareData(session, category_id);
			getRows(session, 1);
			session.setAttribute("page_number", "1");
			context.put("page_number", new Integer(1));				
		}
		

		
		Template template = engine.getTemplate(template_name);	
		return template;		
		
	}
	
	void prepareData(HttpSession session, String category_id) throws Exception {
		//String category_id = getParam("category_id");
		context.put("category_id", category_id);
		
		String group_id = getParam("group_id");
		context.put("group_id", group_id);	

		Vector groupList =  GroupData.getList(category_id);
		context.put("groupList", groupList);
		
		if ( "".equals(group_id) && groupList.size() > 0 ) {
			Group group = (Group) groupList.elementAt(0);
			group_id = group.getId();
			context.put("group_id", group_id);
		}
		
		Group g = GroupData.getData(group_id);
		context.put("group", g);
		prepareList(session, group_id);		
	}
	
	void prepareData(HttpSession session, String category_id, String group_id) throws Exception {
		context.put("category_id", category_id);
		context.put("group_id", group_id);	
				
		Vector categoryList = CategoryData.getList();
		context.put("categoryList", categoryList);
		
		if ( "".equals(group_id) && categoryList.size() > 0 ) {
			//get first item in CategoryList
			Category category = (Category) categoryList.elementAt(0);
			category_id = category.getId();
			context.put("category_id", category_id);
			
		}
		
		Vector groupList =  GroupData.getList(category_id);
		context.put("groupList", groupList);
		
		if ( "".equals(group_id) && groupList.size() > 0 ) {
			Group group = (Group) groupList.elementAt(0);
			group_id = group.getId();
			context.put("group_id", group_id);
		}
		
		Group g = GroupData.getData(group_id);
		context.put("group", g);
		prepareList(session, group_id);		
	}	
	
	
	//-- start paging methods
	
	void prepareList(HttpSession session, String group_id) throws Exception {
		Vector list = ItemData.getList(group_id);
		int pages =  list.size() / LIST_ROWS;
		double leftover = ((double)list.size() % (double)LIST_ROWS);
		if ( leftover > 0.0 ) ++pages;
		context.put("pages", new Integer(pages));	
		session.setAttribute("pages", new Integer(pages));
			
		session.setAttribute("enquiryList", list);

	}	
	
	void getRows(HttpSession session, int page) throws Exception {
		Vector list = (Vector) session.getAttribute("enquiryList");
		Vector items = getPage(page, LIST_ROWS, list);
		context.put("items", items);	
		context.put("page_number", new Integer(page));
		context.put("pages", (Integer) session.getAttribute("pages" ));			
	}
	
	Vector getPage(int page, int size, Vector list) throws Exception {
		int elementstart = (page - 1) * size;
		int elementlast = 0;
		//int elementlast = page * size < list.size() ? (page * size) - 1 : list.size() - 1;
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
	
	//---- end paging methods	
	
	Hashtable uploadFile(HttpSession session, String category_id) throws Exception {
	
		
		lebah.upload.SmartUpload2 myUpload = new lebah.upload.SmartUpload2();
		myUpload.initialize2(session.getServletContext(), request, response);
		myUpload.upload();
		
		//request parameters
		
		//String category_id = myUpload.getRequest().getParameter("category_id");
		String group_id = myUpload.getRequest().getParameter("group_id");
		String item_name = myUpload.getRequest().getParameter("item_name");
		String item_description = myUpload.getRequest().getParameter("item_description");
		
		String subdir = category_id + "/" + group_id;
		String upload_dir = FilesRepository.getUploadDir() + subdir;			
		java.io.File dir = new java.io.File(upload_dir);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}

		int count = myUpload.save( upload_dir, lebah.upload.SmartUpload2.SAVE_PHYSICAL);
		
		com.jspsmart.upload.Files files = myUpload.getFiles();
		Vector filenames = new Vector();
		for ( int i=0; i < files.getCount(); i++ ) {
			com.jspsmart.upload.File file = files.getFile(i);
			String fileName = file.getFileName();
			if ( fileName != null && !"".equals(fileName.trim()) )filenames.addElement(fileName);
			//create thumbnail
			String imgName = upload_dir + "/" + fileName;
			String imgThumbname = upload_dir + "/tn_" + fileName;
			Thumbnail.create(imgName, imgThumbname, 120, 80, 75);
		}

		String filename = upload_dir + "/" + (String) filenames.elementAt(0);
		if ( item_name == null || item_name.equals("")) item_name = (String) filenames.elementAt(0);
		//insert data into database
		Item item = new Item();
		item.setName(item_name);
		item.setDescription(item_description);
		item.setGroupId(group_id);
		item.setFileName(filename);
		item.setThumbFilename(upload_dir + "/tn_" + (String) filenames.elementAt(0));
		String uid = ItemData.add(item);
		item.setId(uid);
		
		//
		String pagenum = myUpload.getRequest().getParameter("pagenum");
		int page = pagenum != null && !"".equals(pagenum) ? Integer.parseInt(pagenum) : 1;
		Hashtable h = new Hashtable();
		h.put("page", new Integer(page));
		h.put("category_id", category_id);
		h.put("group_id", group_id);
		h.put("item", item);
		return h;
	}	
	
	
	void addGroup(String category_id) throws Exception {
		String group_name = getParam("group_name");
		Group g = new Group();
		g.setName(group_name);
		g.setCategoryId(category_id);
		GroupData.add(g);
		
	}			

}
