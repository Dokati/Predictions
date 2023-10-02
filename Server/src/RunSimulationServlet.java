import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/runSimulation")
public class RunSimulationServlet extends HttpServlet {
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Get the "requestNum" parameter from the request
            String requestNumParam = request.getParameter("requestNum");

            if (requestNumParam != null && !requestNumParam.isEmpty()) {
                try {
                    // Parse the "requestNum" parameter as an Integer
                    int requestNum = Integer.parseInt(requestNumParam);

                    // Deserialize the JSON request body into a Map<String, Integer> for entitiesPopulationMap
                    Map<String, Integer> entitiesPopulationMap = gson.fromJson(request.getReader(), HashMap.class);

                    // Deserialize the JSON request body into a Map<String, String> for envPropValue
                    Map<String, String> envPropValue = gson.fromJson(request.getReader(), HashMap.class);

                    // Run the simulation based on the request number and parameters
                    adminManager.runSimulationByRequestNumber(requestNum, entitiesPopulationMap, envPropValue);

                    // Send a success message in the response
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Simulation started successfully.");
                } catch (NumberFormatException e) {
                    // Handle the case when the "requestNum" parameter is not a valid integer
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Invalid 'requestNum' parameter.");
                } catch (IllegalArgumentException e) {
                    // Handle invalid request numbers or missing users/simulations
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(e.getMessage());
                } catch (Exception e) {
                    // Handle any other exceptions that may occur during simulation execution
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Simulation execution failed: " + e.getMessage());
                }
            } else {
                // Handle the case when the "requestNum" parameter is missing or empty
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing or empty 'requestNum' parameter.");
            }
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context.");
        }
    }



}
