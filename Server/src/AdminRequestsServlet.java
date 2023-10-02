import Dto.AdminRequestDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import Dto.FullRequestDto;
import com.google.gson.Gson;
import UserRequest.UserRequestStatusType;
import java.io.IOException;
import java.util.List;

@WebServlet("/adminRequests")
public class AdminRequestsServlet extends HttpServlet {
    private final Gson gson = new Gson();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Call the getRequestslist method to obtain the list of requests
            List<AdminRequestDto> requestList = adminManager.getRequestslist();

            // Serialize the result to JSON
            String jsonResult = gson.toJson(requestList);

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Write the JSON response to the client
            response.getWriter().write(jsonResult);
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Get the "requestNum" parameter from the request
            String requestNumParam = request.getParameter("request number");

            if (requestNumParam != null && !requestNumParam.isEmpty()) {
                try {
                    // Parse the "requestNum" parameter as an Integer
                    int requestNum = Integer.parseInt(requestNumParam);

                    // Get the "status" parameter from the request
                    String statusParam = request.getParameter("status");

                    if (statusParam != null && !statusParam.isEmpty()) {
                        // Parse the "status" parameter as a UserRequestStatusType
                        UserRequestStatusType status = UserRequestStatusType.valueOf(statusParam);

                        // Call the SetRequestStatus method to update the request status
                        adminManager.SetRequestStatus(requestNum, status);

                        // Send a success message in the response
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Request status updated successfully.");
                    } else {
                        // Handle the case when the "status" parameter is missing or empty
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Missing or empty 'status' parameter.");
                    }
                } catch (NumberFormatException e) {
                    // Handle the case when the "requestNum" parameter is not a valid integer
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Invalid 'requestNum' parameter.");
                } catch (IllegalArgumentException e) {
                    // Handle the case when the "status" parameter is not a valid UserRequestStatusType
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Invalid 'status' parameter: " + e.getMessage());
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
