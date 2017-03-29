/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.db.DbForum;
import lebah.tree.Attachment;
import lebah.upload.FileLocation;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class ForumAjaxModule extends lebah.portal.AjaxBasedModule implements lebah.portal.Attributable {
	
	protected boolean isLastPage = false;
	protected int ROWS = 5;
	protected int LIST_ROWS = 10;	
	protected String idPrefix = "for";
	protected String vtlDir = "vtl/forum/ajax";

	protected String[] names = {"Rows", "Moderators", "Attachment"};
	protected Hashtable values = new Hashtable();
	
	public ForumAjaxModule() {

	}
	
	public void setIdPrefix(String str){
		idPrefix = str;
	}
	
	public String getIdPrefix() {
		return idPrefix;
	}
	
	public void setVtlDir(String str) {
		vtlDir = str;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public Hashtable getValues() {
		return values;
	}
	
	public void setValues(java.util.Hashtable hashtable) {
		values = hashtable;
	}	
	
	public String doTemplate2() throws Exception {
		HttpSession session = request.getSession();
		
		//Identify classroom
		//get the subject id from hidden field
		String subjectId = getParam("subjectId");
		context.put("subjectId", subjectId);		
		
		//everybody can reply
		context.put("allowReply", new Boolean(true));		
		String category_id = idPrefix + "_" + getId();
		ROWS = values.get(names[0]) != null ? Integer.parseInt((String) values.get(names[0])) : 5;
		LIST_ROWS = ROWS;
		String moderators = values.get(names[1]) != null ? (String) values.get(names[1]) + "," : "";

		String userid = (String) session.getAttribute("_portal_login");
		context.put("userid", userid);
		
		String _portal_role = (String) session.getAttribute("_portal_role");
	
		String inCollabModule = session.getAttribute("inCollabModule") != null ? 
						    	(String) session.getAttribute("inCollabModule") : "false";
		String collab_role = "true".equals(inCollabModule) ? session.getAttribute("_collab_role") != null ? 
								(String) session.getAttribute("_collab_role") : "" : "";
		String roleToAllow = !"".equals(collab_role) ? collab_role : _portal_role;
		
		if ( moderators.indexOf(roleToAllow + ",") == -1 ) {
			context.put("allowPost", new Boolean(false));		
			context.put("allowUpdate", new Boolean(false));
			context.put("allowDelete", new Boolean(false));
		} else {
			context.put("allowPost", new Boolean(true));		
			context.put("allowUpdate", new Boolean(true));
			context.put("allowDelete", new Boolean(true));
		}
        //always allow post 
        //context.put("allowPost", new Boolean(true));

		
		//Attachments
		String allow_attachment = values.get(names[2]) != null ? (String) values.get(names[2]) : "false";
		if ( "true".equals(allow_attachment.trim()) ) {
			context.put("allowAttachment", new Boolean(true));
		}
		else if ( "false".equals(allow_attachment.trim()) ) {
			context.put("allowAttachment", new Boolean(false));
		} else {
			context.put("allowAttachment", new Boolean(false));
		}
		
		//avatar dir
		String photoDir = FileLocation.getPhotosDir() + "photos/";
		context.put("photoDir", photoDir);		
		
		
		String template_name = vtlDir + "/list.vm";
		//String errorMsg = "";
		
		String submit = getParam("command");
        
		//System.out.println("ForumAjaxModule: " + submit);
     
		if ( "AddNewTopic".equals(submit) ) {
			session.setAttribute("mode_" + getId(), "add");
			clearData(session);
			//template_name = vtlDir + "/add.vm";
			template_name = vtlDir + "/addTopic.vm";
			//System.out.println(template_name);
		}
		else if ( "Add".equals(submit) ) {
			session.setAttribute("mode_" + getId(), "add");
			add(session, category_id);
			prepareTopicList(session, category_id);
			template_name = vtlDir + "/list.vm";
		}
		else if ( "Open".equals(submit) ) {
			session.setAttribute("page_number_" + getId(), "1");
			template_name = vtlDir + "/open.vm";
			open(session, 1, category_id);	
			getRows(session, 1);
		}
		else if ( "Reply".equals(submit) ) {
			clearData(session);
			session.setAttribute("mode_" + getId(), "reply");
			template_name = vtlDir + "/reply.vm";
			session.setAttribute("filenames_" + getId(), new Vector());
			reply(session);
		}
		else if ( "SubmitReply".equals(submit) ) {
			template_name = vtlDir + "/open.vm";
			submitReply(session, category_id);
			open(session, 1, category_id);	
			getRows(session, 1);
		}
		else if ( "Update".equals(submit) ) {
			clearData(session);
			session.setAttribute("mode_" + getId(), "update");
			template_name = vtlDir + "/update.vm";
			update(session);
		}
		else if ( "SubmitUpdate".equals(submit) ) {
			template_name = vtlDir + "/open.vm";
			submitUpdate(session);
			open(session, 1, category_id);	
			getRows(session, 1);
		}		
		else if ( "Delete".equals(submit) ) {
			template_name = vtlDir + "/open.vm";
			delete(session);
			String parent_id = getParam("parent_id");
			if ( !"0".equals(parent_id) ) {
				open(session, 1, parent_id, category_id);
				getRows(session, 1);
			}
			else {
				template_name = vtlDir + "/list.vm";
				prepareTopicList(session, category_id);			
			}
		}	
		else if ( "Next".equals(submit) ) {
			int page = session.getAttribute("page_number_" + getId()) != null ? Integer.parseInt((String) session.getAttribute("page_number_" + getId())) : 1;
			template_name = vtlDir + "/open.vm";
			//open(session, ++page);
			getRows(session, ++page);
			session.setAttribute("page_number_" + getId(), Integer.toString(page));		
		}	
		else if ( "Previous".equals(submit) ) {
			int page = session.getAttribute("page_number_" + getId()) != null ? Integer.parseInt((String) session.getAttribute("page_number_" + getId())) : 1;
			template_name = vtlDir + "/open.vm";
			page = (page == 1 ) ? 1: --page;
			//open(session, page);
			getRows(session, page);
			session.setAttribute("page_number_" + getId(), Integer.toString(page));
		}
		else if ( "GoPage".equals(submit) ) {
			int page = Integer.parseInt(getParam("pagenum"));

			template_name = vtlDir + "/open.vm";
			getRows(session, page);
			session.setAttribute("page_number_" + getId(), Integer.toString(page));			
		} 
		else if ( "NextTopic".equals(submit) ) {
			int page = session.getAttribute("topic_page_number_" + getId()) != null ? Integer.parseInt((String) session.getAttribute("topic_page_number_" + getId())) : 1;
			template_name = vtlDir + "/list.vm";
			getTopicRows(session, ++page);
			session.setAttribute("topic_page_number_" + getId(), Integer.toString(page));		
		}	
		else if ( "PreviousTopic".equals(submit) ) {
			int page = session.getAttribute("topic_page_number_" + getId()) != null ? Integer.parseInt((String) session.getAttribute("topic_page_number_" + getId())) : 1;
			template_name = vtlDir + "/list.vm";
			page = (page == 1 ) ? 1: --page;
			getTopicRows(session, page);
			session.setAttribute("topic_page_number_" + getId(), Integer.toString(page));
		}
		else if ( "GoTopicPage".equals(submit) ) {
			int page = Integer.parseInt(getParam("pagenum"));
			template_name = vtlDir + "/list.vm";
			getTopicRows(session, page);
			session.setAttribute("topic_page_number_" + getId(), Integer.toString(page));			
		} 
		else if ( "Attachment".equals(submit) ) {
			template_name = vtlDir + "/upload_files.vm";
			session.setAttribute("filenames_" + getId(), new Vector());
			keepData(session);
		}		
		else if ( "UploadFiles".equals(submit) ) {
			String upload_dir = FilesRepository.getUploadDir() + getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
			Vector filenames = uploadFile(session, upload_dir);
			session.setAttribute("filenames_" + getId(), filenames);
			template_name = vtlDir + "/reply.vm";
			String forum_id = (String) session.getAttribute("forum_id_" + getId());
			String mode = (String) session.getAttribute("mode_" + getId());
			if ( "reply".equals(mode) ) {
				template_name = vtlDir + "/reply.vm";
				reply(session, forum_id, filenames);
			}
			else {
				template_name = vtlDir + "/update.vm";
				update(session, (Vector) filenames);
			}
									
		}
		else if ( "AttachmentTopic".equals(submit) ) {
			template_name = vtlDir + "/upload_files_topic.vm";
			String forum_id = getParam("forum_id");
			context.put("forum_id", forum_id);
			keepData(session);
		}		
		else if ( "UploadFilesTopic".equals(submit) ) {
			String upload_dir = FilesRepository.getUploadDir() + getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
			Vector filenames = uploadFile(session, upload_dir);
			session.setAttribute("filenames_" + getId(), filenames);
			context.put("attachments", filenames);
			String mode = (String) session.getAttribute("mode_" + getId());
			if ( "add".equals(mode) ) {
				template_name = vtlDir + "/add.vm";
			}
			else {
				template_name = vtlDir + "/update.vm";
				update(session, (Vector) filenames);			
			}			
		
		}
		else if ( "CancelAttachmentTopic".equals(submit) ) {
			String mode = (String) session.getAttribute("mode_" + getId());
			if ( "add".equals(mode) ) {
				template_name = vtlDir + "/add.vm";
			}
			else {
				template_name = vtlDir + "/update.vm";
				update(session);
			}
		}
		else {
	
			template_name = vtlDir + "/list.vm";
			prepareTopicList(session, category_id);	
		}

		
		
		//Template template = engine.getTemplate(template_name);	
		//return template;
		return template_name;
		
	}
	
	void keepData(HttpSession session) throws Exception {
		session.setAttribute("subject", getParam("title"));
		session.setAttribute("message", getParam("message"));
	}
	
	void clearData(HttpSession session) throws Exception {
		session.setAttribute("subject", "");
		session.setAttribute("message", "");	
		session.setAttribute("filenames_" + getId(), new ArrayList());
	}	
	
	Vector uploadFile(HttpSession session, String upload_dir) throws Exception {
		//System.out.println("upload files to = " + upload_dir);
		//String upload_dir = "d:/uploadfiles/";
		java.io.File dir = new java.io.File(upload_dir);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		lebah.upload.SmartUpload2 myUpload = new lebah.upload.SmartUpload2();
		//myUpload.initialize(getServletConfig(), request, response);
		myUpload.initialize2(session.getServletContext(), request, response);
		myUpload.upload();
		int count = myUpload.save( upload_dir, lebah.upload.SmartUpload2.SAVE_PHYSICAL);
		//System.out.println("files count=" + count);
		com.jspsmart.upload.Files files = myUpload.getFiles();
		Vector filenames = new Vector();
		for ( int i=0; i < files.getCount(); i++ ) {
			com.jspsmart.upload.File file = files.getFile(i);
			String fileName = file.getFileName();
			//System.out.println(fileName);	
			if ( fileName != null && !"".equals(fileName.trim()) )filenames.addElement(fileName);
		}
		return filenames;
	}	
	
	void checkMember(HttpSession session) throws Exception {
		String memberid = (String) session.getAttribute("_portal_login");
		String name = (String) session.getAttribute("_portal_username");
		DbForum db = null;
		try {
			db = new DbForum();
			db.initializeMember(memberid, name);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	void prepareTopicList(HttpSession session, String category_id) throws Exception {
		Vector forumList = getForumList(category_id);
		
		//System.out.println("forum size=" + forumList.size());
		
		int pages =  forumList.size() / LIST_ROWS;
		double leftover = ((double)forumList.size() % (double)LIST_ROWS);
		if ( leftover > 0.0 ) ++pages;
		context.put("topic_pages", new Integer(pages));	
		session.setAttribute("topic_pages_" + getId(), new Integer(pages));
			
		session.setAttribute("_topics_list_" + getId(), forumList);
		
		getTopicRows(session, 1);
		
		session.setAttribute("topic_page_number_" + getId(), "1");
		context.put("topic_page_number", new Integer(1));
		context.put("forumList", forumList);		
	}
	
	Vector getForumList(String category_id) throws Exception {
		//String category_id = request.getParameter("category_id");
		DbForum db = null;
		try {

			db = new DbForum();
			Vector forumList = db.getRootVector(category_id);
			//System.out.println("forumList.size= " + forumList.size());
			return forumList;
		} finally {
			if ( db != null ) db.close();
		}		
		
	}
	
	void add(HttpSession session, String category_id) throws Exception {
		DbForum db = null;
		try {
			//String category_id = (String) session.getAttribute("category_id");
			String title = getParam("title");
			String message = getParam("message");
			String userid = (String) session.getAttribute("_portal_login");
			String subjectId = getParam("subjectId");
			
			System.out.println("message = " + message);
			
			Forum forum = new Forum(title);
			forum.setNotes(message);
			forum.setUserId(userid);
			forum.setCategoryId(category_id);
			forum.setClassroomId(subjectId);

			Attachment attachment = new Attachment();
			
			//TODO: This cause BUGS.. it may get previous attachment list.
			List fileList = (List) session.getAttribute("filenames_" + getId());
			if ( fileList != null ) {
				Iterator files = fileList.iterator();
				while ( files.hasNext() ) {
					String filename = (String) files.next();
					attachment.add(filename);
				}
				forum.setAttachment(attachment);
				String upload_subdir = getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
				forum.setAttachmentFolder(upload_subdir);
			}


			db = new DbForum();
			db.add(forum);
		} finally {
			if ( db !=  null ) db.close();
		}		
	}
	
	void update(HttpSession session) throws Exception {
		System.out.println("update");
		String subjectId = getParam("subjectId");
		System.out.println("subject id = " + subjectId);
		String forum_id = getParam("forum_id");
		context.put("forum_id", forum_id);
		DbForum dbForum = null;
		try {
			dbForum = new DbForum();
			Forum forum = dbForum.get(forum_id);
			System.out.println(forum.getMessage());
			context.put("forum", forum);
			if (forum.hasAttachment()) { 
				//String folder = forum.getAttachmentFolder();
				String files_dir = FilesRepository.getUploadDir() + forum.getAttachmentFolder();
				context.put("files_dir", files_dir);
				Attachment attachment = forum.getAttachment();
				List fileLst = null;
				if (!(fileLst = attachment.get()).isEmpty() ) {
					Iterator files = fileLst.iterator();
					context.put("attachments", files);
                    session.setAttribute("filenames_" + getId(), fileLst);
				} else {
					context.put("attachments", new Vector());
                    session.setAttribute("filenames_" + getId(), new Vector());
				}
			}
		} finally {
			if ( dbForum != null ) dbForum.close();
		}		
	}	
	
	void update(HttpSession session, Vector filenames) throws Exception {
		String forum_id = getParam("forum_id");
		context.put("forum_id", forum_id);
		DbForum dbForum = null;
		try {
			dbForum = new DbForum();
			Forum forum = dbForum.get(forum_id);
			context.put("forum", forum);
			if (forum.hasAttachment()) { 
				//String folder = forum.getAttachmentFolder();
				String files_dir = FilesRepository.getUploadDir() + forum.getAttachmentFolder();
				context.put("files_dir", files_dir);
				Attachment attachment = forum.getAttachment();
				List fileLst = null;
				if (!(fileLst = attachment.get()).isEmpty() ) {
					//Iterator files = fileLst.iterator();
					if ( filenames != null && filenames.size() > 0 ) {
						for (int i=0; i < filenames.size(); i++ ){
							fileLst.add(filenames.elementAt(i));
						}
					}					
					context.put("attachments", fileLst);
					session.setAttribute("filenames_" + getId(), fileLst);
					
				} else {
					if ( filenames != null && filenames.size() > 0 ) {
						context.put("attachments", filenames);	
						session.setAttribute("filenames_" + getId(), filenames);
					} else {
						context.put("attachments", new Vector());	
						session.setAttribute("filenames_" + getId(),  new Vector());							
					}					
				}
			} 
			else if ( filenames != null && filenames.size() > 0 ) {
				context.put("attachments", filenames);	
				session.setAttribute("filenames_" + getId(), filenames);
			}
			
		} finally {
			if ( dbForum != null ) dbForum.close();
		}		
	}	
	
	String[] removeAttachment(HttpSession session) throws Exception {
		String[] filenames = request.getParameterValues("attachfiles");
        
		return filenames;
	}
	
	void submitUpdate(HttpSession session) throws Exception {
		DbForum db = null;
		try {
			String forum_id = getParam("forum_id");
			context.put("forum_id", forum_id);
			String title = getParam("title");
			String message = getParam("message");
			String subjectId = getParam("subjectId");
			//String userid = (String) session.getAttribute("_portal_login");
			
			Forum forum = new Forum(title);
			forum.setId(forum_id);
			forum.setNotes(message);
			forum.setClassroomId(subjectId);
			//remove attachment first
			//String[] removefiles = removeAttachment(session);
            String[] removefiles = request.getParameterValues("attachfiles");
            /*
            if ( removefiles != null ) {
                System.out.println("got files to removed");
            }
            else {
                System.out.println("no files to removed");
            }
            */
			Attachment attachment = new Attachment();
			
            List fileList = (List) session.getAttribute("filenames_" + getId());
			//System.out.println("fileList=" + fileList.size());
            
			if ( fileList != null ) {
				if ( removefiles != null ) {
					for ( int i=0; i < removefiles.length; i++ ) {
						fileList.remove(removefiles[i]);
					}				
				}
				Iterator files = fileList.iterator();
				while ( files.hasNext() ) {
					String filename = (String) files.next();
					attachment.add(filename);
				}
				forum.setAttachment(attachment);
				String upload_subdir = getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
				forum.setAttachmentFolder(upload_subdir);
			}

			db = new DbForum();
			//if ( removefiles != null && removefiles.length > 0 )
				db.update(forum, removefiles);
			//else
			//	db.update(forum);
		} finally {
			if ( db !=  null ) db.close();
		}		
	}	
	

	void open(HttpSession session, int page, String category_id) throws Exception {
		open(session, page, "", category_id);	
	}
	
	void open(HttpSession session, int page, String id, String category_id) throws Exception {
		
		//System.out.println("page=" + page);
		context.put("page_number", new Integer(page));
		
		//String category_id = getParam("category_id");
		
		System.out.println("forum_id=" + getParam("forum_id"));
		
		String forum_id = !"".equals(id) ? id : getParam("forum_id");
		context.put("forum_id", forum_id);
		session.setAttribute("forum_id_" + getId(), forum_id);
		
		//String dir = FileLocation.getPATH() + "photos/" + matricNo + "/thumb";
		String photoDir = FileLocation.getPhotosDir() + "photos/";
		context.put("photoDir", photoDir);
		
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
					//while ( files.hasNext() ) {
					//	String filename = (String) files.next();
					//}
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
			if (forum.hasChild()) {
				iterate1(forum, list);
			}
			
			session.setAttribute("_forum_list_" + getId(), list);
			
			int pages =  list.size() / ROWS;
			double leftover = ((double)list.size() % (double)ROWS);
			if ( leftover > 0.0 ) ++pages;
			context.put("pages", new Integer(pages));
			session.setAttribute("pages_" + getId(), new Integer(pages));

			//Vector items = getPage(page, ROWS, list);
			//context.put("forum_items", items);
			
			
		} finally {
			if ( db != null ) db.close();
		}	
		
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
	
	void getRows(HttpSession session, int page) throws Exception {
		//System.out.println("pagenum=" + page);
		Vector list = (Vector) session.getAttribute("_forum_list_" + getId());
		//System.out.println("list size= " + list.size());
		Vector items = getPage(page, ROWS, list);
		context.put("forum_items", items);	
		context.put("page_number", new Integer(page));	
		context.put("pages", (Integer) session.getAttribute("pages_" + getId()));

	}
	
	Vector getTopicPage(int page, int size, Vector list) throws Exception {
		int elementstart = (page - 1) * size;
		int elementlast = 0;
		//int elementlast = page * size < list.size() ? (page * size) - 1 : list.size() - 1;
		if ( page * size < list.size() ) {
			elementlast = (page * size) - 1;
			isLastPage = false;
			context.put("topic_eol", new Boolean(false));
		} else {
			elementlast = list.size() - 1;
			isLastPage = true;
			context.put("topic_eol", new Boolean(true));
		}
		if ( page == 1 ) context.put("topic_bol", new Boolean(true));
		else context.put("topic_bol", new Boolean(false));
		Vector v = new Vector();
		for ( int i = elementstart; i < elementlast + 1; i++ ) {
			v.addElement(list.elementAt(i));
		}
		
		return v;
	}	
	
	void getTopicRows(HttpSession session, int page) throws Exception {
		Vector list = (Vector) session.getAttribute("_topics_list_" + getId());
		Vector items = getTopicPage(page, LIST_ROWS, list);
		context.put("topic_items", items);	
		context.put("topic_page_number", new Integer(page));
		context.put("topic_pages", (Integer) session.getAttribute("topic_pages_" + getId()));			
	}	
	
	
	void iterate1(Forum parent, Vector items) throws Exception {
		if ( parent.getChildCount() > 0 ) {
			Iterator replies = parent.getChildIterator();
			while(replies.hasNext()) {
				Forum forum = (Forum) replies.next();
				iterate2(forum, items);
			}
		}
	}
	
	void iterate2(Forum parent, Vector items) throws Exception {
		items.addElement(parent);
		iterate1(parent, items);
	}
	
	void reply(HttpSession session) throws Exception {
		String forum_id = getParam("forum_id");
		context.put("forum_id", forum_id);
		session.setAttribute("forum_id_" + getId(), forum_id);		
		DbForum dbForum = null;
		try {
			dbForum = new DbForum();
			Forum forum = dbForum.get(forum_id);
			context.put("forum", forum);
		} finally {
			if ( dbForum != null ) dbForum.close();
		}		
	}	
	
	void reply(HttpSession session, String forum_id) throws Exception {
		DbForum dbForum = null;
		try {
			dbForum = new DbForum();
			Forum forum = dbForum.get(forum_id);
			context.put("forum", forum);
		} finally {
			if ( dbForum != null ) dbForum.close();
		}		
	}
	
	void reply(HttpSession session, String forum_id,  Vector filenames) throws Exception {
		DbForum dbForum = null;
		try {
			dbForum = new DbForum();
			Forum forum = dbForum.get(forum_id);
			context.put("forum", forum);
			/*
			if (forum.hasAttachment()) { 
				//String folder = forum.getAttachmentFolder();
				String files_dir = FilesRepository.getUploadDir() + forum.getAttachmentFolder();
				context.put("files_dir", files_dir);
				Attachment attachment = forum.getAttachment();
				List fileLst = null;
				if (!(fileLst = attachment.get()).isEmpty() ) {
					//Iterator files = fileLst.iterator();
					if ( filenames != null && filenames.size() > 0 ) {
						for (int i=0; i < filenames.size(); i++ ){
							fileLst.add(filenames.elementAt(i));
						}
					}					
					context.put("attachments", fileLst);
					session.setAttribute("filenames_" + getId(), fileLst);
					
				} else {
					if ( filenames != null && filenames.size() > 0 ) {
						context.put("attachments", filenames);	
						session.setAttribute("filenames_" + getId(), filenames);
					} else {
						context.put("attachments", new Vector());	
						session.setAttribute("filenames_" + getId(), new Vector());						
					}
				}
			} 
			*/	
			if ( filenames != null && filenames.size() > 0 ) {
				context.put("attachments", filenames);	
				session.setAttribute("filenames_" + getId(), filenames);
			} else {
				context.put("attachments", new Vector());	
				session.setAttribute("filenames_" + getId(), new Vector());						
			}					
		} finally {
			if ( dbForum != null ) dbForum.close();
		}		
	}	

	
	
	void submitReply(HttpSession session, String category_id) throws Exception {
		String forum_id = getParam("forum_id");
		context.put("forum_id", forum_id);
		String title = getParam("title");
		String notes = getParam("message");
		//String category_id = getParam("category_id");
		String user_id = (String) session.getAttribute("_portal_login");
		
		//System.out.println("user_id = " + user_id);
		

		DbForum db = null;
		try {
			Forum forum = new Forum(title);
			forum.setNotes(notes);
			forum.setUserId(user_id);
			forum.setCategoryId(category_id);
			
			Attachment attachment = new Attachment();
			List fileList = (List) session.getAttribute("filenames_" + getId());
			if ( fileList != null ) {
				Iterator files = fileList.iterator();
				while ( files.hasNext() ) {
					String filename = (String) files.next();
					attachment.add(filename);
				}
				forum.setAttachment(attachment);
				String upload_subdir = getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
				forum.setAttachmentFolder(upload_subdir);
			}
			
			db = new DbForum();
			db.add(forum_id, forum);
		} finally {
			if ( db != null ) db.close();
		}
	
	}
	
	void delete(HttpSession session) throws Exception {
		String forum_id = getParam("forum_id");
		context.put("forum_id", forum_id);
		DbForum db = null;
		try {
			db = new DbForum();
			db.delete(forum_id);
		} finally {
			if ( db != null ) db.close();
		}		
	}	
	

}
