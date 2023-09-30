import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import com.google.gson.Gson;

import java.io.IOException;

@WebServlet("/getNumOfSimulations")
public class NumOfSimulationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Call the getNumOfSimulations method to obtain the number of simulations
            Integer numOfSimulations = adminManager.getNumOfSimulations();

            // Create an instance of Gson for JSON serialization
            Gson gson = new Gson();

            // Serialize the result to JSON
            String jsonResult = gson.toJson(numOfSimulations);

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Write the JSON response to the client
            response.getWriter().write(jsonResult);
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context.");
        }
    }
}
