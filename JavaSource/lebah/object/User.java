/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.object;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class User
{
    private String login;
    private String password;
    private String name;
    private String role;
    private String scheme;
    
    public User()
    {
        init();
    }
    
/**
 * This method is used to reset all attributes to its initial state.
 */
    public void init()
    {
        login = "";
        password = "";
        name = "";
        role = "";
        scheme = "";
    }
    
    public void setLogin(String s1)
    {
        if (s1 == null)
        {
            login = "";
        } else {
            login = s1;
        }
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public void setPassword(String s1)
    {
        if (s1 == null)
        {
            password = "";
        } else {
            password = s1;
        }
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setName(String s1)
    {
        if (s1 == null)
        {
            name = "";
        } else {
            name = s1;
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setRole(String s1)
    {
        if (s1 == null)
        {
            role = "";
        } else {
            role = s1;
        }
    }
    
    public String getRole()
    {
        return role;
    }
    
    public void setScheme(String s1)
    {
        if (s1 == null)
        {
            scheme = "";
        } else {
            scheme = s1;
        }
    }
    
    public String getScheme()
    {
        return scheme;
    }
}
