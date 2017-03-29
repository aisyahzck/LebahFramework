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
package lebah.util;


import java.util.Vector;

import javax.servlet.http.HttpSession;



/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
 
public class Paging {
	private HttpSession session;
	private boolean isFirstPage, isLastPage;
	private int LIST_ROWS = 10;	
	private int currentPage, totalPages, topNumber, lastNumber;

	public Paging(HttpSession session, Vector list, int rows) {
		this.session = session;
		LIST_ROWS = rows;
		totalPages = prepareList(list);
	}	
	
	public Paging(HttpSession session, int rows) {
		this.session = session;
		LIST_ROWS = rows;
	}
	
	public Paging(HttpSession session) {
		this.session = session;
	}	
	
	public int prepareList(Vector list) {
		int pages =  list.size() / LIST_ROWS;
		double leftover = ((double)list.size() % (double)LIST_ROWS);
		if ( leftover > 0.0 ) ++pages;
		session.setAttribute("pagingList", list);
		totalPages = pages;
		return pages;	
	}	
	
	public int prepareList(Vector list, int rows){
		LIST_ROWS = rows;
		int pages =  list.size() / LIST_ROWS;
		double leftover = ((double)list.size() % (double)LIST_ROWS);
		if ( leftover > 0.0 ) ++pages;
		session.setAttribute("pagingList", list);
		return pages;	
	}	
	
	public Vector getPage(int page) {
		currentPage = page;
		Vector list = (Vector) session.getAttribute("pagingList");
		return getPage(page, LIST_ROWS, list);
	}
	
	public int getCurrentPage() {
		return currentPage;
	}	
	
	public Vector goNextPage()  {
		return getPage(++currentPage);	
	}
	
	public Vector goPreviousPage(){
		return getPage(--currentPage);	
	}
	
	public Vector goFirstPage()  {
		return getPage(1);
	}
	
	public Vector goLastPage() {
		return getPage(totalPages);
	}
	
	public boolean eol() {
		return isLastPage;
	}
	
	public boolean bol() {
		return isFirstPage;
	}
	
	public int getTopNumber() {
	    return topNumber;
	}
	
	public int getLastNumber() {
	    return lastNumber;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	Vector getPage(int page, int size, Vector list) {
		int elementstart = (page - 1) * size;
		int elementlast = 0;
		if ( page * size < list.size() ) {
			elementlast = (page * size) - 1;
			isLastPage = false;
		} else {
			elementlast = list.size() - 1;
			isLastPage = true;
		}
		if ( page == 1 ) {
			isFirstPage = true;
		}
		else {
			isFirstPage = false;
		}
		Vector v = new Vector();
		for ( int i = elementstart; i < elementlast + 1; i++ ) {
			v.addElement(list.elementAt(i));
		}
		topNumber = elementstart;
		lastNumber = elementlast;
		return v;
	}
	
	

	
}	
