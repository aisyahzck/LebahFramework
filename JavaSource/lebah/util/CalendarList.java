/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.util;

import java.util.Calendar;
import java.util.Vector;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */

public class CalendarList {
    Calendar cal = Calendar.getInstance();
    
    public CalendarList()
    {
    }
    public Vector getDates()
    {
        Vector list = new Vector();
        for (int i = 1; i < 32; i++)
        {
            list.addElement(new Integer(i));
        }
        return list;
    }
    public Vector getMonths()
    {
        Vector list = new Vector();
        list.addElement("January");
        list.addElement("February");
        list.addElement("March");
        list.addElement("April");
        list.addElement("May");
        list.addElement("June");
        list.addElement("July");
        list.addElement("August");
        list.addElement("September");
        list.addElement("October");
        list.addElement("November");
        list.addElement("December");
        return list;
    }
    public Vector getYears()
    {
        Vector list = new Vector();
        int year = cal.get(Calendar.YEAR);
        for (int i = year; i < (year + 3); i++)
        {
            list.addElement(new Integer(i));
        }
        return list;
    }
}
