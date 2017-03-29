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
package lebah.mail;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

public class Hyperlinker {
	public static String convert(String txt, HttpServletRequest request) {
		String http = request == null ? "http://" : request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf("://") + 3);
		Vector v = new Vector();		
		int ind1 = 0, ind2 = 0;
		for (;;) {
			//first look for http
			if ( ind2 > 0 ) ind1 = txt.indexOf(http, ind2);
			else ind1 = txt.indexOf(http);
			if ( ind1 < 0) break;
			//then look for " " or end of file
			int k1 = txt.indexOf(" ", ind1);
			int k2 = txt.indexOf("\r", ind1);
			if ( k2 > 0 ) 
				ind2 = k1 < k2 ? k1 : k2;
			else
				ind2 = k1;
			if ( ind2 < 0 ) ind2 = txt.length();
			String str = txt.substring(ind1, ind2);
			v.addElement(str);
		}
		//construct the new string
		StringBuffer sb = new StringBuffer(txt);
		int j=0, k=0;
		for ( int i = 0; i < v.size(); i++ ) {
			String url = (String) v.elementAt(i);
			j = sb.toString().indexOf(url, k);
			String href = "<a href=\"" + url + "\" target=\"_new\"><u>";
			k = j + href.length() + url.length();
			sb.insert(j, href);
			sb.insert(k, "</u></a>");
		}
		return sb.toString();
	}
	public static void main(String args[]) {
		String txt = "This is a link http://www.unitar.edu.my/index.html to unitar and another http://www.umm.com.my/index.html to umm";
		txt += " and this http://www.yahoo.com and http://www.unitar.edu.my";
		String s = convert(txt, null);
		System.out.println(s);
	}	
}
