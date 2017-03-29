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

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Property {
	private String color;
	private String bgcolor;
	private String fontweight;
	public Property() {
		color = "";
		bgcolor = "";
		fontweight = "normal";
	}
	public void setColor(String color) { this.color = color; }
	public String getColor() { return color; }
	public void setBgColor(String bgcolor) { this.bgcolor = bgcolor; }
	public String getBgColor() { return bgcolor; }
	public void setFontWeight(String fontweight) { this.fontweight = fontweight; }
	public String getFontWeight() { return fontweight; }
}
