/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
/*
 * Created on Apr 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lebah.db;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestDelegator {
    public static void main(String[] args) throws Exception {
        //testSelect();
        //testInsert();
        //testUpdate();
        testDelete();
    }
    
    public static void testSelect() throws Exception {
		Vector data = new Vector();
		Hashtable where = new Hashtable();
		data.addElement("id");
		data.addElement("name");
		where.put("id", "Q0001");
		DbDelegator delegator = new DbDelegator();
		Vector results = delegator.select("student", data, where);
		for ( int i=0; i < results.size(); i++) {
			Hashtable h = (Hashtable) results.elementAt(i);
			String id = (String) h.get("id");
			String name = (String) h.get("name");
			System.out.println(id + " = " + name);
		}        
    }
    
    public static void testInsert() throws Exception {
        Hashtable data = new Hashtable();
        data.put("id", "001");
        data.put("name", "Shaiful");
        data.put("address", "Unknown");
        DbDelegator delegator = new DbDelegator();
        delegator.insert("test", data);
    }
    
    public static void testUpdate() throws Exception {
        Hashtable data = new Hashtable();
        data.put("name", "Shaiful Nizam");
        data.put("address", "Unknown Places");
        Hashtable where = new Hashtable();
        where.put("id", "001");
        DbDelegator delegator = new DbDelegator();
        delegator.update("test", data, where);
    }
    
    public static void testDelete() throws Exception {
        Hashtable data = new Hashtable();
        data.put("id", "001");
        DbDelegator delegator = new DbDelegator();
        delegator.delete("test", data);
    }    
    
}
