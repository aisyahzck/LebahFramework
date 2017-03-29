package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImgSrcServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("image/jpeg");
			out.write(getBlob(id));
			out.flush();
			out.close();			
		} catch (Exception e ) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			e.printStackTrace(out);
			out.flush();
			out.close();			
		}
	}

	private byte[] getBlob(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
