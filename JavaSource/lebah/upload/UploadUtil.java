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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.jspsmart.upload.Files;
//import javax.servlet.http.HttpSession;

public class UploadUtil {
    
    public static String uploadFile(ServletContext servletContext, 
            HttpServletRequest request, HttpServletResponse response, 
            String uploadDir) throws Exception {
        lebah.upload.SmartUpload2 myUpload = new lebah.upload.SmartUpload2();
        myUpload.initialize2(servletContext, request, response);
        myUpload.upload();
        myUpload.save(uploadDir, myUpload.SAVE_PHYSICAL);
        Files files =myUpload.getFiles();
        String fileName = "";
        if ( files.getCount() > 0 ) {
            com.jspsmart.upload.File file = files.getFile(0);
            fileName = "/" + file.getFileName();
        }
        return fileName;
        
    }

}
