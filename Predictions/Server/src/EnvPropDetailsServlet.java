import Dto.EnvPropDto;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.PredictionManager;

import java.io.IOException;

@WebServlet("/getEnvPropDetails")
public class EnvPropDetailsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the "envPropName" parameter from the request
        String envPropName = request.getParameter("envProp Name");

        if (envPropName != null && !envPropName.isEmpty()) {
            // Retrieve the PredictionManager instance from the servlet context
            PredictionManager manager = (PredictionManager) getServletContext().getAttribute("manager");

            if (manager != null) {
                // Call the getEnvProp method with the environment property name
                EnvPropDto envPropDetails = manager.getEnvProp(envPropName);

                // Create an instance of Gson for JSON serialization
                Gson gson = new Gson();

                // Serialize the result to JSON
                String jsonResult = gson.toJson(envPropDetails);

                // Set the response content type to JSON
                response.setContentType("application/json");

                // Write the JSON response to the client
                response.getWriter().write(jsonResult);
            } else {
                // Handle the case when the PredictionManager is not found in the servlet context
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("PredictionManager not found in the servlet context.");
            }
        } else {
            // Handle the case when the "envPropName" parameter is missing or empty
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or empty 'envPropName' parameter.");
        }
    }
}
