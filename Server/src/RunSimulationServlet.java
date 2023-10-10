import Dto.RunSimulationDto;
import Dto.SimulationDetailsDto;
import com.google.gson.JsonParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

@WebServlet("/runSimulation")
public class RunSimulationServlet extends HttpServlet {
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            try {
                // Deserialize the JSON request body into a RunSimulationDto
                RunSimulationDto runSimulationDto = gson.fromJson(request.getReader(), RunSimulationDto.class);

                // Extract values from the RunSimulationDto
                int requestNum = runSimulationDto.getRequestNum();
                Map<String, Integer> entitiesPopulationMap = runSimulationDto.getEntitiesPopulationMap();
                Map<String, String> envPropValue = runSimulationDto.getEnvPropValue();

                // Run the simulation based on the extracted parameters
                Integer simulationId = adminManager.runSimulationByRequestNumber(requestNum, entitiesPopulationMap, envPropValue);

                // Send a success message in the response with the simulation ID as a query parameter
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Simulation started successfully. Simulation ID: " + simulationId);

                // Add simulationId as a query parameter to the response URL
                response.sendRedirect("/your-servlet-path?simulationId=" + simulationId);
            } catch (JsonParseException | IllegalArgumentException e) {
                // Handle JSON parsing or invalid data errors
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid JSON data: " + e.getMessage());
            } catch (Exception e) {
                // Handle any other exceptions that may occur during simulation execution
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Simulation execution failed: " + e.getMessage());
            }
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        // Get the "userName" and "simulationId" parameters from the request
        String userName = request.getParameter("userName");
        String simulationIdParam = request.getParameter("simulationId");

        if (adminManager != null && userName != null && !userName.isEmpty() && simulationIdParam != null && !simulationIdParam.isEmpty()) {
            try {
                Integer simulationId = Integer.parseInt(simulationIdParam);

                // Retrieve the SimulationDetailsDto using the getSimulationDetailsDto method
                SimulationDetailsDto simulationDetailsDto = adminManager.getSimulationDetailsDto(userName, simulationId);

                // Serialize the SimulationDetailsDto to JSON and send it as the response
                String jsonResponse = gson.toJson(simulationDetailsDto);

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().write(jsonResponse);
            } catch (NumberFormatException e) {
                // Handle the case when the "simulationId" parameter is not a valid integer
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid 'simulationId' parameter.");
            } catch (Exception e) {
                // Handle any other exceptions that may occur during the process
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error: " + e.getMessage());
            }
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context, or missing or empty 'userName' or 'simulationId' parameter.");
        }
    }
}
