import Dto.GridDto;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.PredictionManager;

import java.io.IOException;

@WebServlet("/getGridDetails")
public class GridDetailsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the PredictionManager instance from the servlet context
        PredictionManager manager = (PredictionManager) getServletContext().getAttribute("manager");

        if (manager != null) {
            // Call the getGridDetails method to obtain grid details
            GridDto gridDetails = manager.getGridDetails();

            // Create an instance of Gson for JSON serialization
            Gson gson = new Gson();

            // Serialize the result to JSON
            String jsonResult = gson.toJson(gridDetails);

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Write the JSON response to the client
            response.getWriter().write(jsonResult);
        } else {
            // Handle the case when the PredictionManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("PredictionManager not found in the servlet context.");
        }
    }
}
