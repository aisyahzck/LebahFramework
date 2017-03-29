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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */

public class Logger
{
    private Calendar cal = Calendar.getInstance();
    private DateFormat df = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");
    private String className;
    
    public Logger(String className)
    {
        this.className = className;
    }
    
    public Logger(Class classIn)
    {
        className = classIn.getName();
    }    
/**
 * This method is used for printing messages out onto the system console.
 */
    public void setMessage(String msg)
    {
        String date = df.format(cal.getTime());
        System.out.println("["+date+"]"+className+": "+msg);
        //System.out.println(msg);
    }
}
