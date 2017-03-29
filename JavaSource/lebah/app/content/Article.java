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
package lebah.app.content;

import java.util.Vector;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */

public class Article {
	private String subjectId;
	private Vector textPages;
	
	public Article() {
		textPages = new Vector();
	}
	
	public void setSubjectId(String id) {
		subjectId = id;
	}
	
	public String getSubjectId() {
		return subjectId;
	}
	
	public void setText(int i, String text) {
		textPages.addElement(text);
	}
	
	public Vector getTextPages() {
		return textPages;
	}
	
	public String getText(int i) {
		if ( i < textPages.size() )
			return (String) textPages.elementAt(i);
		else
			return "NULL";
	}
}
