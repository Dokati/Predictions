import Dto.RuleActivationDto;
import Manager.AdminManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.PredictionManager;

import java.io.IOException;

@WebServlet("/getRuleActivation")
public class RuleActivationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the "ruleName" parameter from the request
        String ruleName = request.getParameter("ruleName");

        if (ruleName != null && !ruleName.isEmpty()) {
            // Retrieve the PredictionManager instance from the servlet context
            AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

            if (adminManager != null) {
                String worldDefName = request.getParameter("worldDefName");
                // Call the RuleActivationDto method with the rule name
                RuleActivationDto ruleActivation = adminManager.getActivationDetails(worldDefName,ruleName);

                // Create an instance of Gson for JSON serialization
                Gson gson = new Gson();

                // Serialize the result to JSON
                String jsonResult = gson.toJson(ruleActivation);

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
            // Handle the case when the "ruleName" parameter is missing or empty
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or empty 'ruleName' parameter.");
        }
    }
}
