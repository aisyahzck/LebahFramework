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

package lebah.general;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;

public class CodeData {
	
	
	public static Vector getList(String table, String name) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("code");
			r.add(name);
			sql = r.getSQLSelect(table, name);
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			while ( rs.next() ) {
				Hashtable h = new Hashtable();
				h.put("code", rs.getString("code"));
				h.put("name", rs.getString(name));
				v.addElement(h);
			}
			return v;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static Vector getRaceList() throws Exception {
		return getList("race_code", "race_name");
	}
	public static Vector getNationalityList() throws Exception { 
		return getList("nationality_code", "nationality_name");
	}
	public static Vector getReligionList() throws Exception {
		return getList("religion_code", "religion_name");
	}
	public static Vector getMaritalList() throws Exception {
		return getList("marital_code", "status_name");
	}
	public static Vector getStateList() throws Exception {
		return getList("state_code", "state_name");
	}
	public static Vector getGenderList() throws Exception {
		return getList("gender_code", "gender_name");
	}
	public static Vector getDisabilityList() throws Exception {
		Vector v = new Vector();
		for ( int i=0; i < disabilityArr.length; i++ ) {
			Hashtable h = new Hashtable();
			Object[] disability = disabilityArr[i];	
			h.put("code", (String) disability[0]);
			h.put("name", (Boolean) disability[1]);
			v.addElement(h);
		}
		return v;
		
	}
	static Object[][] disabilityArr = {
		{"1", new Boolean(false)},		
		{"2", new Boolean(true)}
	};
	

}
