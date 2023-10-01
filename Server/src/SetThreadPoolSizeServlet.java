import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import java.io.IOException;

@WebServlet("/setThreadPoolSize")
public class SetThreadPoolSizeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Get the "threadPoolSize" parameter from the request
            String threadPoolSizeParam = request.getParameter("threadPoolSize");

            if (threadPoolSizeParam != null && !threadPoolSizeParam.isEmpty()) {
                try {
                    // Parse the "threadPoolSize" parameter as an Integer
                    int threadPoolSize = Integer.parseInt(threadPoolSizeParam);

                    // Call the setThreadPoolSize method to set the thread pool size
                    adminManager.setThreadPoolSize(threadPoolSize);

                    // Send a success message in the response
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Thread pool size set successfully.");
                } catch (NumberFormatException e) {
                    // Handle the case when the "threadPoolSize" parameter is not a valid integer
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Invalid 'threadPoolSize' parameter.");
                }
            } else {
                // Handle the case when the "threadPoolSize" parameter is missing or empty
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing or empty 'threadPoolSize' parameter.");
            }
        }
    }
}
