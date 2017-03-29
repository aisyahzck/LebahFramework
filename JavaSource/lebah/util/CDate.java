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

import java.util.Date;
import java.util.Calendar;

public class CDate extends Date {
	Calendar calendar;
	
	public CDate() {
		super(new Date().getTime());
		this.calendar = Calendar.getInstance();
		calendar.setTime(new Date());		
	}
	
	public CDate(Date date) {
		super(date.getTime());
		this.calendar = Calendar.getInstance();
		calendar.setTime(date);
	}
	
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	//month is 1 based (not zero based)
	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	public int getDay() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public boolean equals(Object o) {
		if ( o instanceof CDate ) {
			CDate d = (CDate) o;
			if ( getYear() == d.getYear() && getMonth() == d.getMonth() && getDay() == d.getDay() ) {
				return true;
			}
			else
				return false;
		} else {
			return false;
		}
			
	}
	
	public String toString() {
		return calendar.getTime().toString();
	}
	
}
