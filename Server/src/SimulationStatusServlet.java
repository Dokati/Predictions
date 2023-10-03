import World.instance.SimulationStatusType;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;

import java.io.IOException;

@WebServlet("/SimulationStatus")
public class SimulationStatusServlet extends HttpServlet {
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            try {
                // Get the "userName," "simulationId," and "status" parameters from the request
                String userName = request.getParameter("userName");
                String simulationIdParam = request.getParameter("simulationId");
                String statusParam = request.getParameter("status");

                if (userName != null && !userName.isEmpty() && simulationIdParam != null && !simulationIdParam.isEmpty() && statusParam != null && !statusParam.isEmpty()) {
                    Integer simulationId = Integer.parseInt(simulationIdParam);
                    SimulationStatusType status = SimulationStatusType.valueOf(statusParam);

                    // Call the setSimulationStatus method to update the status
                    adminManager.setSimulationStatus(userName, simulationId, status);

                    // Send a success message in the response
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Simulation status updated successfully.");
                } else {
                    // Handle the case when the required parameters are missing or empty
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Missing or empty 'userName,' 'simulationId,' or 'status' parameter.");
                }
            } catch (NumberFormatException e) {
                // Handle the case when the "simulationId" parameter is not a valid integer
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid 'simulationId' parameter.");
            } catch (IllegalArgumentException e) {
                // Handle the case when the "status" parameter is not a valid SimulationStatusType value
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid 'status' parameter.");
            } catch (Exception e) {
                // Handle any other exceptions that may occur during the process
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error: " + e.getMessage());
            }
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context.");
        }
    }
}
