import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import java.io.IOException;

@WebServlet("/UserList")
public class UseListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Get the "userName" parameter from the request
            String userName = request.getParameter("user name");

            if (userName != null && !userName.isEmpty()) {
                try {
                    // Call the AddUserToList method to add the user to the list
                    adminManager.AddUserToList(userName);

                    // Send a success message in the response
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("User added to the list successfully.");
                } catch (IllegalArgumentException e) {
                    // Handle the case when a user with the same name already exists
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(e.getMessage());
                }
            } else {
                // Handle the case when the "userName" parameter is missing or empty
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing or empty 'userName' parameter.");
            }
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Get the "userName" parameter from the request
            String userName = request.getParameter("user name");

            if (userName != null && !userName.isEmpty()) {
                // Call the RemoveUserFromList method to remove the user from the list
                adminManager.RemoveUserFromList(userName);

                // Send a success message in the response
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("User removed from the list successfully.");
            } else {
                // Handle the case when the "userName" parameter is missing or empty
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing or empty 'userName' parameter.");
            }
        }
    }
}

