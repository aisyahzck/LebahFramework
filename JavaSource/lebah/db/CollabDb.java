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
package lebah.db;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class CollabDb extends lebah.db.Db {
	public CollabDb() throws DbException {
		super("dbconnection"); 	
	}	
	
	public String uid() {
		return Long.toString(lebah.db.UniqueID.get());	
	}
	
	//---
	protected String dblch(String txt, char ch){
		StringBuffer txtout = new StringBuffer("");
		if (txt != null) {
			char c;
			for (int i=0; i < txt.length(); i++) {
				c = txt.charAt(i);
				if ( c == ch) {
					txtout.append(ch).append(ch);
				}
				else {
					txtout.append(String.valueOf(c));
				}
			}
		} else {
			txtout.append("");
		}
		return txtout.toString();
	}

	protected String rep(String txt) {
		return dblch(txt, '\'');
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
}
