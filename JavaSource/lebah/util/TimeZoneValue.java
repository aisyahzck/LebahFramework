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


public class TimeZoneValue implements Comparable {
    
    private String id;
    private String name;
    private int hourFromGMT;
    private int minuteFromGMT;
    private String fromGMT;
    
    public String getFromGMT() {
        return fromGMT;
    }
    public void setFromGMT(String fromGMT) {
        this.fromGMT = fromGMT;
    }
    public int getHourFromGMT() {
        return hourFromGMT;
    }
    public void setHourFromGMT(int hourFromGMT) {
        this.hourFromGMT = hourFromGMT;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getMinuteFromGMT() {
        return minuteFromGMT;
    }
    public void setMinuteFromGMT(int minuteFromGMT) {
        this.minuteFromGMT = minuteFromGMT;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public int compareTo(Object o) {
        return name.compareTo(((TimeZoneValue) o).name);
    }
    

}
