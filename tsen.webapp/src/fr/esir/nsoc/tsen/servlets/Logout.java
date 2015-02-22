package fr.esir.nsoc.tsen.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet(name = "Logout", urlPatterns = { "/Logout", "/logout" })
public class Logout extends HttpServlet {

    public static final String VUE = "login";

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();

        //this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        response.sendRedirect( VUE );
    }

}
