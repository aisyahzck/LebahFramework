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

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public abstract class Validator {

	private static final int days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private static final String months[] =
		{"January","February","March","April","May",
		"June","July","August","September","October","November","December"};

	public static boolean isICNO(String ic) {
		//first remove the '-' if exist
		ic = remove(ic, '-');
		//the length must be 12
		int len = ic.length();
		if ( len != 12 ) {
			return false;
		}
		//the first six char must represent valid date value
		String strYr = ic.substring(0, 2);
		if ( Integer.parseInt(strYr) < 20 ) strYr = "20" + strYr;
		else strYr = "19" + strYr;

		int y = Integer.parseInt(strYr);
		int m = Integer.parseInt(ic.substring(2, 4));
		int d = Integer.parseInt(ic.substring(4, 6));

		if ( m > 12 ) return false;
		if ( d > 31 ) return false;

		if ( isLeapYear(y) ) days[1] = 29;
		if ( d > days[m-1]) return false;

		return true;
	}

	public static boolean isDate(int y, int m, int d) {
		if ( m > 12 ) return false;
		if ( d > 31 ) return false;
		if ( isLeapYear(y) ) days[1] = 29;
		if ( d > days[m-1]) return false;
		return true;
	}

	public static boolean isDate(String yr, String mn, String dy) {
		if ( yr.equals("") || mn.equals("") || dy.equals("") ) return false;
		int i = 0;
		for (i = 0; i < months.length; i++) {
			if ( months[i].equals(mn) ) {
				mn = Integer.toString(i+1);
				break;
			}
		}

		if ( yr.length() < 3 ) {
			if ( Integer.parseInt(yr) < 20 ) yr = "20" + yr;
			else yr = "19" + yr;
		}
		int y = Integer.parseInt(yr);
		int m = Integer.parseInt(mn);
		int d = Integer.parseInt(dy);
		return isDate(y, m, d);
	}

	public static SimpleDate getBirthDateFromICNO(String icnum) {
		String yr = icnum.substring(0, 2);
		if ( Integer.parseInt(yr) < 20 ) yr = "20" + yr;
		else yr = "19" + yr;

		int m = Integer.parseInt(icnum.substring(2, 4));
		String mn = months[m-1];
		int d = Integer.parseInt(icnum.substring(4, 6));

		return new SimpleDate(Integer.parseInt(yr), mn, d);
	}

	private static boolean isLeapYear(int year) {
		if ( year%4 != 0 ) return false;
		if ( year%4 == 0 ) return true;
		return ( year%100 != 0);
	}

	private static String remove(String str, char c) {
		StringBuffer txt = new StringBuffer(str);
		while (txt.toString().indexOf(c) > -1) {
			int pos = txt.toString().indexOf(c);
			txt.replace(pos, pos + 1, "");
		}
		return txt.toString();
	}

}
