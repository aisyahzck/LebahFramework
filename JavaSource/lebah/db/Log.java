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

package lebah.db;

import java.util.Date;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Log
{

    private static int logCnt;

    public Log() 
    {
    }

    public static void print(String s)
    {
	    
        logCnt++;
        if(logCnt > 1000)
            logCnt = 1;
        System.out.println(logCnt + " : " + s);
        
        
    }

    public static void print(Throwable throwable, String s)
    {
        System.out.println(new Date() + ": " + s);
        System.out.println(": " + s);
        throwable.printStackTrace(System.out);
    }
}
