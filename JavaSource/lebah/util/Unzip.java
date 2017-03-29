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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */


public class Unzip {
	
	public static void main(String args[]) {
		String zipFilename = args[0];
		if ( args.length == 2 ) unzipFiles(zipFilename, args[1]);
		else  unzipFiles(zipFilename, "");
	 }
	 
	public static void unzipFiles(String zipFilename) {
		unzipFiles(zipFilename, "");	
	}
	 
	public static void unzipFiles(String zipFilename, String folder) {
		ZipFile zip = null;
	    try {
		    //System.out.println(zipFilename + ", " + folder);
		    zipFilename = zipFilename.replace('/', java.io.File.separatorChar);
	        // Open the ZIP file
	        zip = new ZipFile(zipFilename);
	        // Prepare a buffer
	    	byte[] buffer = new byte[16384];
	        // Enumerate each entry in the zip file
	        for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				//If the entry is not a directory - unzip it
				if ( !entry.isDirectory()) {
					//Get the filename
					String filename = entry.getName();
					//Make the filename proper
					filename = filename.replace('/', java.io.File.separatorChar);
					//Where to unzip the file
					filename = !"".equals(folder) ? folder + java.io.File.separatorChar + filename : filename;
					//System.out.println(filename);
					//Create a destination file object
					java.io.File destFile = new java.io.File(filename);
					//create folders
					String parent = destFile.getParent();
         			if ( parent != null )  {
            			java.io.File parentFile = new java.io.File(parent);
            			if ( !parentFile.exists() )  {
               				parentFile.mkdirs();
            			}
         			}
         			
         			//Get inputstream for this entry
         			InputStream in = zip.getInputStream(entry);
         			//Open the output stream where the inputstream from this entry will go
         			OutputStream outs = new FileOutputStream(filename);
         			//Write to outputstream
         			int count;
         			while ( (count=in.read(buffer)) != -1 ) outs.write(buffer, 0, count);         			
         			//close all stream
         			in.close();
         			outs.close();
         								
				}
	        }
	    } catch (IOException e) {
		    System.out.println(e.getMessage());
	    } finally {
		    try {
		    	if ( zip != null ) zip.close();
	    	} catch ( IOException e2) {}
	    }
	}			 	
}
