/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */
package lebah.app;
import java.util.Date;

import lebah.tree.Category;
import lebah.tree.Tree2;
import lebah.util.SimpleDate;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Forum extends Tree2  {
	private String category_id;
	private String category_name;
	private Category category;
	private String user_id;
	private SimpleDate date_posted;
	private String posted_by;
	private int replyCount;
	private String avatar;
	private String classroomId;
	private int rate;
	
	private Date postedDate;

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	

	public Forum(String title) {
		super(title);
	}

	public void setCategoryId(String id) { category_id = id; }
	public String getCategoryId() { return category_id; }
	public void setCategoryName(String s) { category_name = s; }
	public String getCategoryName() { return category_name; }
	public void setCategory(Category cat) { category = cat; }
	public Category getCategory() { return category; }
	public void setUserId(String id) { user_id = id; }
	public String getUserId() { return user_id; }
	public void setDatePosted(SimpleDate date) { date_posted = date; }
	public SimpleDate getDatePosted() { return date_posted; }
	public void setPostedBy(String s) { posted_by = s; }
	public String getPostedBy() { return posted_by; }
	public String displayMessage() {
		return putLineBreak(getNotes());
	}
	public String getMessage() {
		return putLineBreak(getNotes());
	}
	
	public String getMessage(int numOfChars) {
		String s = stripHtmlTags(getNotes());
		int len = s.length();
		numOfChars = numOfChars > len ? len : numOfChars;
		return putLineBreak(s.substring(0, numOfChars));
	}
	
	public static String putLineBreak(String str) {
		StringBuffer txt = new StringBuffer(str);
		char c = '\n';
		while (txt.toString().indexOf(c) > -1) {
			int pos = txt.toString().indexOf(c);
			txt.replace(pos, pos + 1, "<br>");
		}
		return txt.toString();
	}
	
	public static String stripHtmlTags(String str) {
		StringBuffer s = new StringBuffer("");
		boolean b = true;
		for ( int i=0; i < str.length(); i++ ) {
			char c = str.charAt(i);
			if ( b && c == '<' ) {
				b = false;
			}
			if (b) s.append(c);
			if ( !b && c == '>' ) {
				b = true;
			}			
		}
		
		return s.toString();
	}

	/**
	 * @return Returns the replyCount.
	 */
	public int getReplyCount() {
		return replyCount;
	}

	/**
	 * @param replyCount The replyCount to set.
	 */
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	
	public static void main(String[] args) {
		String s = "<p>Hello <i>World</i></p>";
		System.out.println(stripHtmlTags(s));
	}

	public String getClassroomId() {
		return classroomId != null ? classroomId:"";
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	
	

}
