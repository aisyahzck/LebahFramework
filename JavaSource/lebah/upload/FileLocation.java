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

package lebah.upload;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FileLocation {
    
    private static ResourceBundle rb;
    private static String PATH;
    private static String photosDir;
    
    static {
        try {
            rb = ResourceBundle.getBundle("files");
            read();
        } catch ( MissingResourceException e ) {
            System.out.println(e.getMessage()); 
        }
    }

    public static String getPATH() { return PATH; }
    public static String getPhotosDir() { return photosDir; }

    public static void read() {
        readPATH();
        readPhotosDir();
    }

    private static void readPATH() {
        try {
            PATH = rb.getString("dir" );
        } catch ( MissingResourceException e ) { 
            System.out.println("Recource - " + e.getMessage()); 
        }
    }
    
    private static void readPhotosDir() {
        try {
            photosDir = rb.getString("photosDir" );
        } catch ( MissingResourceException e ) { 
            System.out.println("Recource - " + e.getMessage()); 
        }
    }    

}   
