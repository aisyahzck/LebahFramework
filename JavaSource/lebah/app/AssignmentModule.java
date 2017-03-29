/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */
package lebah.app;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.db.DbForum;
import lebah.tree.Attachment;

 
/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class AssignmentModule extends ForumModule {
	
	public AssignmentModule() {
		idPrefix = "ass";
		vtlDir = "vtl/assignment";
	}
	
	void open(HttpSession session, int page, String id, String category_id) throws Exception {
		
		//System.out.println("in the assingment module");
		context.put("page_number", new Integer(page));
		
		//String category_id = getParam("category_id");
		String forum_id = !"".equals(id) ? id : getParam("forum_id");
		context.put("forum_id", forum_id);
		session.setAttribute("forum_id_" + getId(), forum_id);
		
		Forum forum = null;
		DbForum db = null;
		try {
			db = new DbForum();
			forum = db.get(forum_id);
			
			context.put("forum", forum);
			
			String posted_by = forum.getUserId();
			String portal_login = (String) session.getAttribute("_portal_login");
			if ( posted_by.equals(portal_login) ) {
				context.put("allowUpdate", new Boolean(true));
				context.put("allowDelete", new Boolean(true));			
			}
			
			Forum parent = (Forum) forum.getParent();
			
			//get this forum's parent
			if ( parent != null ) {
				context.put("hasParent", new Boolean(true));
				context.put("forum_parent", parent);
			} else {
				context.put("hasParent", new Boolean(false) );
			}
			
			//attachment
			context.put("uploadDir", FilesRepository.getUploadDir());
			if (forum.hasAttachment()) { 
				//String folder = forum.getAttachmentFolder();
				String files_dir = FilesRepository.getUploadDir() + forum.getAttachmentFolder();
				context.put("files_dir", files_dir);
				Attachment attachment = forum.getAttachment();
				List fileLst = null;
				if (!(fileLst = attachment.get()).isEmpty() ) {
					session.setAttribute("filenames_" + getId(), fileLst);
					Iterator files = fileLst.iterator();
					context.put("attachments", files);
				} else {
					session.setAttribute("filenames_" + getId(), new ArrayList());
					context.put("attachments", new Vector());	
				}
			} else {
				session.setAttribute("filenames_" + getId(), new ArrayList());
				context.put("attachments", new Vector());	
			}			
			
			//get this forum childs
			Vector list = new Vector();
			boolean display = true;
			if (forum.hasChild()) {
				if ( forum.getParent() == null )
					iterate1(forum, list, session, display);
				else
					iterate1(forum, list);
			}
			
			session.setAttribute("_forum_list_" + getId(), list);
			
			if ( list.size() == 0 )
				context.put("allowReply", new Boolean(true));
			else
				context.put("allowReply", new Boolean(false));
			
			int pages =  list.size() / ROWS;
			double leftover = ((double)list.size() % (double)ROWS);
			if ( leftover > 0.0 ) ++pages;
			context.put("pages", new Integer(pages));
			session.setAttribute("pages_" + getId(), new Integer(pages));
			
		} finally {
			if ( db != null ) db.close();
		}	
		
	}	
	
	void iterate1(Forum parent, Vector items, HttpSession session, boolean display) throws Exception {
		String userid = (String) session.getAttribute("_portal_login");
		String role = (String) session.getAttribute("_portal_role");
		if ( parent.getChildCount() > 0 ) {
			Iterator replies = parent.getChildIterator();
			while(replies.hasNext()) {
				Forum forum = (Forum) replies.next();
				if ( forum.getLevel() == 1 ) display = true;
				if ( display ) {
					if ( forum.getLevel() == 1 && userid.equals(forum.getUserId()) ) 
						display = true;
					else if ( forum.getLevel() == 1 && !userid.equals(forum.getUserId()) ) {
						display = false;
					}
				}
				if (
                     "teacher"           .equals(role) 
                  || "super_teacher"     .equals(role)
                  || "assistant_teacher" .equals(role)
                ) 
                display = true;
                
				if ( display ) iterate2(forum, items, session, display);
			}
		}
	}
	
	void iterate2(Forum parent, Vector items, HttpSession session, boolean display) throws Exception {
		items.addElement(parent);
		iterate1(parent, items, session, display);
	}	
	
}
