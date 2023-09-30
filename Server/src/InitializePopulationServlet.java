import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.PredictionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/initializePopulation")
public class InitializePopulationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve the JSON data from the request body
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuffer.append(line);
        }

        // Deserialize the JSON data into a Map using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Integer>>() {}.getType();
        Map<String, Integer> entitiesPopulationMap = gson.fromJson(jsonBuffer.toString(), type);

        // Retrieve the PredictionManager instance from the servlet context
        PredictionManager manager = (PredictionManager) getServletContext().getAttribute("manager");

        if (manager != null) {
            // Call the InitializePopulation method with the entity population data
            manager.InitializePopulation(entitiesPopulationMap);

            // Send a success message in the response
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Population initialized successfully.");
        } else {
            // Handle the case when the PredictionManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("PredictionManager not found in the servlet context.");
        }
    }
}
