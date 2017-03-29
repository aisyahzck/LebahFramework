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
import java.util.StringTokenizer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SimpleDate implements java.io.Serializable {
	int d;
	int m;
	int y;
	private final String months[] =
		{"January","February","March","April","May",
		"June","July","August","September","October","November","December"};

	private final String short_months[] =
		{"Jan","Feb","Mar","Apr","May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

	public SimpleDate() {
		Calendar calendar = Calendar.getInstance();
		d = calendar.get(Calendar.DAY_OF_MONTH);
		m = calendar.get(Calendar.MONTH) + 1;
		y = calendar.get(Calendar.YEAR);
	}

	public SimpleDate(int y, String mn, int d) {
		this.d = d;
		this.y = y;
		for (int i = 0; i < months.length; i++) {
			if ( months[i].equals(mn) ) {
				m = i+1;
				break;
			}
		}
	}

	public SimpleDate(String y, String mn, String d) {
		this.d = Integer.parseInt(d);
		this.y = Integer.parseInt(y);
		for (int i = 0; i < months.length; i++) {
			if ( months[i].equals(mn) ) {
				m = i+1;
				break;
			}
		}
	}
	public SimpleDate(int d, int m, int y) {
		this.d = d;
		this.m = m;
		this.y = y;
	}
	public SimpleDate(java.sql.Date sqlDate) {
		if ( sqlDate != null ) {
			String strDate = sqlDate.toString();
			StringTokenizer st = new StringTokenizer(strDate, "-");
			if ( st.hasMoreTokens() ) y = Integer.parseInt(st.nextToken().trim());
			if ( st.hasMoreTokens() ) m = Integer.parseInt(st.nextToken().trim());
			if ( st.hasMoreTokens() ) d = Integer.parseInt(st.nextToken().trim());
		} else {
			d = 1;
			m = 1;
			y = 2000;
		}
	}
	public int getDay() { return d; }
	public int getMonth() { return m; }
	public int getYear() { return y; }
	public String getMonthName() { return months[m-1]; }
	public String getShortMonthName() { return short_months[m-1]; }
	public String getDateShort() { return d + " " + short_months[m-1] + ", " + y; }
	//public String toString() { return d + " " + months[m-1] + ", " + y; }
	/**
		Return date in format YYYY-MM-DD
	*/
	
	public String toString() {
		String dd = "";
		String mm = "";
		String yyyy = "";
		if ( d < 10 ) dd = "0" + d;
		else dd = "" + d;
		if ( m < 10 ) mm = "0" + m;
		else mm = "" + m;

		String result = y + "-" + mm + "-" + dd;
		return result;
	}
	
	public String display() {
		return display(1);
	}
	
	public String display(int type) {
		if ( type == 1 )
			return getDay() + "/" + getMonth() + "/" + getYear();	
		else if ( type == 2 )
			return getDay() + " " + getShortMonthName() + " " + getYear();	
		else if ( type == 3 )
			return getDay() + " " + getMonthName() + ", " + getYear();	
		else
			return getDay() + "/" + getMonth() + "/" + getYear();	
	}
	

	public int compare(SimpleDate date_in) {
		int result = 0;
		int d1 = date_in.getDay();
		int m1 = date_in.getMonth();
		int y1 = date_in.getYear();
		if ( (d == d1) && (m == m1) && (y == y1) )
			result = 0;
		else if ( y > y1 )
			result = 1;
		else if ( y < y1 )
			result = -1;
		else if ( y == y1 ) {
			if ( m > m1) result = 1;
			else if ( m < m1) result = -1;
			else if ( m == m1) {
				if ( d > d1) result = 1;
				else if ( d < d1 ) result = -1;
			}
		}
		return result;
	}
	
    /**
    * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT 
    *  represented by this object.	
    */
	public long getTime() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(y, m-1, d);
	    return calendar.getTime().getTime();
	}

}
