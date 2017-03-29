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

import lebah.util.Logger;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class Module {
    private String id;
    private String title;
    private String className;
    private String group;
    private String description;
    private Boolean selected;
    private Logger log;
    private String cName = "mecca.object.Module";
    private boolean logger = false;
    
    public Module()
    {
        id = "";
        title = "";
        className = "";
        group = "";
        description = "";
        selected = Boolean.valueOf(false);
        if (logger) log = new Logger(cName);
    }
    
/**
 * Setter method to set the String attribute, <i>id</i>.
 */
    public void setId(String id)
    {
        if (id == null)
        {
            this.id = "";
        } else {
            this.id = id;
        }
    }
    
/**
 * Getter method to get the String attribute, <i>id</i>.
 */
    public String getId()
    {
        return id;
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
    
/**
 * Setter method to set the String attribute, <i>className</i>.
 */
    public void setClassName(String className)
    {
        if (className == null)
        {
            this.className = "";
        } else {
            this.className = className;
        }
    }
    
/**
 * Getter method to get the String attribute, <i>className</i>.
 */
    public String getClassName()
    {
        return className;
    }
    
/**
 * Setter method to set the String attribute, <i>group</i>.
 */
    public void setGroup(String group)
    {
        if (group == null)
        {
            this.group = "";
        } else {
            this.group = group;
        }
    }
    
/**
 * Getter method to get the String attribute, <i>group</i>.
 */
    public String getGroup()
    {
        return group;
    }
    
/**
 * Setter method to set the String attribute, <i>description</i>.
 */
    public void setDescription(String description)
    {
        if (description == null)
        {
            this.description = "";
        } else {
            this.description = description;
        }
    }
    
/**
 * Getter method to get the String attribute, <i>description</i>.
 */
    public String getDescription()
    {
        return description;
    }
    
/**
 * Setter method to set the Boolean attribute, <i>selected</i>.
 */
    public void setSelected(boolean selected)
    {
        this.selected = Boolean.valueOf(selected);
    }
    public void setSelected(String selected)
    {
        this.selected = Boolean.valueOf(selected);
    }
    
/**
 * Getter method to get the Boolean attribute, <i>selected</i>.
 */
    public Boolean isSelected()
    {
        return selected;
    }
    
}
