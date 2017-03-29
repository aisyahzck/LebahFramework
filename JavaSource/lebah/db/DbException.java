/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








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
public class DbException extends Exception
{

    String txt;

    public DbException(String s)
    {
        super(s);
        txt = s;
    }

    public String getMessage()
    {
        return txt;
    }
}
