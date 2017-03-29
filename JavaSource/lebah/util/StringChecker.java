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

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */

public class StringChecker
{
    
    public StringChecker()
    {
    }
    
    public static String invComma(String str)
    {
        if (str != null)
        {
            int i = str.length();
            String temp = "";
            String txt = "";
            for (int j = 0; j < i; j++)
            {
                temp = str.substring(j,j+1);
                if (temp.equals("'"))
                {
                    txt = txt + "\\" + temp;
                } else {
                    txt = txt + temp;
                }
            }
            return txt;
        } else {
            return "";
        }
    }
    
    public static String parse(String str)
    {
        if (str != null)
        {
            int i = str.length();
            String temp = "";
            String txt = "";
            for (int j = 0; j < i; j++)
            {
                temp = str.substring(j,j+1);
                if ((temp.equals("'")) || (temp.equals(",")))
                {
                    txt = txt + "\\" + temp;
                } else {
                    txt = txt + temp;
                }
            }
            return txt;
        } else {
            return "";
        }
    }   

    public static String putLineBreak(String str)
    {
        if (str != null)
        {
            StringBuffer txt = new StringBuffer(str);
            char c = '\n';
            while (txt.toString().indexOf(c) > -1) {
                int pos = txt.toString().indexOf(c);
                txt.replace(pos, pos + 1, "<br>");
            }
            return txt.toString();
        } else {
            return "";
        }
    }
}
