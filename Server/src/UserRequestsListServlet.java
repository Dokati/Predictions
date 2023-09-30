import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Manager.AdminManager;
import Dto.FullRequestDto;
import Dto.RequestDto;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

@WebServlet("/userRequestsList")
public class UserRequestsListServlet extends HttpServlet {
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            try {
                // Deserialize the JSON request body into a RequestDto object
                RequestDto requestDto = gson.fromJson(request.getReader(), RequestDto.class);

                // Call the addRequestToList method to add the request
                adminManager.addRequestToList(requestDto);

                // Send a success message in the response
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Request added successfully.");
            } catch (Exception e) {
                // Handle any exceptions that may occur during request processing
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid request format: " + e.getMessage());
            }
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the AdminManager instance from the servlet context
        AdminManager adminManager = (AdminManager) getServletContext().getAttribute("adminManager");

        if (adminManager != null) {
            // Get the "userName" parameter from the request
            String userName = request.getParameter("user name");

            if (userName != null && !userName.isEmpty()) {
                // Call the getUserRequestslist method to obtain the list of user requests
                List<FullRequestDto> requestList = adminManager.getUserRequestslist(userName);

                // Serialize the result to JSON
                String jsonResult = gson.toJson(requestList);

                // Set the response content type to JSON
                response.setContentType("application/json");

                // Write the JSON response to the client
                response.getWriter().write(jsonResult);
            } else {
                // Handle the case when the "userName" parameter is missing or empty
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing or empty 'userName' parameter.");
            }
        } else {
            // Handle the case when the AdminManager is not found in the servlet context
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("AdminManager not found in the servlet context.");
        }
    }
}
