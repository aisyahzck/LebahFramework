/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.portal.element;

import lebah.util.Logger;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class Role
{
    private String name;
    private String description;
    private Logger log;
    private String className = "mecca.object.Role";
    private boolean logger = false;
    
    public Role()
    {
        name = "";
        description = "";
        if (logger) log = new Logger(className);
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
}
