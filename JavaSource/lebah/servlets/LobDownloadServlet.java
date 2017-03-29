/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or





but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.servlets;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class LobDownloadServlet extends HttpServlet{
  private String dbUrl = 
    "jdbc:mysql://localhost/images";
  private String jdbcDriver = "com.mysql.jdbc.Driver";

  protected void doGet(HttpServletRequest request,
                      HttpServletResponse response)
  throws ServletException, IOException
  {
    ServletOutputStream out = 
      response.getOutputStream();
    int id = Integer.parseInt(
      request.getParameter("id"));
    response.setContentType("image/jpeg");
    out.write(getBlob(id));
    out.flush();
    out.close();
  }
  public byte[] getBlob(int id){
    String sqlQuery = 
      "SELECT BINARYDATA FROM BLOBS WHERE ID = ?;";
    Connection con = null;
    PreparedStatement pstmt  = null;
    ResultSet rs = null;
    Blob blob = null; 
    byte[] bytes = null;
    String description = "";
    try {
      Class.forName(jdbcDriver);    
      con = DriverManager.getConnection(dbUrl);
      pstmt = con.prepareStatement(sqlQuery);
      pstmt.setInt(1,id);  

      rs = pstmt.executeQuery();
      ResultSetMetaData md = rs.getMetaData();
      while (rs.next()) {
        blob = rs.getBlob(1);
      }
      bytes = blob.getBytes( 1, (int)(blob.length()));  
      con.close();
    }
    catch(ClassNotFoundException e){
      e.printStackTrace();
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    return bytes;
  }
}

 
