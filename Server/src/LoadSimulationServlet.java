import Dto.SimulationTitlesDetails;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import Manager.PredictionManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
@WebServlet("/loadSimulationServlet")
public class LoadSimulationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the input stream from the request's body
        InputStream inputStream = request.getInputStream();

        // Check if the input stream is not null
        if (inputStream != null) {
            // Convert the input stream to a string
            String fileContent = convertInputStreamToString(inputStream);

            // Retrieve the PredictionManager instance from the servlet context
            PredictionManager predictionManager = (PredictionManager) getServletContext().getAttribute("manager");

            // Call the loadSimulation method with the file content string
            SimulationTitlesDetails simulationDetails = predictionManager.loadSimulation(fileContent);

            // Create an instance of Gson
            Gson gson = new Gson();

            // Serialize the result to JSON using Gson
            String jsonResult = gson.toJson(simulationDetails);

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Write the JSON response to the client
            response.getWriter().write(jsonResult);
        } else {
            // Handle the case where the input stream is missing
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing request body.");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}