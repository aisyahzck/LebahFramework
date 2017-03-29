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
package lebah.report;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class RecordHeader {
	private int numCols;
	private int itemnum;
	private List cols;
	private boolean moreItem;

	public RecordHeader() {
		cols = new ArrayList();
		numCols = 0;
	}
	public void add(String colTitle) {
		cols.add(colTitle);
		numCols++;
		moreItem = true;
	}
	public int getCols() { return numCols; }
	public String next() {
		String data = "";
		if (itemnum < numCols) {
			data = (String) cols.get(itemnum);
			itemnum++;
			if (itemnum == numCols) moreItem = false;
		}
		return data;
	}
	public boolean hasNext() {
		return moreItem;
	}
}
