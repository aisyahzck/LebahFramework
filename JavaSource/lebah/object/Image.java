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

import java.sql.Blob;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class Image
{
    private String id;
    private String filename;
    private Blob blob;
    
    public Image()
    {
        id = "0";
        filename = "";
        blob = null;
    }
    
    public void setId(String s)
    {
        if (s == null)
        {
            id = "0";
        } else {
            id = s;
        }
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setFilename(String s)
    {
        if (s == null)
        {
            filename = "";
        } else {
            filename = s;
        }
    }
    
    public String getFilename()
    {
        return filename;
    }
    
    public void setBlob(Blob b)
    {
        blob = b;
    }
    
    public Blob getBlob()
    {
        return blob;
    }
    
}
