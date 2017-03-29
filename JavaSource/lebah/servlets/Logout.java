package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class Logout implements IServlet {
    
    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String visitor = session.getAttribute("_portal_visitor") != null ? 
			    (String) session.getAttribute("_portal_visitor") : "anon";
			     session.invalidate();
			     
		System.out.println("Logging out...");
		String randomNo = lebah.db.UniqueID.getUID();
	    response.sendRedirect("../c/?logoutrndId=" + randomNo); 
			     
		//out.println("<script>document.location = \"../servlet/lebah.servlets.Logout2?visitor=" + visitor + "\"</script>");
    }


}
