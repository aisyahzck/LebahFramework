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

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Group {

	String id, name, categoryId, categoryName;
	Category category;
	
	public void setId(String s) {
		id = s;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public void setCategoryId(String s) {
		categoryId = s;
	}
	
	public void setCategoryName(String s) {
		categoryName = s;
	}
	
	
	public void setCategory(Category c) {
		category = c;
	}
	
	public String getId() {
		return id;
	}
	
	
	public String getName() {
		return name;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	
	public Category getCategory() {
		return category;
	}
	

}
