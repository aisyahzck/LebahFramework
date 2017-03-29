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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateValue {
    
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private boolean ampm;
    private int hourOfDay;
    private String timeZoneId;
    
    public DateValue() {
        
    }
    
    public DateValue(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR);
        minutes = c.get(Calendar.MINUTE);
        ampm = c.get(Calendar.AM_PM) == Calendar.AM;
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
    }
    
    public String getTimeZoneId() {
        return timeZoneId;
    }
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
    public boolean isAmpm() {
        return ampm;
    }
    public void setAmpm(boolean ampm) {
        this.ampm = ampm;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinutes() {
        return minutes;
    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    
    public String toString() {
        return DateTool.getDateStr(year, month, day);
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }
    
    public Calendar getCalendar() {
        Calendar c = new GregorianCalendar(year, month-1, day);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        return c;
    }

}
