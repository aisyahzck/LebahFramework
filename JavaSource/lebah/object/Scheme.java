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
package lebah.object;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class Scheme
{
    private String name;
    private String title;
    
    public Scheme()
    {
        name = "";
        title = "";
    }
    
/**
 * Setter method to set the String attribute, <i>name</i>.
 */
    public void setName(String name)
    {
        if (name == null)
        {
            this.name = "";
        } else {
            this.name = name;
        }
    }
    
/**
 * Getter method to get the String attribute, <i>name</i>.
 */
    public String getName()
    {
        return name;
    }

/**
 * Setter method to set the String attribute, <i>title</i>.
 */
    public void setTitle(String title)
    {
        if (title == null)
        {
            this.title = "";
        } else {
            this.title = title;
        }
    }
    
/**
 * Getter method to get the String attribute, <i>title</i>.
 */
    public String getTitle()
    {
        return title;
    }
}
