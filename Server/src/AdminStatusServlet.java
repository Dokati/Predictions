import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;

import java.io.IOException;

@WebServlet("/adminStatus")
public class AdminStatusServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager == null) {
            throw new ServletException("AdminManager not found in the servlet context.");
        }

        // Get the "adminUp" parameter from the request
        String adminStatus = request.getParameter("adminStatus");

        if (adminStatus != null && !adminStatus.isEmpty()) {
            // Parse the "adminUp" parameter as a boolean
            boolean adminUp = Boolean.parseBoolean(adminStatus);

            // Set the adminUp property in the AdminManager
            adminManager.setAdminUp(adminUp);

            // Send a success message in the response
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Admin status updated successfully.");
        } else {
            // Handle the case when the "adminUp" parameter is missing or empty
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or empty 'adminUp' parameter.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager == null) {
            throw new ServletException("AdminManager not found in the servlet context.");
        }

        // Get the current adminUp status from the AdminManager
        boolean adminUp = adminManager.isAdminUp();

        // Send the adminUp status in the response as JSON
        response.setContentType("application/json");
        response.getWriter().write("{\"adminUp\": " + adminUp + "}");
    }
}
