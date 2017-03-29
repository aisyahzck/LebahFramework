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
public class SelectListResource {

	public SelectListResource(){
		super();
	}

	public SelectList getYear(int yr1, int yr2) {
		SelectList select = new SelectList();
		for (int i = yr1; i < yr2 + 1; i++) {
			String yr = new Integer(i).toString();
			select.add(yr, yr);
		}
		return select;
	}
	public SelectList getMonth() {
		final String monthname[] =
		{"January","February","March","April","May",
		"June","July","August","September","October","November","December"};
		SelectList select = new SelectList();
		for (int i = 1; i < 13; i++) {
			select.add(monthname[i-1], monthname[i-1]);
		}
		return select;
	}
	public SelectList getDay() {
		SelectList select = new SelectList();
		for (int i = 1; i < 32; i++) {
			String s = new Integer(i).toString();
			select.add(s, s);
		}
		return select;
	}
	public SelectList getGender() {
		SelectList select = new SelectList();
		select.add("1", "MALE");
		select.add("2", "FEMALE");
		return select;
	}
	public SelectList getMaritalStatus() {
		SelectList select = new SelectList();
		select.add("1", "SINGLE");
		select.add("2", "MARRIED");
		select.add("3", "DIVORCED");
		return select;
	}
}
