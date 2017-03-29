/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or





but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.repository;

import java.util.*;

import lebah.util.*;
/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Item {

	String id, name, groupId, fileName, thumbFilename, description;
	String userId;
	String userName;
	Date uploadDate;
	String uploadDateStr;
	ItemType type;
	String title;
	String facultyCode;
	String urlPath;
	
	
	public String getPath() {
		return urlPath;
	}

	public void setPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getFacultyCode() {
		return facultyCode;
	}

	public void setFacultyCode(String facultyCode) {
		this.facultyCode = facultyCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUploadDateStr() {
		return DateTool.getDateFormatted(uploadDate);
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setId(String s) {
		id = s;
	}
	
	public void setGroupId(String s){
		groupId = s;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public void setFileName(String s) {
		fileName = s;
	}
	
	public void setDescription(String s) {
		description = s;
	}
	
	public String getId() {
		return id;
	}
	
	public String getGroupId(){
		return groupId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getDescription() {
		return description;
	}

	/**
	 * @return Returns the thumbFilename.
	 */
	public String getThumbFilename() {
		return thumbFilename;
	}

	/**
	 * @param thumbFilename The thumbFilename to set.
	 */
	public void setThumbFilename(String thumbFilename) {
		this.thumbFilename = thumbFilename;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}
	
	public String getTypeName() {
		if ( type == ItemType.Image ) return "Image";
		else if ( type == ItemType.Interactive ) return "Interactive";
		else if ( type == ItemType.Flash ) return "Flash";
		else if ( type == ItemType.Audio ) return "Audio";
		return "";
	}
	public boolean equals(Object o) {
		Item item = (Item)o;
		if (this.id.equals(item.id)) return true;
		return false;
	}

}
