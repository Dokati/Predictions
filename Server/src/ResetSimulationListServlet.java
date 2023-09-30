import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.PredictionManager;
import java.io.IOException;

@WebServlet("/resetSimulationList")
public class ResetSimulationListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the PredictionManager instance from the servlet context
        PredictionManager manager = (PredictionManager) getServletContext().getAttribute("manager");

        if (manager != null) {
            // Call the resetSimulationList method to reset the simulation list
            manager.resetSimulationList();

            // Send a success message in the response
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Simulation list reset successfully.");
        } else {
            // Handle the case when the PredictionManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("PredictionManager not found in the servlet context.");
        }
    }
}
