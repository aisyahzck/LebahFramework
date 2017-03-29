/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.db.Db;
import lebah.db.DbForum;
import lebah.tree.Attachment;

import org.apache.velocity.Template;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MessageModule extends ForumModule {
	private String categoryId;
	private String subjectId;
	private HttpSession session;
	private String className = this.getClass().getName();
	
	public MessageModule() {
		idPrefix = "mes_";
		vtlDir = "vtl/message";
		LIST_ROWS = ROWS;
		names = new String[] {"Rows", "Moderators", "Attachment", "StartAsList", "ShowSubject"};
	}
	
	public Template doTemplate() throws Exception {
		session = request.getSession();
		
		//Identify classroom
		//get the subject id from hidden field
		subjectId = getParam("subject_id_"+getId());
		if(subjectId == null) {
			subjectId = "";
		}
		//System.out.println("["+className+"] subjectId = "+subjectId);
		context.put("subjectId", subjectId);
		
		//the module identification
		context.put("formname", getId().replace(" ", ""));
		
		//everybody can reply
		context.put("allowReply", new Boolean(true));		
		
		categoryId = idPrefix + "_" + getId();
		//System.out.println("["+className+"] categoryId = "+categoryId);
		
		ROWS = values.get(names[0]) != null ? Integer.parseInt((String) values.get(names[0])) : 5;
		LIST_ROWS = ROWS;
		
		String moderators = values.get(names[1]) != null ? (String) values.get(names[1]) + "," : "";
		String _portal_role = (String) session.getAttribute("_portal_role");
		//System.out.println("["+className+"] _portal_role = "+_portal_role);
		
		String inCollabModule = session.getAttribute("inCollabModule") != null ? 
								(String) session.getAttribute("inCollabModule") : "false";
		String collab_role = "true".equals(inCollabModule) ? session.getAttribute("_collab_role") != null ? 
								(String) session.getAttribute("_collab_role") : "" : "";
		String roleToAllow = !"".equals(collab_role) ? collab_role : _portal_role;		

		if ( moderators.indexOf(roleToAllow + ",") > -1 ) {
			context.put("allowPost", new Boolean(true));		
			context.put("allowUpdate", new Boolean(true));
			context.put("allowDelete", new Boolean(true));
		} else {
			context.put("allowPost", new Boolean(false));		
			context.put("allowUpdate", new Boolean(false));
			context.put("allowDelete", new Boolean(false));
		}
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
		//List
		boolean asList = false;
		String startAsList = values.get(names[3]) != null ? (String) values.get(names[3]) : "false";
		if ( "true".equals(startAsList.trim()) ) {
			context.put("startAsList", new Boolean(true));
			asList = true;
		}
		else if ( "false".equals(startAsList.trim()) ) {
			context.put("startAsList", new Boolean(false));
			asList = false;
		} else {
			context.put("startAsList", new Boolean(false));
			asList = false;
		}
		
		String showSubject = values.get(names[4]) != null ? (String) values.get(names[4]) : "false";
		
		if ( "true".equals(showSubject) ) 
		    context.put("showSubject", new Boolean(true));
		else
		    context.put("showSubject", new Boolean(false));

		context.put("rows_count", new Integer(LIST_ROWS));	
		
		
		//String template_name = vtlDir + "/list.vm";
		String template_name = vtlDir + "/list_by_week.vm";
		//String errorMsg = "";
		
		String submit = getParam("command_"+getId());
		String formname = getParam(getId());
		
		if(submit == null) {
			submit = "";
		} else {
			if(!formname.equals(getId())) {
				submit = "";
			}
		}
		//System.out.println("["+className+"] command = "+submit);
		

		if ( "AddNewTopic".equals(submit) ) {
			session.setAttribute("mode_" + getId(), "add");
			clearData(session);
			template_name = vtlDir + "/add.vm";
		}
		else if ( "Add".equals(submit) ) {
			
			template_name = vtlDir + "/open.vm";	
			session.setAttribute("mode_" + getId(), "add");
			add(session, categoryId);
			prepareTopicList(session, categoryId);	
			if ( !showMessage(session, categoryId, 1)) template_name = vtlDir + "/list.vm";
			
			session.setAttribute("mode_" + getId(), "add");
			template_name = openMessage();
		}
		else if ( "Open".equals(submit) ) {
			/*
			int i = getParam("item") != null && !"".equals(getParam("item")) ? 
				Integer.parseInt(getParam("item")) : 
				session.getAttribute("message_page_" + getId()) != null ?
				((Integer) session.getAttribute("message_page_" + getId())).intValue() : 1;
			session.setAttribute("message_page_" + getId(), new Integer(i));
			if ( !showMessage(session, categoryId, i) ) {
				//template_name = vtlDir + "/list.vm";
				template_name = vtlDir + "/list_by_week.vm";
			}
			*/
			template_name = openMessage();
		}
		else if ( "Update".equals(submit) ) {
			String selectedWeek = request.getParameter("selected_week_"+getId());
			context.put("selectedWeek", selectedWeek);
			
			clearData(session);
			session.setAttribute("mode_" + getId(), "update");
			template_name = vtlDir + "/update.vm";
			update(session);
		}
		else if ( "SubmitUpdate".equals(submit) ) {
			submitUpdate(session);
			template_name = openMessage();
		}		
		else if ( "Delete".equals(submit) ) {
			template_name = vtlDir + "/open.vm";
			delete(session);
			String parent_id = getParam("parent_id");
			template_name = vtlDir + "/list.vm";
			prepareTopicList(session, categoryId);			
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
		}		
		else if ( "UploadFiles".equals(submit) ) {
			String upload_dir = FilesRepository.getUploadDir() + getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
			Collection filenames = uploadFile(session, upload_dir);
			session.setAttribute("filenames_" + getId(), filenames);
			template_name = vtlDir + "/reply.vm";
			String forum_id = (String) session.getAttribute("forum_id_" + getId());
			reply(session, forum_id);			
		}
		else if ( "AttachmentTopic".equals(submit) ) {
			String selectedWeek = request.getParameter("selected_week_"+getId());
			context.put("selectedWeek", selectedWeek);
			
			template_name = vtlDir + "/upload_files_topic.vm";
			String forum_id = getParam("forum_id");
			context.put("forum_id", forum_id);			
			keepData(session);
		}		
		else if ( "UploadFilesTopic".equals(submit) ) {
			String upload_dir = FilesRepository.getUploadDir() + getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
			//System.out.println("["+className+"] upload_dir = "+upload_dir);
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
		else if ( "RemoveAttachment".equals(submit) ) {
			template_name = vtlDir + "/remove_attachment.vm";
		}	
		else if ( "SubmitRemoveAttachment".equals(submit) ) {
			template_name = vtlDir + "/open.vm";
			int i = getParam("item") != null && !"".equals(getParam("item")) ? 
				Integer.parseInt(getParam("item")) : 
				session.getAttribute("message_page_" + getId()) != null ?
				((Integer) session.getAttribute("message_page_" + getId())).intValue() : 1;
			session.setAttribute("message_page_" + getId(), new Integer(i));
			if ( !showMessage(session, categoryId, i) ) template_name = vtlDir + "/list.vm";		
		}
		else if ( "ListTopics".equals(submit) ) {
			template_name = vtlDir + "/list.vm";
			prepareTopicList(session, categoryId);				
		}
		else if ( "GoPage".equals(submit) ) {
			template_name = vtlDir + "/open.vm";					
			int page = !"".equals(getParam("page")) ? Integer.parseInt(getParam("page")) : 1;
			if ( !showMessage(session, categoryId, page) ) template_name = vtlDir + "/list.vm";
		} 	
		else if ( "openContent".equals(submit) ) {
			template_name = vtlDir + "/open.vm";
		}
		else {
			/*
			prepareTopicList(session, categoryId);	
			if ( asList ) template_name = vtlDir + "/list.vm";		
			else {
				template_name = vtlDir + "/open.vm";		
				if ( !showMessage(session, categoryId, 1) ) template_name = vtlDir + "/list.vm";
			}
			*/
			template_name = vtlDir + "/list_by_week.vm";
			String selectedWeek = request.getParameter("selected_week_"+getId());
			Vector list = getTitleListByWeek(categoryId, selectedWeek);
			context.put("topicList", list);
		}
		
		Template template = engine.getTemplate(template_name);	
		return template;		
	}
	
	void keepData(HttpSession session) throws Exception {
		session.setAttribute("subject", getParam("title"));
		session.setAttribute("message", getParam("message"));
	}
	
	void clearData(HttpSession session) throws Exception {
		session.setAttribute("subject", "");
		session.setAttribute("message", "");	

		session.setAttribute("filenames_" + getId(), new Vector());		
	}
	
	
	boolean showMessage(HttpSession session, String categoryId, int message_page) throws Exception {
		Vector topicList = (Vector) session.getAttribute("_topics_list_" + getId());
		if ( topicList.size() == 0 ) {
			return false;	
		}
		int total_pages = topicList.size();
		int next_page = message_page + 1;
		int prev_page = message_page - 1;
		Forum item = (Forum) topicList.elementAt(message_page - 1);

		//System.out.println("ROWS=" + LIST_ROWS);
		context.put("total_pages", new Integer(total_pages));
		context.put("message_page", new Integer(message_page));
		context.put("next_page", new Integer(next_page));
		context.put("prev_page", new Integer(prev_page));
		context.put("hasPrevious", prev_page == 0 ? new Boolean(false) : new Boolean(true));
		context.put("hasNext", next_page > total_pages ? new Boolean(false) : new Boolean(true));
		open(session, item.getId(), categoryId);			
		return true;
	}
	
	boolean openContent(HttpSession session, String content_id) throws Exception {
		//Forum item = (Forum) topicList.elementAt(message_page - 1);
		//open(session, item.getId(), categoryId);			
		return true;
	}	
	
	void open(HttpSession session, String id, String categoryId) throws Exception {
		
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
			
			context.put("uploadDir", FilesRepository.getUploadDir());
			if (forum.hasAttachment()) { 
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
			
		} finally {
			if ( db != null ) db.close();
		}	
		
	}	
	
	
	
	void open(HttpSession session, int page, String id, String categoryId) throws Exception {
		
		context.put("page_number", new Integer(page));
		
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
			
			context.put("uploadDir", FilesRepository.getUploadDir());
			if (forum.hasAttachment()) { 
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
			}			
			
			//THIS WON'T HAPPENED FOR THIS MODULE
			//get this forum childs
			Vector list = new Vector();
			if (forum.hasChild()) {
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
	
	private String openMessage() throws Exception {
		String messageId = request.getParameter("message_id_"+getId());
		String selectedWeek = request.getParameter("selected_week_"+getId());
		Message message = getMessage(categoryId, messageId);
		context.put("message", message);
		context.put("listOfAttachments", message.getAttachment());
		context.put("selectedWeek", selectedWeek);
		
		return vtlDir + "/open.vm";
	}
	/**
	 * This method will get a list of announcement titles based on 7 days (1 week)
	 * duration. By default it will get announcement for the current week. 
	 * Users can then choose to view previous weeks announcements.
	 * @param categoryId
	 * @return a vector containing a list of Message objects.
	 * @throws Exception
	 */
	private Vector<Message> getTitleListByWeek(String categoryId, 
			String selectedWeek) throws Exception {
		//System.out.println("["+className+"] selectedWeek = "+selectedWeek);
		if(selectedWeek == null) {
			selectedWeek = "0";
		}
		context.put("selectedWeek", selectedWeek);
		
		Vector<Message> list = new Vector<Message>();
		Db db = null;
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		Date dateFrom = null;
		Date dateUntil = null;
		
		int noOfDaysFromToday = Integer.parseInt(selectedWeek) * 7;
		
		if (noOfDaysFromToday == 0) {
			dateUntil = today;
		} else {
			cal.add(Calendar.DATE, -noOfDaysFromToday);
			dateUntil = cal.getTime();
		}
		cal.add(Calendar.DATE, -6);
		dateFrom = cal.getTime();
		
		//System.out.println("["+className+"] dateFrom = "+dateFrom.toString());
		//System.out.println("["+className+"] dateUntil = "+dateUntil.toString());
		
		try {
			db = new Db();
			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			String strDateFrom = df.format(dateFrom);
			//System.out.println("["+className+"] strDateFrom = " + strDateFrom);
			String strDateUntil = df.format(dateUntil);
			//System.out.println("["+className+"] strDateUntil = " + strDateUntil);
			
			String sql = "SELECT id, posted_date, title FROM forum WHERE " +
					"category_id = ? AND posted_date BETWEEN ? AND ? " +
					"ORDER BY posted_date DESC";
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, categoryId);
			ps.setString(2, strDateFrom);
			ps.setString(3, strDateUntil);
			
			ResultSet rs = ps.executeQuery();
			Message headline = null;
			while (rs.next()) {
				headline = new Message();
				headline.setId(rs.getString("id"));
				headline.setDateOfPost(rs.getString("posted_date"));
				headline.setTitle(rs.getString("title"));
				
				list.add(headline);
			}
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return list;
	}
	
	/**
	 * This method will get the message content/text for display.
	 * @param messageId
	 * @param categoryId
	 * @return Message object.
	 * @throws Exception
	 */
	private Message getMessage(String categoryId, String messageId) throws Exception {
		Message message = null;
		Db db = null;
		try {
			db = new Db();
			String sql = "SELECT posted_date, title, message_text " +
					"FROM forum WHERE category_id = ? AND id = ?";
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, categoryId);
			ps.setString(2, messageId);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				message = new Message();
				message.setId(messageId);
				message.setTitle(rs.getString("title"));
				message.setContent(rs.getString("message_text"));
				message.setDateOfPost(rs.getString("posted_date"));
			}
			
			// Get attachments if any.
			sql = "SELECT file_name, directory " +
					"FROM forum_attachment WHERE forum_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, messageId);
			
			rs = ps.executeQuery();
			String attachmentDir = null;
			while ( rs.next() ) {
				message.setAttachment(rs.getString("file_name"));
				if (attachmentDir == null) {
					attachmentDir = rs.getString("directory");
				}
			}
			message.setAttachmentFolder(attachmentDir);
			
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return message;
	}
	
	private void addMessage(String categoryId, String userId, String title, 
			String message, String subjectId) throws Exception {
		DbForum db = null;
		try {
			//String category_id = (String) session.getAttribute("category_id");
			//String title = getParam("title");
			//String message = getParam("message");
			//String userid = (String) session.getAttribute("_portal_login");
			//String subjectId = getParam("subjectId");
			
			Forum forum = new Forum(title);
			forum.setNotes(message);
			forum.setUserId(userId);
			forum.setCategoryId(categoryId);
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
}
