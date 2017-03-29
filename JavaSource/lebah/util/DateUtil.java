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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */

public class DateUtil
{
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private Calendar cal;
    
    public DateUtil()
    {
        cal = Calendar.getInstance();
    }
    
    public DateUtil(String date)
    {
        cal = Calendar.getInstance();
        cal.setTime(toDate(date));
    }
    
    public DateUtil(java.util.Date date)
    {
        cal = Calendar.getInstance();
        cal.setTime(date);
    }
    
    public Integer getDate()
    {
        return new Integer(cal.get(Calendar.DATE));
    }
    
    public Integer getMonth()
    {
        return new Integer(cal.get(Calendar.MONTH));
    }
    
    public Integer getYear()
    {
        return new Integer(cal.get(Calendar.YEAR));
    }
    
    public java.util.Date getToday()
    {        
        return cal.getTime();
    }
        
    public String toString(java.util.Date date, String dateFormat)
    {
        String d = "";
        if (date != null)
        {
            DateFormat dft = new SimpleDateFormat(dateFormat);
            d = dft.format(date);
        }
        return d;       
    }
    
    public String toString(java.util.Date date)
    {
        String d = "";
        if (date != null)
        {
            d = df.format(date);
        }
        return d;       
    }

    public java.util.Date toDate(String date, String dateFormat)
    {
        java.util.Date d = new java.util.Date();
        DateFormat dft = new SimpleDateFormat(dateFormat);
        try
        {
            d = dft.parse(date);
        }
        catch(ParseException pe)
        {
            System.out.println("DateUtil.toDate("+date+","+dateFormat+") exception: "+ pe.getMessage());
        }
        return d;
    }

    public java.util.Date toDate(String date)
    {
        java.util.Date d = new java.util.Date();
        try
        {
            d = df.parse(date);
        }
        catch(ParseException pe)
        {
            System.out.println("DateUtil.toDate("+date+") exception: "+ pe.getMessage());
        }
        return d;
    }
    
/**
 * This method takes a date format in String form and returns
 * a date as a String in the given format.
 */
    public String getDateString(String dateFormat)
    {
        String d = "";
        DateFormat dft = new SimpleDateFormat(dateFormat);
        d = dft.format(getToday());
        return d;       
    }
    
}
