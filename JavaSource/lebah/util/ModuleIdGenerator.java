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

package lebah.util;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class ModuleIdGenerator
{
    private String format = "dd-MM-yyyy-hh-mm-ss";
    
    public ModuleIdGenerator()
    {       
    }

/**
 * This method will generate a unique Module Id.
 */
    public String getModuleId()
    {
        DateUtil du = new DateUtil();
        String moduleId = du.toString(du.getToday(),format);
        return moduleId;
    }

}
