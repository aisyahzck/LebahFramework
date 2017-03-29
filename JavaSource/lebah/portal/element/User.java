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
package lebah.portal.element;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class User {
    String login;
    String name;
    String password;
    String role;
    String style;
    String login_alt;
    
    public User()
    {
        this.login = "";
        this.name = "";
        this.password = "";
        this.role = "";
        this.style = "";    
        this.login_alt = "";
    }
    
    public User(String login, String name, String password, String role)
    {
        this.login = login;
        this.name = name;
        this.password = password;
        this.role = role;
        this.style = "";
    }
    
    public User(String login, String name, String password, String role, String style)
    {
        this.login = login;
        this.name = name;
        this.password = password;
        this.role = role;
        this.style = style;
    }
    
    public void setLogin(String login)
    {
        if (login == null)
        {
            this.login = "";
        } else {
            this.login = login;
        }
    }
    
    public void setAlternateLogin(String login_alt)
    {
        if (login_alt == null)
        {
            this.login_alt = "";
        } else {
            this.login_alt = login_alt;
        }
    }    
    
    public String getLogin()
    {
        return login;
    }
    
    public String getAlternateLogin()
    {
        return login_alt;
    }    
       
    public void setName(String name)
    {
        if (name == null)
        {
            this.name = "";
        } else {
            this.name = name;
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setPassword(String password)
    {
        if (password == null)
        {
            this.password = "";
        } else {
            this.password = password;
        }
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setRole(String role)
    {
        if (role == null)
        {
            this.role = "";
        } else {
            this.role = role;
        }
    }
    
    public String getRole()
    {
        return role;
    }
    
    public void setStyle(String style)
    {
        if (style == null)
        {
            this.style = "";
        } else {
            this.style = style;
        }
    }
    
    public String getStyle()
    {
        return style;
    }
}
