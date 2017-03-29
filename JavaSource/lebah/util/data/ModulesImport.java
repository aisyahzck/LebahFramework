/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.util.data;

import java.io.*;

import java.util.StringTokenizer;

import lebah.db.Db;
import lebah.db.SQLRenderer;

import java.sql.Statement;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class ModulesImport {
	
	static String filename = "sql/portal_modules.dat";
	static String outfile = "sql/portal_modules.sql";
	
    public static void main(String[] args) throws Exception {
        Db db = null;
        String sql = "";
        SQLRenderer r = new SQLRenderer();
        String fields[] = new String[] {"module_group", "module_title", "module_id", "module_class", "module_description"};
        try {
            //db = new Db();
            //Statement stmt = db.getStatement();
            BufferedReader in = new BufferedReader(new FileReader(filename));
            BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
            String str;
            while ((str = in.readLine()) != null) {
                StringTokenizer tok = new StringTokenizer(str, ",");
                r.clear();
                int i = 0;
                while ( tok.hasMoreTokens() ) {
                    String s = tok.nextToken().trim();
                    r.add(fields[i], s);
                    i++;
                }
                
                sql = r.getSQLInsert("module");
                out.write(sql + ";\n");
                System.out.println(sql);
                //stmt.executeUpdate(sql);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( db != null ) db.close();
        }
    }
}
