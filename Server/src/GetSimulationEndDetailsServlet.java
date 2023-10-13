import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import Dto.SimulationEndDetailsDto;

import java.io.IOException;

@WebServlet("/getSimulationEndDetails")
public class GetSimulationEndDetailsServlet extends HttpServlet {
    private final Gson gson = new Gson();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            try {
                // Get the "userName" and "simulationId" parameters from the request
                String userName = request.getParameter("userName");
                String simulationIdParam = request.getParameter("simulationId");

                if (userName != null && !userName.isEmpty() && simulationIdParam != null && !simulationIdParam.isEmpty()) {
                    Integer simulationId = Integer.parseInt(simulationIdParam);

                    // Call the getSimulationEndDetailsDto method to retrieve the details
                    SimulationEndDetailsDto endDetailsDto = adminManager.getSimulationEndDetailsDto(userName, simulationId);

                    if (endDetailsDto != null) {
                        // Serialize the SimulationEndDetailsDto to JSON and send it as the response
                        String jsonResponse = gson.toJson(endDetailsDto);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.getWriter().write(jsonResponse);
                    } else {
                        // Handle the case when the requested details are not found
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("Simulation details not found.");
                    }
                } else {
                    // Handle the case when the required parameters are missing or empty
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Missing or empty 'userName' or 'simulationId' parameter.");
                }
            } catch (NumberFormatException e) {
                // Handle the case when the "simulationId" parameter is not a valid integer
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid 'simulationId' parameter: " + e.getMessage());
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
