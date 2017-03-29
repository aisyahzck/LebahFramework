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

import javax.servlet.http.HttpSession;

import lebah.util.FileManagerModule.Resource;

public class AppFileManagerModule  extends FileManagerModule {
    
    protected void listFiles(HttpSession session, String dir) throws Exception {
        String current_dir = Resource.getAPP() + dir;
        listFilesInDirectory(session, dir, current_dir);
    }    
 

}
