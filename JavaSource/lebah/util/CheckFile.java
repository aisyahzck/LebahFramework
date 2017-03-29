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

public class CheckFile
{
    public CheckFile()
    {
    }
    
    public boolean validate(String filename, String filepath)
    {
        boolean flag = false;
        String sourceFilePathName = filepath + "/" + filename;
        System.out.println("[CheckFile.isValid()] file = "+sourceFilePathName);
        try
        {       
            java.io.File file = new java.io.File(sourceFilePathName);
            flag = file.exists();
        }
        catch (NullPointerException npe)
        {
            System.out.println("[CheckFile.isValid()] NullPointerException: "+npe.getMessage() );
        }
        catch (SecurityException se)
        {
            System.out.println("[CheckFile.isValid()] SecurityException: "+se.getMessage() );
        }
        return flag;        
    }
}
