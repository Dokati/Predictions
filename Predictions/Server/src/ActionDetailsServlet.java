import Dto.ActionDetailsDto;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.PredictionManager;

import java.io.IOException;

@WebServlet("/getActionDetails")
public class ActionDetailsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the "ruleName" and "index" parameters from the request
        String ruleName = request.getParameter("ruleName");
        String indexParam = request.getParameter("index");

        if (ruleName != null && !ruleName.isEmpty() && indexParam != null && !indexParam.isEmpty()) {
            try {
                // Parse the index parameter as an integer
                int index = Integer.parseInt(indexParam);

                // Retrieve the PredictionManager instance from the servlet context
                PredictionManager manager = (PredictionManager) getServletContext().getAttribute("manager");

                if (manager != null) {
                    // Call the getActionDetails method with the rule name and index
                    ActionDetailsDto actionDetails = manager.getActionDetails(index, ruleName);

                    // Create an instance of Gson for JSON serialization
                    Gson gson = new Gson();

                    // Serialize the result to JSON
                    String jsonResult = gson.toJson(actionDetails);

                    // Set the response content type to JSON
                    response.setContentType("application/json");

                    // Write the JSON response to the client
                    response.getWriter().write(jsonResult);
                } else {
                    // Handle the case when the PredictionManager is not found in the servlet context
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("PredictionManager not found in the servlet context.");
                }
            } catch (NumberFormatException e) {
                // Handle the case when the "index" parameter is not a valid integer
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid 'index' parameter.");
            }
        } else {
            // Handle the case when the "ruleName" or "index" parameter is missing or empty
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or empty 'ruleName' or 'index' parameter.");
        }
    }
}
