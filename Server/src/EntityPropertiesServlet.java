import Dto.EntityPropertyDetailDto;
import Manager.PredictionManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;

@WebServlet("/getEntityProperties")
public class EntityPropertiesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the "entityName" parameter from the request
        String entityName = request.getParameter("entityName");

        if (entityName != null && !entityName.isEmpty()) {
            // Retrieve the PredictionManager instance from the servlet context
            PredictionManager manager = (PredictionManager) getServletContext().getAttribute("manager");

            if (manager != null) {
                // Call the getEntityPropertiesDetail method and get the result
                EntityPropertyDetailDto entityProperties = manager.getEntityPropertiesDetail(entityName);

                // Create an instance of Gson
                Gson gson = new Gson();

                // Convert the result to JSON using Gson
                String jsonResponse = gson.toJson(entityProperties);

                // Set the content type of the response to JSON
                response.setContentType("application/json");

                // Write the JSON response to the output stream
                response.getWriter().write(jsonResponse);
            } else {
                // Handle the case when the PredictionManager is not found in the servlet context
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("PredictionManager not found in the servlet context.");
            }
        } else {
            // Handle the case when the "entityName" parameter is missing or empty
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing or empty 'entityName' parameter.");
        }
    }
}
